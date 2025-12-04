/**
 * Owner Authentication Middleware
 * Ensures only owner can access protected routes
 */

import { Request, Response, NextFunction } from 'express';
import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

// Owner credentials from config
const OWNER_EMAIL = 'Hamziii886@gmail.com';
const OWNER_USER_ID = 'owner_admin_001';

/**
 * Middleware to require owner role
 */
export const requireOwner = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const userId = req.user?.id;
    const userEmail = req.user?.email;

    if (!userId) {
      return res.status(401).json({ 
        success: false, 
        error: 'Unauthorized' 
      });
    }

    // Check if user is owner by email or ID
    const isOwner = userEmail === OWNER_EMAIL || userId === OWNER_USER_ID;

    if (!isOwner) {
      // Double check in database
      const admin = await prisma.admin.findUnique({
        where: { userId },
        select: { adminLevel: true, isActive: true }
      });

      if (!admin || admin.adminLevel !== 'OWNER' || !admin.isActive) {
        return res.status(403).json({ 
          success: false, 
          error: 'Access denied. Owner privileges required.' 
        });
      }
    }

    next();
  } catch (error) {
    console.error('Owner auth middleware error:', error);
    res.status(500).json({ 
      success: false, 
      error: 'Authentication error' 
    });
  }
};

/**
 * Check if user is owner (helper function)
 */
export const isOwner = async (userId: string, userEmail?: string): Promise<boolean> => {
  if (userEmail === OWNER_EMAIL || userId === OWNER_USER_ID) {
    return true;
  }

  const admin = await prisma.admin.findUnique({
    where: { userId },
    select: { adminLevel: true, isActive: true }
  });

  return admin?.adminLevel === 'OWNER' && admin.isActive;
};
