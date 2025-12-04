/**
 * Settings Routes
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import * as settingsController from '../controllers/settingsController';

const router = Router();

// Public routes
router.get('/app/version', settingsController.checkAppVersion);
router.get('/system/config', settingsController.getSystemConfig);

// Authenticated routes
router.use(authenticate);

router.post('/feedback', settingsController.submitFeedback);
router.get('/cache/size', settingsController.getCacheSize);
router.post('/cache/clear', settingsController.clearCache);

export default router;
