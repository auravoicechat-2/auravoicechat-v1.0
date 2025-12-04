/**
 * Cinema Routes
 */

import { Router } from 'express';
import { authenticate } from '../middleware/auth';
import * as cinemaController from '../controllers/cinemaController';

const router = Router();

// All cinema routes require authentication
router.use(authenticate);

// Cinema session management
router.post('/rooms/:roomId/start', cinemaController.startCinema);
router.get('/rooms/:roomId/session', cinemaController.getCinemaSession);
router.post('/rooms/:roomId/sync', cinemaController.syncCinema);
router.post('/rooms/:roomId/join', cinemaController.joinCinema);
router.post('/rooms/:roomId/stop', cinemaController.stopCinema);
router.get('/rooms/:roomId/viewers', cinemaController.getViewers);

export default router;
