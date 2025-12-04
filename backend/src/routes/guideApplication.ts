/**
 * Guide Application Routes
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { requireAdmin } from '../middleware/adminAuth';
import * as guideController from '../controllers/guideApplicationController';

const router = Router();

// All routes require authentication
router.use(authenticate);

// User routes
router.post('/apply', guideController.submitApplication);
router.get('/applications/my', guideController.getMyApplication);
router.get('/profile/my', guideController.getMyGuideProfile);
router.get('/targets', guideController.getGuideTargets);

// Admin routes
router.get('/applications', requireAdmin, guideController.getAllApplications);
router.put('/applications/:id/approve', requireAdmin, guideController.approveApplication);
router.put('/applications/:id/reject', requireAdmin, guideController.rejectApplication);

export default router;
