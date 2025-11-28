/**
 * Wallet Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * GET /wallet/balances - Get balances
 * POST /wallet/exchange - Exchange diamonds to coins (30% rate)
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { validateExchange } from '../middleware/validation';
import * as walletController from '../controllers/walletController';

const router = Router();

// Get balances
router.get('/balances', authenticate, walletController.getBalances);

// Exchange diamonds to coins
router.post('/exchange', authenticate, validateExchange, walletController.exchangeDiamondsToCoins);

// Get transaction history
router.get('/transactions', authenticate, walletController.getTransactions);

// Transfer coins
router.post('/transfer', authenticate, walletController.transferCoins);

export default router;
