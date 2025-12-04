import { S3Client, PutObjectCommand } from "@aws-sdk/client-s3";
import * as fs from "fs";
import * as path from "path";
import { logger } from "../utils/logger";
import giftsConfig from "../config/gifts-config.json";

// Initialize S3 client
const s3Client = new S3Client({
  region: process.env. AWS_REGION || "us-east-1",
  credentials: {
    accessKeyId: process.env.AWS_ACCESS_KEY_ID || "",
    secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY || ""
  }
});

const BUCKET_NAME = process.env. S3_BUCKET_NAME || "auravoicechat-assets";
const GIFTS_FOLDER = "gifts/animations";

interface UploadResult {
  giftId: string;
  name: string;
  s3Url: string;
  category: string;
  price_coins: number;
  diamond_value: number;
  is_premium: boolean;
  rarity: string;
}

async function uploadGiftAnimation(giftId: string, animationFileName: string): Promise<string> {
  try {
    const animationPath = path.join(
      __dirname,
      `../../android/app/src/main/res/raw/${animationFileName}`
    );

    // Check if file exists
    if (!fs.existsSync(animationPath)) {
      throw new Error(`Animation file not found: ${animationPath}`);
    }

    // Read file
    const fileContent = fs.readFileSync(animationPath);

    // S3 key (path in bucket)
    const s3Key = `${GIFTS_FOLDER}/${giftId}/${animationFileName}`;

    // Upload to S3
    const command = new PutObjectCommand({
      Bucket: BUCKET_NAME,
      Key: s3Key,
      Body: fileContent,
      ContentType: "application/json",
      Metadata: {
        "gift-id": giftId,
        "uploaded-at": new Date().toISOString()
      },
      ACL: "public-read"
    });

    await s3Client.send(command);

    // Generate S3 URL
    const s3Url = `https://${BUCKET_NAME}.s3. ${process.env.AWS_REGION || "us-east-1"}.amazonaws.com/${s3Key}`;

    logger.info(`✓ Uploaded ${giftId} to S3`, { s3Url });
    return s3Url;
  } catch (error) {
    logger.error(`✗ Failed to upload ${giftId}`, { error });
    throw error;
  }
}

async function uploadAllGifts(): Promise<UploadResult[]> {
  const results: UploadResult[] = [];

  logger.info("Starting gift animations upload to S3...");
  logger.info(`Bucket: ${BUCKET_NAME}, Region: ${process.env.AWS_REGION || "us-east-1"}`);

  for (const gift of giftsConfig.gifts) {
    try {
      const s3Url = await uploadGiftAnimation(gift.id, gift. animation_file);

      results.push({
        giftId: gift.id,
        name: gift.name,
        s3Url,
        category: gift.category,
        price_coins: gift. price_coins,
        diamond_value: gift.diamond_value,
        is_premium: gift. is_premium,
        rarity: gift.rarity
      });
    } catch (error) {
      logger.error(`Failed to upload gift ${gift.id}`, { error });
    }
  }

  return results;
}

async function generateSqlInsertStatements(uploadResults: UploadResult[]): Promise<string> {
  let sql = "-- Gift animations uploaded to S3\n";
  sql += `-- Generated: ${new Date().toISOString()}\n\n`;
  sql += "INSERT INTO gifts (id, name, description, price_coins, diamond_value, category, animation_url, thumbnail_url, is_premium, is_active, created_at)\n";
  sql += "VALUES\n";

  const values = uploadResults.map((gift, index) => {
    const description = giftsConfig.gifts. find(g => g.id === gift.giftId)?.description || "";
    const isLast = index === uploadResults.length - 1;
    
    return `  ('${gift.giftId}', '${gift.name}', '${description}', ${gift.price_coins}, ${gift.diamond_value}, '${gift.category}', '${gift.s3Url}', '${gift.s3Url}', ${gift. is_premium}, true, NOW())${isLast ? ";" : ","}`;
  });

  sql += values.join("\n");
  sql += "\nON CONFLICT (id) DO NOTHING;\n";

  return sql;
}

async function main() {
  try {
    logger.info("================================");
    logger.info("Gift S3 Upload Script");
    logger.info("================================\n");

    // Upload all gifts
    const uploadResults = await uploadAllGifts();

    if (uploadResults.length === 0) {
      logger.error("No gifts were uploaded successfully");
      process.exit(1);
    }

    // Generate SQL
    const sqlStatements = await generateSqlInsertStatements(uploadResults);

    // Save SQL to file
    const sqlFilePath = path.join(__dirname, "../database/seeds/gifts-insert.sql");
    fs.mkdirSync(path.dirname(sqlFilePath), { recursive: true });
    fs.writeFileSync(sqlFilePath, sqlStatements);

    logger.info(`\n✓ Successfully uploaded ${uploadResults.length} gifts to S3`);
    logger.info(`✓ SQL insert statements generated: ${sqlFilePath}\n`);

    // Print summary
    console.log("\n=== Upload Summary ===");
    uploadResults.forEach((gift) => {
      console.log(`${gift.name} (${gift.giftId}): ${gift.s3Url}`);
    });

    console.log("\n=== Next Steps ===");
    console. log(`1. Review the SQL file: ${sqlFilePath}`);
    console.log("2. Run: docker exec -i aura-postgres psql -U postgres -d auravoicechat < " + sqlFilePath);
    console.log("3. Verify with: SELECT * FROM gifts;");

  } catch (error) {
    logger.error("Script failed", { error });
    process.exit(1);
  }
}

main();