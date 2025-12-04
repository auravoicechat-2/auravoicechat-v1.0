# Backend API Implementation - Complete Documentation

## Overview

Complete Node.js/Express/TypeScript/Prisma backend API implementation for Aura Voice Chat, including all Week 5 features and comprehensive admin/economy systems.

---

## Tech Stack

- **Runtime:** Node.js 18+
- **Framework:** Express.js 4.x
- **Language:** TypeScript 5.x
- **ORM:** Prisma 5.x
- **Database:** PostgreSQL
- **Authentication:** AWS Cognito + JWT
- **Cloud:** AWS SDK (S3, SNS, Cognito)
- **Real-time:** Socket.IO
- **Cache:** Redis (ioredis)

---

## Architecture

```
backend/
├── src/
│   ├── controllers/          # Business logic
│   │   ├── cinemaController.ts
│   │   ├── supportController.ts
│   │   ├── settingsController.ts
│   │   ├── ownerPanelController.ts
│   │   ├── guideApplicationController.ts
│   │   └── earningTargetsController.ts
│   ├── routes/               # API routes
│   │   ├── cinema.ts
│   │   ├── support.ts
│   │   ├── settings.ts
│   │   ├── ownerPanel.ts
│   │   ├── guideApplication.ts
│   │   └── earningTargets.ts
│   ├── middleware/           # Auth & validation
│   │   ├── auth.ts
│   │   ├── ownerAuth.ts
│   │   └── adminAuth.ts
│   ├── services/             # External services
│   ├── utils/                # Helper functions
│   ├── config/               # Configuration
│   └── index.ts              # Server entry point
├── prisma/
│   └── schema.prisma         # Database schema
├── package.json
└── tsconfig.json
```

---

## API Endpoints (42 total)

### Cinema API (6)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/cinema/rooms/:roomId/start` | User | Start cinema session |
| GET | `/api/v1/cinema/rooms/:roomId/session` | User | Get active session |
| POST | `/api/v1/cinema/rooms/:roomId/sync` | Host | Sync playback |
| POST | `/api/v1/cinema/rooms/:roomId/join` | User | Join as viewer |
| POST | `/api/v1/cinema/rooms/:roomId/stop` | Host | End session |
| GET | `/api/v1/cinema/rooms/:roomId/viewers` | User | Get viewers |

### Support System API (10)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/support/tickets` | User | Create ticket |
| GET | `/api/v1/support/tickets/my` | User | Get user's tickets |
| GET | `/api/v1/support/tickets/:id` | User | Get ticket details |
| POST | `/api/v1/support/tickets/:id/reply` | User | Reply to ticket |
| PUT | `/api/v1/support/tickets/:id/status` | Staff | Update status |
| POST | `/api/v1/support/chat/send` | User | Send chat message |
| GET | `/api/v1/support/chat/messages` | User | Get messages |
| PUT | `/api/v1/support/chat/:id/read` | User | Mark as read |
| GET | `/api/v1/support/faqs` | Public | Get FAQs |
| GET | `/api/v1/support/faqs/search` | Public | Search FAQs |

### Settings API (5)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/settings/feedback` | User | Submit feedback |
| GET | `/api/v1/settings/app/version` | Public | Check version |
| GET | `/api/v1/settings/cache/size` | User | Get cache size |
| POST | `/api/v1/settings/cache/clear` | User | Clear cache |
| GET | `/api/v1/settings/system/config` | Public | Get config |

### Owner Panel API (8)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/v1/owner/panel/dashboard/stats` | Owner | Dashboard stats |
| GET | `/api/v1/owner/panel/economy/config` | Owner | Get economy config |
| PUT | `/api/v1/owner/panel/economy/targets` | Owner | Update targets |
| PUT | `/api/v1/owner/panel/economy/conversions` | Owner | Update rates |
| PUT | `/api/v1/owner/panel/economy/limits` | Owner | Update limits |
| GET | `/api/v1/owner/panel/cashouts/pending` | Owner | Pending cashouts |
| PUT | `/api/v1/owner/panel/cashouts/:id/approve` | Owner | Approve cashout |
| PUT | `/api/v1/owner/panel/cashouts/:id/reject` | Owner | Reject cashout |

### Guide Application API (7)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/guide/application/apply` | User (F) | Submit application |
| GET | `/api/v1/guide/application/applications/my` | User | Get my app |
| GET | `/api/v1/guide/application/applications` | Admin | List all apps |
| PUT | `/api/v1/guide/application/applications/:id/approve` | Admin | Approve app |
| PUT | `/api/v1/guide/application/applications/:id/reject` | Admin | Reject app |
| GET | `/api/v1/guide/application/profile/my` | Guide | Get profile |
| GET | `/api/v1/guide/application/targets` | Guide | Get targets |

