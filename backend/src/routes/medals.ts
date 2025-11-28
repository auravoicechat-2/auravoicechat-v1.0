/**
 * Medals Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Endpoints:
 * GET /profile/medals - Get user's medals
 * POST /profile/medals/display - Update display order
 * GET /users/:id/medals - View other user's medals
 */

import { Router } from 'express';
import { authenticate, optionalAuth } from '../middleware/auth';
import { validateMedalDisplay } from '../middleware/validation';
import * as medalsController from '../controllers/medalsController';

const router = Router();

// Get user's medals
router.get('/', authenticate, medalsController.getUserMedals);

// Update display order
router.post('/display', authenticate, validateMedalDisplay, medalsController.updateMedalDisplay);

export default router;
