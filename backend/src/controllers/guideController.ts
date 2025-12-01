/**
 * Guide System Controller
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles guide applications, dashboard, daily tracking, and earnings.
 * Guides are female users who complete daily room tasks and earn real money.
 */

import { Request, Response, NextFunction } from 'express';
import { query } from '../config/database.config';
import { AppError } from '../middleware/errorHandler';
import { logger } from '../utils/logger';

// ==================== GUIDE STATUS ====================

// Check if user is a guide
export const getGuideStatus = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;

        const result = await query(
            `SELECT g.*, u.display_name, u.level
             FROM guides g
             JOIN users u ON g.user_id = u.id
             WHERE g.user_id = $1`,
            [userId]
        );

        if (result.rows.length === 0) {
            res.json({
                isGuide: false,
                guide: null
            });
            return;
        }

        res.json({
            isGuide: true,
            guide: result.rows[0]
        });
    } catch (error) {
        next(error);
    }
};

// Check eligibility for guide application
export const checkEligibility = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;

        const userResult = await query(
            `SELECT id, display_name, level, 
                    (SELECT COUNT(*) FROM rooms WHERE owner_id = users.id) as room_count,
                    created_at,
                    (SELECT status FROM kyc_submissions WHERE user_id = users.id ORDER BY submitted_at DESC LIMIT 1) as kyc_status
             FROM users WHERE id = $1`,
            [userId]
        );

        if (userResult.rows.length === 0) {
            throw new AppError('User not found', 404, 'USER_NOT_FOUND');
        }

        const user = userResult.rows[0];
        const accountAgeDays = Math.floor((Date.now() - new Date(user.created_at).getTime()) / (1000 * 60 * 60 * 24));

        const eligibility = {
            level: { required: 20, current: user.level, met: user.level >= 20 },
            accountAge: { required: 30, current: accountAgeDays, met: accountAgeDays >= 30 },
            roomOwnership: { required: 1, current: parseInt(user.room_count), met: parseInt(user.room_count) >= 1 },
            kycVerified: { required: true, current: user.kyc_status === 'approved', met: user.kyc_status === 'approved' }
        };

        const isEligible = Object.values(eligibility).every(e => e.met);

        res.json({
            isEligible,
            eligibility,
            message: isEligible ? 'You are eligible to apply as a Guide!' : 'Some requirements are not met.'
        });
    } catch (error) {
        next(error);
    }
};

// Apply to become a guide
export const applyForGuide = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;
        const { documents } = req.body;

        // Check if already a guide
        const existingGuide = await query(
            'SELECT * FROM guides WHERE user_id = $1',
            [userId]
        );

        if (existingGuide.rows.length > 0) {
            throw new AppError('You are already a guide', 400, 'ALREADY_GUIDE');
        }

        // Check for pending application
        const existingApp = await query(
            "SELECT * FROM guide_applications WHERE user_id = $1 AND status = 'pending'",
            [userId]
        );

        if (existingApp.rows.length > 0) {
            throw new AppError('You already have a pending application', 400, 'APPLICATION_PENDING');
        }

        // Create application
        const result = await query(
            `INSERT INTO guide_applications (user_id, documents, status, created_at)
             VALUES ($1, $2, 'pending', NOW()) RETURNING *`,
            [userId, JSON.stringify(documents)]
        );

        res.json({
            success: true,
            application: result.rows[0],
            message: 'Application submitted! We will review within 3-5 business days.'
        });
    } catch (error) {
        next(error);
    }
};

// ==================== GUIDE DASHBOARD ====================

// Get guide dashboard
export const getDashboard = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;

        // Check if user is a guide
        const guideResult = await query(
            'SELECT * FROM guides WHERE user_id = $1',
            [userId]
        );

        if (guideResult.rows.length === 0) {
            throw new AppError('Not a guide', 403, 'NOT_A_GUIDE');
        }

        const guide = guideResult.rows[0];
        const currentMonth = new Date().toISOString().slice(0, 7); // '2025-12'

        // Get current month targets
        const targetsResult = await query(
            'SELECT * FROM guide_targets WHERE user_id = $1 AND year_month = $2',
            [userId, currentMonth]
        );

        // Get this month's daily stats
        const dailyResult = await query(
            `SELECT 
                COUNT(*) FILTER (WHERE jar_completed = true) as days_completed,
                SUM(room_hours) as total_room_hours,
                SUM(mic_hours) as total_mic_hours,
                SUM(coins_received) as total_coins_received,
                SUM(messages_received) as total_messages,
                SUM(unique_visitors) as total_visitors,
                SUM(games_hosted) as total_games
             FROM guide_daily 
             WHERE user_id = $1 AND date >= date_trunc('month', CURRENT_DATE)`,
            [userId]
        );

        // Get earnings
        const earningsResult = await query(
            'SELECT * FROM guide_earnings WHERE user_id = $1',
            [userId]
        );

        res.json({
            guide,
            currentMonth,
            monthlyTargets: targetsResult.rows[0] || null,
            monthlyStats: dailyResult.rows[0],
            earnings: earningsResult.rows[0] || { pending_usd: 0, available_usd: 0, total_earned: 0 },
            daysRemaining: new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0).getDate() - new Date().getDate()
        });
    } catch (error) {
        next(error);
    }
};

