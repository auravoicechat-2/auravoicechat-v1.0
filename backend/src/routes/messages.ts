/**
 * Messages Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * GET /messages/conversations - Get user's conversations
 * GET /messages/conversations/:conversationId - Get messages in a conversation
 * POST /messages/send - Send a message
 */

import { Router } from 'express';
import { generalLimiter } from '../middleware/rateLimiter';
import { authMiddleware } from '../middleware/auth';
import * as messagesController from '../controllers/messagesController';

const router = Router();

// All message routes require authentication
router.use(authMiddleware);

// Get conversations
router.get('/conversations', generalLimiter, messagesController.getConversations);

// Get messages in a conversation
router.get('/conversations/:conversationId', generalLimiter, messagesController.getMessages);

// Send a message
router.post('/send', generalLimiter, messagesController.sendMessage);

export default router;
