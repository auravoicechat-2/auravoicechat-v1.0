/**
 * Rooms Routes
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Voice/Video room management
 */

import { Router } from 'express';
import { authenticate, optionalAuth } from '../middleware/auth';
import { validateAddToPlaylist } from '../middleware/validation';
import * as roomsController from '../controllers/roomsController';

const router = Router();

// Get popular rooms
router.get('/popular', optionalAuth, roomsController.getPopularRooms);

// Get my rooms
router.get('/mine', authenticate, roomsController.getMyRooms);

// Get room by ID
router.get('/:roomId', optionalAuth, roomsController.getRoom);

// Create room
router.post('/', authenticate, roomsController.createRoom);

// Join room
router.post('/:roomId/join', authenticate, roomsController.joinRoom);

// Leave room
router.post('/:roomId/leave', authenticate, roomsController.leaveRoom);

// Video - Add to playlist
router.post('/:roomId/video/playlist', authenticate, validateAddToPlaylist, roomsController.addToPlaylist);

// Video - Exit
router.post('/:roomId/video/exit', authenticate, roomsController.exitVideo);

export default router;
