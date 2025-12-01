/**
 * Guide System Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Routes for guide applications, dashboard, targets, and earnings.
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { generalLimiter, withdrawalLimiter } from '../middleware/rateLimiter';
import * as guideController from '../controllers/guideController';

const router = Router();

// Apply rate limiting and authentication
router.use(generalLimiter);
router.use(authenticate);

// ==================== GUIDE STATUS ====================
// Check guide status
router.get('/status', guideController.getGuideStatus);

// Check eligibility for guide application
router.get('/eligibility', guideController.checkEligibility);

// Apply to become a guide
router.post('/apply', guideController.applyForGuide);

// ==================== GUIDE DASHBOARD ====================
// Get guide dashboard (main overview)
router.get('/dashboard', guideController.getDashboard);

// Get guide targets for a specific month
router.get('/targets/:yearMonth', guideController.getTargets);

// Get daily tracking for a specific date
router.get('/daily/:date', guideController.getDailyTracking);

// ==================== GUIDE EARNINGS ====================
// Get guide earnings wallet
router.get('/earnings', guideController.getEarnings);

// Request withdrawal
router.post('/earnings/withdraw', withdrawalLimiter, guideController.requestWithdrawal);

// Convert USD to coins
router.post('/earnings/convert', guideController.convertToCoins);

// Get earnings history
router.get('/earnings/history', guideController.getEarningsHistory);

// ==================== GUIDE LEADERBOARD ====================
// Get monthly leaderboard
router.get('/leaderboard/monthly', guideController.getMonthlyLeaderboard);

// Get weekly leaderboard
router.get('/leaderboard/weekly', guideController.getWeeklyLeaderboard);

export default router;
