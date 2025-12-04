/**
 * Admin Authentication Middleware
 * Role-based access control for admin features
 */

import { Request, Response, NextFunction } from 'express';
import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

/**
 * Middleware to require any admin role
 */
export const requireAdmin = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ 
        success: false, 
        error: 'Unauthorized' 
      });
    }

    // Check if user has admin privileges
    const admin = await prisma.admin.findUnique({
      where: { userId },
      select: { 
        adminLevel: true, 
        isActive: true,
        canBanUsers: true,
        canKickUsers: true,
        canDeleteMessages: true,
        canManageRooms: true,
        canManageEvents: true,
        canApproveGuides: true
      }
    });

    if (!admin || !admin.isActive) {
      return res.status(403).json({ 
        success: false, 
        error: 'Access denied. Admin privileges required.' 
      });
    }

    // Attach admin info to request
    req.admin = admin;
    next();
  } catch (error) {
    console.error('Admin auth middleware error:', error);
    res.status(500).json({ 
      success: false, 
      error: 'Authentication error' 
    });
  }
};

/**
 * Middleware to require country admin role
 */
export const requireCountryAdmin = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ 
        success: false, 
        error: 'Unauthorized' 
      });
    }

    const admin = await prisma.admin.findUnique({
      where: { userId }
    });

    if (!admin || !admin.isActive) {
      return res.status(403).json({ 
        success: false, 
        error: 'Access denied' 
      });
    }

    if (!['OWNER', 'COUNTRY_ADMIN'].includes(admin.adminLevel)) {
      return res.status(403).json({ 
        success: false, 
        error: 'Country admin privileges required' 
      });
    }

    req.admin = admin;
    next();
  } catch (error) {
    console.error('Country admin auth error:', error);
    res.status(500).json({ 
      success: false, 
      error: 'Authentication error' 
    });
  }
};

/**
 * Check specific permission
 */
export const requirePermission = (permission: keyof typeof prisma.admin.fields) => {
  return async (req: Request, res: Response, next: NextFunction) => {
    try {
      const userId = req.user?.id;

      if (!userId) {
        return res.status(401).json({ 
          success: false, 
          error: 'Unauthorized' 
        });
      }

      const admin = await prisma.admin.findUnique({
        where: { userId }
      });

      if (!admin || !admin.isActive) {
        return res.status(403).json({ 
          success: false, 
          error: 'Access denied' 
        });
      }

      // Owner has all permissions
      if (admin.adminLevel === 'OWNER') {
        req.admin = admin;
        return next();
      }

      // Check specific permission
      if (!(admin as any)[permission]) {
        return res.status(403).json({ 
          success: false, 
          error: `Permission denied: ${String(permission)}` 
        });
      }

      req.admin = admin;
      next();
    } catch (error) {
      console.error('Permission check error:', error);
      res.status(500).json({ 
        success: false, 
        error: 'Authentication error' 
      });
    }
  };
};