// Get guide targets for a month
export const getTargets = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;
        const { yearMonth } = req.params;

        const result = await query(
            'SELECT * FROM guide_targets WHERE user_id = $1 AND year_month = $2',
            [userId, yearMonth]
        );

        // If no targets exist for this month, create them
        if (result.rows.length === 0) {
            const defaultTargets = [
                { id: 1, name: 'Daily Presence', requirement: 'Complete jar tasks 25/30 days', progress: 0, required: 25, reward: 5.00, status: 'in_progress' },
                { id: 2, name: 'Room Hours', requirement: 'Host room for 100 hours', progress: 0, required: 100, reward: 10.00, status: 'in_progress' },
                { id: 3, name: 'Mic Queen', requirement: 'Use mic for 50 hours', progress: 0, required: 50, reward: 7.50, status: 'in_progress' },
                { id: 4, name: 'Gift Magnet Bronze', requirement: 'Receive 2,000,000 coins in gifts', progress: 0, required: 2000000, reward: 3.00, status: 'in_progress' },
                { id: 5, name: 'Gift Magnet Silver', requirement: 'Receive 5,000,000 coins in gifts', progress: 0, required: 5000000, reward: 7.50, status: 'in_progress' },
                { id: 6, name: 'Gift Magnet Gold', requirement: 'Receive 10,000,000 coins in gifts', progress: 0, required: 10000000, reward: 15.00, status: 'in_progress' },
                { id: 7, name: 'Community Builder', requirement: 'Gain 100 new followers', progress: 0, required: 100, reward: 5.00, status: 'in_progress' },
                { id: 8, name: 'Room Popularity', requirement: 'Get 500 unique room visitors', progress: 0, required: 500, reward: 5.00, status: 'in_progress' },
                { id: 9, name: 'Engagement Pro', requirement: 'Receive 1,000 messages in room', progress: 0, required: 1000, reward: 3.00, status: 'in_progress' },
                { id: 10, name: 'Streak Master', requirement: '14-day consecutive activity streak', progress: 0, required: 14, reward: 3.00, status: 'in_progress' },
                { id: 11, name: 'Game Host', requirement: 'Host 50 games in room', progress: 0, required: 50, reward: 5.00, status: 'in_progress' },
                { id: 12, name: 'VIP Attractor', requirement: 'Have 10 VIP users visit room', progress: 0, required: 10, reward: 5.00, status: 'in_progress' },
                { id: 13, name: 'Peak Hours', requirement: 'Host during peak hours for 40 hours', progress: 0, required: 40, reward: 7.50, status: 'in_progress' },
                { id: 14, name: 'Weekly Excellence', requirement: 'Complete all weekly coin targets', progress: 0, required: 4, reward: 10.00, status: 'in_progress' },
                { id: 15, name: 'Perfect Month', requirement: 'Complete ALL 14 targets', progress: 0, required: 14, reward: 25.00, status: 'in_progress' }
            ];

            const insertResult = await query(
                `INSERT INTO guide_targets (user_id, year_month, targets, status, created_at)
                 VALUES ($1, $2, $3, 'in_progress', NOW()) RETURNING *`,
                [userId, yearMonth, JSON.stringify(defaultTargets)]
            );

            res.json({ targets: insertResult.rows[0] });
            return;
        }

        res.json({ targets: result.rows[0] });
    } catch (error) {
        next(error);
    }
};

// Get daily tracking
export const getDailyTracking = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;
        const { date } = req.params;

        const result = await query(
            'SELECT * FROM guide_daily WHERE user_id = $1 AND date = $2',
            [userId, date]
        );

        if (result.rows.length === 0) {
            res.json({
                daily: null,
                message: 'No data for this date'
            });
            return;
        }

        res.json({ daily: result.rows[0] });
    } catch (error) {
        next(error);
    }
};

// ==================== GUIDE EARNINGS ====================

// Get guide earnings
export const getEarnings = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;

        const result = await query(
            'SELECT * FROM guide_earnings WHERE user_id = $1',
            [userId]
        );

        if (result.rows.length === 0) {
            res.json({
                earnings: {
                    pending_usd: 0,
                    available_usd: 0,
                    total_earned: 0,
                    total_withdrawn: 0,
                    total_converted: 0
                }
            });
            return;
        }

        res.json({ earnings: result.rows[0] });
    } catch (error) {
        next(error);
    }
};

