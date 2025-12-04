/**
 * Earning Targets & Cashout Routes
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import * as earningsController from '../controllers/earningTargetsController';

const router = Router();

// All routes require authentication
router.use(authenticate);

// Earning Targets
router.get('/targets', earningsController.getUserTargets);
router.get('/stats', earningsController.getEarningStats);
router.get('/history', earningsController.getEarningHistory);

// Cashout
router.post('/cashout/request', earningsController.requestCashout);
router.get('/cashout/my', earningsController.getMyCashouts);

// Exchange
router.post('/exchange', earningsController.exchangeDiamondsToCoins);

export default router;
