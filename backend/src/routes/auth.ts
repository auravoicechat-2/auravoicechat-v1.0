/**
 * Authentication Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * POST /auth/otp/send - Send OTP
 * POST /auth/otp/verify - Verify OTP
 */

import { Router } from 'express';
import { authLimiter } from '../middleware/rateLimiter';
import { validateSendOtp, validateVerifyOtp } from '../middleware/validation';
import * as authController from '../controllers/authController';

const router = Router();

// Send OTP
router.post('/otp/send', authLimiter, validateSendOtp, authController.sendOtp);

// Verify OTP
router.post('/otp/verify', validateVerifyOtp, authController.verifyOtp);

// Refresh token
router.post('/refresh', authController.refreshToken);

// Logout
router.post('/logout', authController.logout);

export default router;
