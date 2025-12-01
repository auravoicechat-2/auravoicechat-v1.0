/**
 * Gifts Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Routes for gift catalog and sending gifts.
 */

import { Router } from 'express';
import { authenticate, optionalAuth } from '../middleware/auth';
import { generalLimiter, giftLimiter } from '../middleware/rateLimiter';
import { query } from '../config/database.config';
import { AppError } from '../middleware/errorHandler';
import { logger } from '../utils/logger';

const router = Router();

// Apply rate limiting
router.use(generalLimiter);

// Get all gifts (public)
router.get('/', optionalAuth, async (req, res, next) => {
    try {
        const { category } = req.query;
        
        let queryText = `
            SELECT * FROM gifts 
            WHERE is_active = true
        `;
        const params: any[] = [];
        
        if (category) {
            queryText += ` AND category = $1`;
            params.push(category);
        }
        
        queryText += ` ORDER BY price_coins ASC`;
        
        const result = await query(queryText, params);
        
        res.json({ gifts: result.rows });
    } catch (error) {
        next(error);
    }
});

// Get gift by ID
router.get('/:giftId', optionalAuth, async (req, res, next) => {
    try {
        const { giftId } = req.params;
        
        const result = await query(
            'SELECT * FROM gifts WHERE id = $1 AND is_active = true',
            [giftId]
        );
        
        if (result.rows.length === 0) {
            throw new AppError('Gift not found', 404, 'GIFT_NOT_FOUND');
        }
        
        res.json({ gift: result.rows[0] });
    } catch (error) {
        next(error);
    }
});

// Send a gift (authenticated)
router.post('/send', authenticate, giftLimiter, async (req, res, next) => {
    try {
        const senderId = req.user?.id;
        const { giftId, receiverId, roomId, quantity = 1 } = req.body;
        
        if (!giftId || !receiverId) {
            throw new AppError('Gift ID and receiver ID are required', 400, 'MISSING_FIELDS');
        }
        
        if (quantity < 1 || quantity > 999) {
            throw new AppError('Quantity must be between 1 and 999', 400, 'INVALID_QUANTITY');
        }
        
        // Get gift details
        const giftResult = await query(
            'SELECT * FROM gifts WHERE id = $1 AND is_active = true',
            [giftId]
        );
        
        if (giftResult.rows.length === 0) {
            throw new AppError('Gift not found', 404, 'GIFT_NOT_FOUND');
        }
        
        const gift = giftResult.rows[0];
        const totalCost = gift.price_coins * quantity;
        const totalDiamonds = gift.diamond_value * quantity;
        
        // Check sender balance
        const senderResult = await query(
            'SELECT coins FROM users WHERE id = $1',
            [senderId]
        );
        
        if (senderResult.rows.length === 0 || senderResult.rows[0].coins < totalCost) {
            throw new AppError('Insufficient coins', 400, 'INSUFFICIENT_BALANCE');
        }
        
        // Check receiver exists
        const receiverResult = await query(
            'SELECT id FROM users WHERE id = $1',
            [receiverId]
        );
        
        if (receiverResult.rows.length === 0) {
            throw new AppError('Receiver not found', 404, 'RECEIVER_NOT_FOUND');
        }
        
        // Perform transaction
        // 1. Deduct coins from sender
        await query(
            'UPDATE users SET coins = coins - $1 WHERE id = $2',
            [totalCost, senderId]
        );
        
        // 2. Add diamonds to receiver
        await query(
            'UPDATE users SET diamonds = diamonds + $1 WHERE id = $2',
            [totalDiamonds, receiverId]
        );
        
        // 3. Record transaction
        const transactionResult = await query(
            `INSERT INTO gift_transactions (gift_id, sender_id, receiver_id, room_id, quantity, coins_spent, diamonds_earned, created_at)
             VALUES ($1, $2, $3, $4, $5, $6, $7, NOW()) RETURNING *`,
            [giftId, senderId, receiverId, roomId, quantity, totalCost, totalDiamonds]
        );
        
        // 4. Record earnings for receiver (for earning targets)
        await query(
            `INSERT INTO earnings (user_id, type, amount, source_type, source_id, description, status, created_at)
             VALUES ($1, 'gift', $2, 'gift_transaction', $3, $4, 'completed', NOW())`,
            [receiverId, totalDiamonds, transactionResult.rows[0].id, `Received ${quantity}x ${gift.name}`]
        );
        
        // 5. Add wallet transaction for sender
        await query(
            `INSERT INTO wallet_transactions (user_id, type, amount, currency, description, reference_id, created_at)
             VALUES ($1, 'gift_sent', $2, 'coins', $3, $4, NOW())`,
            [senderId, -totalCost, `Sent ${quantity}x ${gift.name}`, transactionResult.rows[0].id]
        );
        
        logger.info(`Gift sent: ${senderId} -> ${receiverId}, ${quantity}x ${gift.name}`);
        
        res.json({
            success: true,
            transaction: transactionResult.rows[0],
            message: `Successfully sent ${quantity}x ${gift.name}!`
        });
    } catch (error) {
        next(error);
    }
});

// Get gift transactions for a user
router.get('/transactions/history', authenticate, async (req, res, next) => {
    try {
        const userId = req.user?.id;
        const { type = 'both', page = 1, limit = 20 } = req.query;
        const offset = (Number(page) - 1) * Number(limit);
        
        let queryText = '';
        
        if (type === 'sent') {
            queryText = `
                SELECT gt.*, g.name as gift_name, g.thumbnail_url,
                       u.display_name as receiver_name, u.avatar_url as receiver_avatar
                FROM gift_transactions gt
                JOIN gifts g ON gt.gift_id = g.id
                JOIN users u ON gt.receiver_id = u.id
                WHERE gt.sender_id = $1
                ORDER BY gt.created_at DESC
                LIMIT $2 OFFSET $3
            `;
        } else if (type === 'received') {
            queryText = `
                SELECT gt.*, g.name as gift_name, g.thumbnail_url,
                       u.display_name as sender_name, u.avatar_url as sender_avatar
                FROM gift_transactions gt
                JOIN gifts g ON gt.gift_id = g.id
                JOIN users u ON gt.sender_id = u.id
                WHERE gt.receiver_id = $1
                ORDER BY gt.created_at DESC
                LIMIT $2 OFFSET $3
            `;
        } else {
            queryText = `
                SELECT gt.*, g.name as gift_name, g.thumbnail_url,
                       s.display_name as sender_name, s.avatar_url as sender_avatar,
                       r.display_name as receiver_name, r.avatar_url as receiver_avatar,
                       CASE WHEN gt.sender_id = $1 THEN 'sent' ELSE 'received' END as direction
                FROM gift_transactions gt
                JOIN gifts g ON gt.gift_id = g.id
                JOIN users s ON gt.sender_id = s.id
                JOIN users r ON gt.receiver_id = r.id
                WHERE gt.sender_id = $1 OR gt.receiver_id = $1
                ORDER BY gt.created_at DESC
                LIMIT $2 OFFSET $3
            `;
        }
        
        const result = await query(queryText, [userId, limit, offset]);
        
        res.json({ transactions: result.rows });
    } catch (error) {
        next(error);
    }
});

export default router;
