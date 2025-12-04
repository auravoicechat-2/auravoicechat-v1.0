/**
 * Owner Panel Routes - Economy Management
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import { requireOwner } from '../middleware/ownerAuth';
import * as ownerController from '../controllers/ownerPanelController';

const router = Router();

// All owner routes require authentication and owner role
router.use(authenticate);
router.use(requireOwner);

// Dashboard
router.get('/dashboard/stats', ownerController.getDashboardStats);

// Economy Management
router.get('/economy/config', ownerController.getEconomyConfig);
router.put('/economy/targets', ownerController.updateEarningTargets);
router.put('/economy/conversions', ownerController.updateConversionRates);
router.put('/economy/limits', ownerController.updateSystemLimits);

// Cashout Management
router.get('/cashouts/pending', ownerController.getPendingCashouts);
router.put('/cashouts/:id/approve', ownerController.approveCashout);
router.put('/cashouts/:id/reject', ownerController.rejectCashout);

export default router;
