/**
 * Daily Rewards Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * GET /rewards/daily/status - Get daily reward status
 * POST /rewards/daily/claim - Claim daily reward
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import * as rewardsController from '../controllers/rewardsController';

const router = Router();

// Get daily status
router.get('/daily/status', authenticate, rewardsController.getDailyStatus);

// Claim reward
router.post('/daily/claim', authenticate, rewardsController.claimDailyReward);

export default router;