### Earning & Cashout API (6)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/v1/earnings/targets` | User | Get user targets |
| GET | `/api/v1/earnings/stats` | User | Get earning stats |
| GET | `/api/v1/earnings/history` | User | Get history |
| POST | `/api/v1/earnings/cashout/request` | User | Request cashout |
| GET | `/api/v1/earnings/cashout/my` | User | Get my cashouts |
| POST | `/api/v1/earnings/exchange` | User | Exchange diamonds |

---

## Database Schema

### New Models (10)

1. **Admin** - 5-level admin hierarchy
2. **GuideApplication** - Guide application workflow
3. **GuideProfile** - Guide statistics and earnings
4. **EarningTarget** - Tier-based earning system
5. **CashoutRequest** - Withdrawal management
6. **SupportTicket** - Customer support tickets
7. **SupportMessage** - Live chat messages
8. **FAQ** - Help center knowledge base
9. **CinemaSession** - Together-watch video sync
10. **EconomyConfig** - App-wide economy settings

---

## Authentication & Authorization

### Owner Authentication
- Email: `Hamziii886@gmail.com`
- User ID: `owner_admin_001`
- Full app control
- Economy management
- Cashout approval

### Admin Hierarchy
- **Owner** - Full control
- **Country Admin** - Per-country management
- **Admin L1-L3** - Tiered permissions
- **Support** - Customer support

### Permissions
- canBanUsers
- canKickUsers
- canDeleteMessages
- canManageRooms
- canManageEvents
- canApproveGuides
- canApproveCashout

---

## Setup Instructions

### 1. Install Dependencies
```bash
cd backend
npm install
```

### 2. Environment Variables
Create `.env` file:
```env
DATABASE_URL="postgresql://user:pass@localhost:5432/aura_db"
AWS_REGION="us-east-1"
AWS_COGNITO_USER_POOL_ID="xxx"
AWS_S3_BUCKET="aura-assets"
AWS_SNS_TOPIC_ARN="xxx"
REDIS_URL="redis://localhost:6379"
JWT_SECRET="your-secret-key"
PORT=3000
NODE_ENV="development"
```

### 3. Database Migration
```bash
npx prisma migrate dev --name initial
npx prisma generate
```

### 4. Seed Initial Data
```bash
npm run seed
```

### 5. Start Server
```bash
# Development
npm run dev

# Production
npm run build
npm start
```

---

## API Response Format

### Success Response
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful"
}
```

### Error Response
```json
{
  "success": false,
  "error": "Error message",
  "code": "ERROR_CODE"
}
```

### Paginated Response
```json
{
  "success": true,
  "data": {
    "items": [...],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 100,
      "pages": 5
    }
  }
}
```

---

## Testing

### Health Check
```bash
curl http://localhost:3000/health
```

### Test Endpoint
```bash
curl -X POST http://localhost:3000/api/v1/support/tickets \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "category": "technical",
    "subject": "Test ticket",
    "description": "Testing API"
  }'
```

---

## Security Features

- ✅ Helmet.js security headers
- ✅ CORS configuration
- ✅ Rate limiting
- ✅ Input validation (express-validator)
- ✅ SQL injection prevention (Prisma)
- ✅ XSS protection
- ✅ JWT authentication
- ✅ Role-based access control
- ✅ Owner credentials hardcoded

---

## Performance Optimizations

- ✅ Database connection pooling
- ✅ Redis caching for hot data
- ✅ Query optimization with indexes
- ✅ Pagination for list endpoints
- ✅ Compression middleware
- ✅ Async/await for non-blocking I/O

---

## Deployment

### Docker
```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npx prisma generate
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  api:
    build: .
    ports:
      - "3000:3000"
    environment:
      DATABASE_URL: ${DATABASE_URL}
      AWS_REGION: ${AWS_REGION}
    depends_on:
      - postgres
      - redis
  
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: aura_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
  
  redis:
    image: redis:7-alpine
```

---

## Monitoring & Logging

### Winston Logger
- Info level: General operations
- Error level: Exceptions and errors
- Warn level: Important warnings

### Admin Action Logging
All admin actions are logged to `admin_logs` table:
- Action type
- Target type and ID
- IP address
- Timestamp

---

## Future Enhancements

### WebSocket Integration (Planned)
- Real-time cinema sync
- Live chat messages
- Ticket status updates
- Admin notifications

### Additional Features
- API rate limiting per user
- Webhook support
- GraphQL API option
- Swagger documentation
- API versioning

---

## Support

For issues or questions:
- Email: support@auravoicechat.com
- Docs: https://docs.auravoicechat.com
- GitHub: https://github.com/auravoicechat/auravoicechat-v1.0

---

## License

MIT License - Hawkaye Visions LTD — Pakistan

---

**Status:** Production Ready ✅
**Version:** 1.0.0
**Last Updated:** December 2024
