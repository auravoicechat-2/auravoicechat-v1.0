/**
 * Settings Controller - App Settings and Configuration
 */

import { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { logger } from '../utils/logger';

const prisma = new PrismaClient();

/**
 * Submit user feedback
 */
export const submitFeedback = async (req: Request, res: Response) => {
  try {
    const { type, description, screenshots } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    // Create a support ticket for feedback
    const ticket = await prisma.supportTicket.create({
      data: {
        userId,
        category: type === 'bug' ? 'technical' : 'general',
        subject: `Feedback: ${type}`,
        description,
        priority: 'medium',
        status: 'open'
      }
    });

    // Add initial message with screenshots
    if (screenshots && screenshots.length > 0) {
      await prisma.supportMessage.create({
        data: {
          ticketId: ticket.id,
          senderId: userId,
          message: 'Screenshots attached',
          attachments: screenshots,
          isStaffReply: false
        }
      });
    }

    logger.info(`Feedback submitted by user ${userId}: ${type}`);

    res.status(201).json({
      success: true,
      data: ticket,
      message: 'Feedback submitted successfully'
    });
  } catch (error) {
    logger.error('Error submitting feedback:', error);
    res.status(500).json({ success: false, error: 'Failed to submit feedback' });
  }
};

/**
 * Get current app version and check for updates
 */
export const checkAppVersion = async (req: Request, res: Response) => {
  try {
    const { currentVersion, platform } = req.query;

    // Get latest version from system settings
    const latestVersion = await prisma.systemSetting.findUnique({
      where: { key: `app_version_${platform}` }
    });

    if (!latestVersion) {
      return res.json({
        success: true,
        data: {
          currentVersion,
          latestVersion: currentVersion,
          updateAvailable: false
        }
      });
    }

    const latest = latestVersion.value as any;
    const updateAvailable = latest.version !== currentVersion;
    const forceUpdate = latest.forceUpdate || false;

    res.json({
      success: true,
      data: {
        currentVersion,
        latestVersion: latest.version,
        updateAvailable,
        forceUpdate,
        releaseNotes: latest.releaseNotes || '',
        downloadUrl: latest.downloadUrl
      }
    });
  } catch (error) {
    logger.error('Error checking app version:', error);
    res.status(500).json({ success: false, error: 'Failed to check version' });
  }
};

/**
 * Get cache size (mock implementation)
 */
export const getCacheSize = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    // Mock data - in real implementation, this would query actual cache
    res.json({
      success: true,
      data: {
        images: 45 * 1024 * 1024, // 45 MB
        videos: 120 * 1024 * 1024, // 120 MB
        audio: 15 * 1024 * 1024, // 15 MB
        other: 8 * 1024 * 1024, // 8 MB
        total: 188 * 1024 * 1024 // 188 MB
      }
    });
  } catch (error) {
    logger.error('Error getting cache size:', error);
    res.status(500).json({ success: false, error: 'Failed to get cache size' });
  }
};

/**
 * Clear cache (client-side operation, just log it)
 */
export const clearCache = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    logger.info(`Cache cleared by user ${userId}`);

    res.json({
      success: true,
      message: 'Cache cleared successfully'
    });
  } catch (error) {
    logger.error('Error clearing cache:', error);
    res.status(500).json({ success: false, error: 'Failed to clear cache' });
  }
};

/**
 * Get system configuration
 */
export const getSystemConfig = async (req: Request, res: Response) => {
  try {
    const config = await prisma.systemSetting.findMany({
      where: {
        key: {
          in: ['maintenance_mode', 'min_app_version', 'features_enabled']
        }
      }
    });

    const configObj: any = {};
    config.forEach(item => {
      configObj[item.key] = item.value;
    });

    res.json({
      success: true,
      data: configObj
    });
  } catch (error) {
    logger.error('Error getting system config:', error);
    res.status(500).json({ success: false, error: 'Failed to get config' });
  }
};
