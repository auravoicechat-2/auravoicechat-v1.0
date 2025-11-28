/**
 * Users Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 */

import { Router } from 'express';
import { authenticate, optionalAuth } from '../middleware/auth';
import * as usersController from '../controllers/usersController';
import * as medalsController from '../controllers/medalsController';

const router = Router();

// Get user profile
router.get('/:userId', optionalAuth, usersController.getUser);

// Update profile
router.put('/me', authenticate, usersController.updateProfile);

// Get user's medals
router.get('/:userId/medals', optionalAuth, medalsController.getOtherUserMedals);

// Follow user
router.post('/:userId/follow', authenticate, usersController.followUser);

// Unfollow user
router.delete('/:userId/follow', authenticate, usersController.unfollowUser);

export default router;
