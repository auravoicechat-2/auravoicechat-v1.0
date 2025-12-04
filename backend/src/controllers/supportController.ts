/**
 * Support Controller - Tickets and Live Chat
 * Handles customer support system
 */

import { Request, Response } from 'express';
import { PrismaClient } from '@prisma/client';
import { logger } from '../utils/logger';

const prisma = new PrismaClient();

/**
 * Create a new support ticket
 */
export const createTicket = async (req: Request, res: Response) => {
  try {
    const { category, subject, description, priority } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const ticket = await prisma.supportTicket.create({
      data: {
        userId,
        category,
        subject,
        description,
        priority: priority || 'medium',
        status: 'open'
      }
    });

    logger.info(`Support ticket created: ${ticket.id} by user ${userId}`);

    res.status(201).json({
      success: true,
      data: ticket,
      message: 'Ticket created successfully'
    });
  } catch (error) {
    logger.error('Error creating ticket:', error);
    res.status(500).json({ success: false, error: 'Failed to create ticket' });
  }
};

/**
 * Get user's support tickets
 */
export const getMyTickets = async (req: Request, res: Response) => {
  try {
    const userId = req.user?.id;
    const { status } = req.query;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const where: any = { userId };
    if (status) {
      where.status = status;
    }

    const tickets = await prisma.supportTicket.findMany({
      where,
      include: {
        messages: {
          orderBy: { createdAt: 'desc' },
          take: 1
        }
      },
      orderBy: { createdAt: 'desc' }
    });

    res.json({
      success: true,
      data: tickets
    });
  } catch (error) {
    logger.error('Error fetching tickets:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch tickets' });
  }
};

/**
 * Get ticket details with messages
 */
export const getTicketDetails = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const ticket = await prisma.supportTicket.findUnique({
      where: { id },
      include: {
        messages: {
          orderBy: { createdAt: 'asc' }
        }
      }
    });

    if (!ticket) {
      return res.status(404).json({ success: false, error: 'Ticket not found' });
    }

    // Check permission (owner or staff)
    const isStaff = req.user?.role === 'admin' || req.user?.role === 'owner';
    if (ticket.userId !== userId && !isStaff) {
      return res.status(403).json({ success: false, error: 'Access denied' });
    }

    res.json({
      success: true,
      data: ticket
    });
  } catch (error) {
    logger.error('Error fetching ticket details:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch ticket' });
  }
};

/**
 * Reply to a ticket
 */
export const replyToTicket = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const { message, attachments } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const ticket = await prisma.supportTicket.findUnique({
      where: { id }
    });

    if (!ticket) {
      return res.status(404).json({ success: false, error: 'Ticket not found' });
    }

    // Check permission
    const isStaff = req.user?.role === 'admin' || req.user?.role === 'owner';
    if (ticket.userId !== userId && !isStaff) {
      return res.status(403).json({ success: false, error: 'Access denied' });
    }

    const supportMessage = await prisma.supportMessage.create({
      data: {
        ticketId: id,
        senderId: userId,
        message,
        attachments: attachments || [],
        isStaffReply: isStaff
      }
    });

    // Update ticket status
    if (ticket.status === 'waiting' && isStaff) {
      await prisma.supportTicket.update({
        where: { id },
        data: { status: 'in_progress' }
      });
    } else if (ticket.status === 'in_progress' && !isStaff) {
      await prisma.supportTicket.update({
        where: { id },
        data: { status: 'waiting' }
      });
    }

    res.status(201).json({
      success: true,
      data: supportMessage,
      message: 'Reply sent'
    });
  } catch (error) {
    logger.error('Error replying to ticket:', error);
    res.status(500).json({ success: false, error: 'Failed to reply' });
  }
};

/**
 * Update ticket status (staff only)
 */
export const updateTicketStatus = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const { status } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const isStaff = req.user?.role === 'admin' || req.user?.role === 'owner';
    if (!isStaff) {
      return res.status(403).json({ success: false, error: 'Only staff can update status' });
    }

    const ticket = await prisma.supportTicket.update({
      where: { id },
      data: {
        status,
        resolvedAt: status === 'resolved' || status === 'closed' ? new Date() : undefined
      }
    });

    res.json({
      success: true,
      data: ticket,
      message: 'Ticket status updated'
    });
  } catch (error) {
    logger.error('Error updating ticket status:', error);
    res.status(500).json({ success: false, error: 'Failed to update status' });
  }
};

/**
 * Get FAQ list
 */
export const getFAQs = async (req: Request, res: Response) => {
  try {
    const { category, search } = req.query;

    const where: any = { isPublished: true };
    if (category) {
      where.category = category;
    }
    if (search) {
      where.OR = [
        { question: { contains: search as string, mode: 'insensitive' } },
        { answer: { contains: search as string, mode: 'insensitive' } }
      ];
    }

    const faqs = await prisma.fAQ.findMany({
      where,
      orderBy: [
        { category: 'asc' },
        { order: 'asc' }
      ]
    });

    res.json({
      success: true,
      data: faqs
    });
  } catch (error) {
    logger.error('Error fetching FAQs:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch FAQs' });
  }
};

/**
 * Search FAQs
 */
export const searchFAQs = async (req: Request, res: Response) => {
  try {
    const { q } = req.query;

    if (!q) {
      return res.status(400).json({ success: false, error: 'Search query required' });
    }

    const faqs = await prisma.fAQ.findMany({
      where: {
        isPublished: true,
        OR: [
          { question: { contains: q as string, mode: 'insensitive' } },
          { answer: { contains: q as string, mode: 'insensitive' } }
        ]
      },
      orderBy: { viewCount: 'desc' },
      take: 20
    });

    res.json({
      success: true,
      data: faqs
    });
  } catch (error) {
    logger.error('Error searching FAQs:', error);
    res.status(500).json({ success: false, error: 'Failed to search FAQs' });
  }
};

/**
 * Send live chat message
 */
export const sendChatMessage = async (req: Request, res: Response) => {
  try {
    const { ticketId, message, attachments } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const supportMessage = await prisma.supportMessage.create({
      data: {
        ticketId,
        senderId: userId,
        message,
        attachments: attachments || [],
        isStaffReply: req.user?.role === 'admin' || req.user?.role === 'owner'
      }
    });

    // TODO: Emit WebSocket event

    res.status(201).json({
      success: true,
      data: supportMessage
    });
  } catch (error) {
    logger.error('Error sending chat message:', error);
    res.status(500).json({ success: false, error: 'Failed to send message' });
  }
};

/**
 * Get chat messages
 */
export const getChatMessages = async (req: Request, res: Response) => {
  try {
    const { ticketId } = req.query;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const messages = await prisma.supportMessage.findMany({
      where: {
        ticketId: ticketId as string
      },
      orderBy: { createdAt: 'asc' }
    });

    res.json({
      success: true,
      data: messages
    });
  } catch (error) {
    logger.error('Error fetching chat messages:', error);
    res.status(500).json({ success: false, error: 'Failed to fetch messages' });
  }
};

/**
 * Mark message as read
 */
export const markMessageRead = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(401).json({ success: false, error: 'Unauthorized' });
    }

    const message = await prisma.supportMessage.update({
      where: { id },
      data: { isRead: true }
    });

    res.json({
      success: true,
      data: message
    });
  } catch (error) {
    logger.error('Error marking message as read:', error);
    res.status(500).json({ success: false, error: 'Failed to update message' });
  }
};
