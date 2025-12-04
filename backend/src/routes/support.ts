/**
 * Support Routes - Tickets and Live Chat
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import * as supportController from '../controllers/supportController';

const router = Router();

// All support routes require authentication
router.use(authenticate);

// Support tickets
router.post('/tickets', supportController.createTicket);
router.get('/tickets/my', supportController.getMyTickets);
router.get('/tickets/:id', supportController.getTicketDetails);
router.post('/tickets/:id/reply', supportController.replyToTicket);
router.put('/tickets/:id/status', supportController.updateTicketStatus);

// Live chat
router.post('/chat/send', supportController.sendChatMessage);
router.get('/chat/messages', supportController.getChatMessages);
router.put('/chat/:id/read', supportController.markMessageRead);

// FAQs
router.get('/faqs', supportController.getFAQs);
router.get('/faqs/search', supportController.searchFAQs);

export default router;
