/**
 * Cinema Controller - Together Watch Mode
 * Handles video synchronization and session management
 */

import { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { logger } from '../utils/logger';

const prisma = new PrismaClient();

/**
 * Start cinema session in a room
 */
export const startCinema = async (req: Request, res: Response) => {
  try {
    const { roomId } = req.params;
    const { videoUrl, videoTitle } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    // Check if user is room owner or admin
    const room = await prisma.room.findUnique({
      where: { id: roomId }
    });

    if (!room || room.ownerId !== userId) {
      return res.status(403).json({ success: false, error: 'Only room owner can start cinema' });
    }

    // Check if cinema already active
    const existing = await prisma.cinemaSession.findUnique({
      where: { roomId }
    });

    if (existing && !existing.endedAt) {
      return res.status(400).json({ success: false, error: 'Cinema session already active' });
    }

    // Create new cinema session
    const session = await prisma.cinemaSession.create({
      data: {
        roomId,
        videoUrl,
        videoTitle,
        hostId: userId,
        isPlaying: false,
        viewers: [userId]
      }
    });

    logger.info(`Cinema started in room ${roomId} by user ${userId}`);

    res.json({
      success: true,
      data: session,
      message: 'Cinema session started'
    });
  } catch (error) {
    logger.error('Error starting cinema:', error);
    res.status(500).json({ success: false, error: 'Failed to start cinema' });
  }
};

/**
 * Get active cinema session for a room
 */
export const getCinemaSession = async (req: Request, res: Response) => {
  try {
    const { roomId } = req.params;

    const session = await prisma.cinemaSession.findFirst({
      where: {
        roomId,
        endedAt: null
      }
    });

    if (!session) {
      return res.status(404).json({ success: false, error: 'No active cinema session' });
    }

    res.json({
      success: true,
      data: session
    });
  } catch (error) {
    logger.error('Error fetching cinema session:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch session' });
  }
};

/**
 * Sync playback position across viewers
 */
export const syncCinema = async (req: Request, res: Response) => {
  try {
    const { roomId } = req.params;
    const { currentPosition, isPlaying } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const session = await prisma.cinemaSession.findFirst({
      where: {
        roomId,
        endedAt: null
      }
    });

    if (!session) {
      return res.status(404).json({ success: false, error: 'No active cinema session' });
    }

    // Only host can control playback
    if (session.hostId !== userId) {
      return res.status(403).json({ success: false, error: 'Only host can control playback' });
    }

    // Update session
    const updated = await prisma.cinemaSession.update({
      where: { id: session.id },
      data: {
        currentPosition,
        isPlaying
      }
    });

    // TODO: Emit WebSocket event to all viewers

    res.json({
      success: true,
      data: updated,
      message: 'Cinema synced'
    });
  } catch (error) {
    logger.error('Error syncing cinema:', error);
    res.status(500).json({ success: false, error: 'Failed to sync cinema' });
  }
};

/**
 * Join cinema session as viewer
 */
export const joinCinema = async (req: Request, res: Response) => {
  try {
    const { roomId } = req.params;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const session = await prisma.cinemaSession.findFirst({
      where: {
        roomId,
        endedAt: null
      }
    });

    if (!session) {
      return res.status(404).json({ success: false, error: 'No active cinema session' });
    }

    // Add viewer if not already in list
    if (!session.viewers.includes(userId)) {
      const updated = await prisma.cinemaSession.update({
        where: { id: session.id },
        data: {
          viewers: [...session.viewers, userId]
        }
      });

      return res.json({
        success: true,
        data: updated,
        message: 'Joined cinema'
      });
    }

    res.json({
      success: true,
      data: session,
      message: 'Already in cinema'
    });
  } catch (error) {
    logger.error('Error joining cinema:', error);
    res.status(500).json({ success: false, error: 'Failed to join cinema' });
  }
};

/**
 * Stop cinema session
 */
export const stopCinema = async (req: Request, res: Response) => {
  try {
    const { roomId } = req.params;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const session = await prisma.cinemaSession.findFirst({
      where: {
        roomId,
        endedAt: null
      }
    });

    if (!session) {
      return res.status(404).json({ success: false, error: 'No active cinema session' });
    }

    // Only host can stop
    if (session.hostId !== userId) {
      return res.status(403).json({ success: false, error: 'Only host can stop cinema' });
    }

    const updated = await prisma.cinemaSession.update({
      where: { id: session.id },
      data: {
        endedAt: new Date()
      }
    });

    logger.info(`Cinema stopped in room ${roomId} by user ${userId}`);

    res.json({
      success: true,
      data: updated,
      message: 'Cinema session ended'
    });
  } catch (error) {
    logger.error('Error stopping cinema:', error);
    res.status(500).json({ success: false, error: 'Failed to stop cinema' });
  }
};

/**
 * Get viewers list
 */
export const getViewers = async (req: Request, res: Response) => {
  try {
    const { roomId } = req.params;

    const session = await prisma.cinemaSession.findFirst({
      where: {
        roomId,
        endedAt: null
      }
    });

    if (!session) {
      return res.status(404).json({ success: false, error: 'No active cinema session' });
    }

    // Fetch viewer details
    const viewers = await prisma.user.findMany({
      where: {
        id: { in: session.viewers }
      },
      select: {
        id: true,
        username: true,
        displayName: true,
        avatarUrl: true,
        level: true
      }
    });

    res.json({
      success: true,
      data: viewers
    });
  } catch (error) {
    logger.error('Error fetching viewers:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch viewers' });
  }
};
