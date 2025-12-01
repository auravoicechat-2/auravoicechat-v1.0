/**
 * Notifications Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * GET /notifications - Get user's notifications
 * POST /notifications/:notificationId/read - Mark notification as read
 * POST /notifications/read-all - Mark all notifications as read
 * GET /notifications/system - Get system messages
 */

import { Router } from 'express';
import { generalLimiter } from '../middleware/rateLimiter';
import { authMiddleware } from '../middleware/auth';
import * as notificationsController from '../controllers/notificationsController';

const router = Router();

// All notification routes require authentication
router.use(authMiddleware);

// Get notifications
router.get('/', generalLimiter, notificationsController.getNotifications);

// Mark notification as read
router.post('/:notificationId/read', generalLimiter, notificationsController.markAsRead);

// Mark all notifications as read
router.post('/read-all', generalLimiter, notificationsController.markAllAsRead);

// Get system messages
router.get('/system', generalLimiter, notificationsController.getSystemMessages);

export default router;
