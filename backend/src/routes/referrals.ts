/**
 * Referrals Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Get Coins and Get Cash programs
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { referralLimiter, withdrawalLimiter } from '../middleware/rateLimiter';
import { validateBindReferral, validateWithdrawCash, validatePagination } from '../middleware/validation';
import * as referralsController from '../controllers/referralsController';

const router = Router();

// Bind referral code
router.post('/bind', authenticate, referralLimiter, validateBindReferral, referralsController.bindReferralCode);

// Get Coins - Summary
router.get('/coins/summary', authenticate, referralsController.getCoinsSummary);

// Get Coins - Withdraw
router.post('/coins/withdraw', authenticate, withdrawalLimiter, referralsController.withdrawCoins);

// Get Coins - Records
router.get('/records', authenticate, validatePagination, referralsController.getRecords);

// Get Cash - Summary
router.get('/cash/summary', authenticate, referralsController.getCashSummary);

// Get Cash - Withdraw
router.post('/cash/withdraw', authenticate, withdrawalLimiter, validateWithdrawCash, referralsController.withdrawCash);

// Get Cash - Invite Records
router.get('/cash/invite-record', authenticate, validatePagination, referralsController.getInviteRecords);

// Get Cash - Ranking
router.get('/cash/ranking', authenticate, validatePagination, referralsController.getRanking);

export default router;