// Request withdrawal
export const requestWithdrawal = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;
        const { amount, method, account_details } = req.body;

        // Get current available balance
        const earningsResult = await query(
            'SELECT available_usd FROM guide_earnings WHERE user_id = $1',
            [userId]
        );

        if (earningsResult.rows.length === 0 || parseFloat(earningsResult.rows[0].available_usd) < amount) {
            throw new AppError('Insufficient balance', 400, 'INSUFFICIENT_BALANCE');
        }

        // Deduct from available
        await query(
            `UPDATE guide_earnings 
             SET available_usd = available_usd - $1, 
                 total_withdrawn = total_withdrawn + $1,
                 updated_at = NOW()
             WHERE user_id = $2`,
            [amount, userId]
        );

        // Create withdrawal request
        const result = await query(
            `INSERT INTO withdrawals (user_id, amount, net_amount, method, account_details, status, created_at)
             VALUES ($1, $2, $2, $3, $4, 'pending', NOW()) RETURNING *`,
            [userId, amount, method, JSON.stringify(account_details)]
        );

        res.json({
            success: true,
            withdrawal: result.rows[0],
            message: 'Withdrawal request submitted. Processing time: 1-3 business days.'
        });
    } catch (error) {
        next(error);
    }
};

// Convert USD to coins
export const convertToCoins = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;
        const { usdAmount } = req.body;

        // Get current available balance
        const earningsResult = await query(
            'SELECT available_usd FROM guide_earnings WHERE user_id = $1',
            [userId]
        );

        if (earningsResult.rows.length === 0 || parseFloat(earningsResult.rows[0].available_usd) < usdAmount) {
            throw new AppError('Insufficient balance', 400, 'INSUFFICIENT_BALANCE');
        }

        // Calculate coins with bonus
        let bonus = 0;
        if (usdAmount >= 50) bonus = 0.20;
        else if (usdAmount >= 25) bonus = 0.18;
        else if (usdAmount >= 10) bonus = 0.15;
        else if (usdAmount >= 5) bonus = 0.12;
        else bonus = 0.10;

        const baseCoins = usdAmount * 100000; // $1 = 100,000 coins
        const bonusCoins = Math.floor(baseCoins * bonus);
        const totalCoins = baseCoins + bonusCoins;

        // Deduct from guide earnings
        await query(
            `UPDATE guide_earnings 
             SET available_usd = available_usd - $1, 
                 total_converted = total_converted + $1,
                 updated_at = NOW()
             WHERE user_id = $2`,
            [usdAmount, userId]
        );

        // Add coins to user
        await query(
            'UPDATE users SET coins = coins + $1 WHERE id = $2',
            [totalCoins, userId]
        );

        res.json({
            success: true,
            usdConverted: usdAmount,
            coinsReceived: totalCoins,
            bonusCoins,
            message: `Converted $${usdAmount} to ${totalCoins.toLocaleString()} coins!`
        });
    } catch (error) {
        next(error);
    }
};

// Get earnings history
export const getEarningsHistory = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const userId = req.user?.id;
        const { page = 1, limit = 20 } = req.query;
        const offset = (Number(page) - 1) * Number(limit);

        const result = await query(
            `SELECT * FROM guide_targets 
             WHERE user_id = $1 
             ORDER BY year_month DESC 
             LIMIT $2 OFFSET $3`,
            [userId, limit, offset]
        );

        const countResult = await query(
            'SELECT COUNT(*) FROM guide_targets WHERE user_id = $1',
            [userId]
        );

        res.json({
            history: result.rows,
            total: parseInt(countResult.rows[0].count),
            page: Number(page),
            totalPages: Math.ceil(parseInt(countResult.rows[0].count) / Number(limit))
        });
    } catch (error) {
        next(error);
    }
};

// ==================== GUIDE LEADERBOARD ====================

// Get monthly leaderboard
export const getMonthlyLeaderboard = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const currentMonth = new Date().toISOString().slice(0, 7);

        const result = await query(
            `SELECT g.user_id, g.level, u.display_name, u.avatar_url,
                    gt.earnings, 
                    (SELECT COUNT(*) FROM jsonb_array_elements(gt.targets) t WHERE (t->>'status')::text = 'completed') as targets_completed
             FROM guides g
             JOIN users u ON g.user_id = u.id
             LEFT JOIN guide_targets gt ON g.user_id = gt.user_id AND gt.year_month = $1
             WHERE g.status = 'active'
             ORDER BY gt.earnings DESC NULLS LAST
             LIMIT 100`,
            [currentMonth]
        );

        res.json({ leaderboard: result.rows });
    } catch (error) {
        next(error);
    }
};

// Get weekly leaderboard
export const getWeeklyLeaderboard = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
        const startOfWeek = new Date();
        startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay());
        startOfWeek.setHours(0, 0, 0, 0);

        const result = await query(
            `SELECT g.user_id, g.level, u.display_name, u.avatar_url,
                    SUM(gd.coins_received) as weekly_coins,
                    SUM(gd.room_hours) as weekly_hours,
                    COUNT(*) FILTER (WHERE gd.jar_completed = true) as days_completed
             FROM guides g
             JOIN users u ON g.user_id = u.id
             LEFT JOIN guide_daily gd ON g.user_id = gd.user_id AND gd.date >= $1
             WHERE g.status = 'active'
             GROUP BY g.user_id, g.level, u.display_name, u.avatar_url
             ORDER BY weekly_coins DESC NULLS LAST
             LIMIT 100`,
            [startOfWeek.toISOString().split('T')[0]]
        );

        res.json({ leaderboard: result.rows });
    } catch (error) {
        next(error);
    }
};
