# Pending Decisions & Clarifications for Aura Voice Chat

Answer by listing the number followed by your choice/value. Example:
1: Keep existing gradient
2: 60, 90, 180, 365
(You can skip any you’re not ready for.)

## A. Branding & Platform
1. Final gradient direction (top→bottom OK or change?): (top-bottom / diagonal / other)
2. Dark mode: Provide now or defer? (provide / defer)
3. Minimum Android version (API 28 / API 29 / other)
4. Primary accent palette final (keep purple/white + magenta→cyan or modify?)

## B. Authentication (Mobile Login)
5. OTP length (4 / 5 / 6 digits)
6. OTP resend cooldown (seconds)
7. Max OTP attempts before temporary lock (number)
8. Phone number formats allowed (local only / international E.164)
9. Auto-claim Day 1 reward on first ever login? (yes / no)

## C. Daily Login Rewards (Remaining Items)
10. VIP multiplier values per tier (example: VIP 1.2, SVIP 1.3) – list tiers & multipliers
11. Daily reward reminder schedule (keep proposed: 6h after login + 2h before reset / change)
12. Animation allowed for claim (particles) when reduce-motion is OFF? (yes / no)
13. Large number formatting for cycle totals (raw numbers / thousands separators / 50k style)

## D. Medals & Long-Term Progress
14. Final medal milestones (list: e.g., 30, 60, 90, 180, 365)
15. Reward per milestone (describe each: cosmetic frame / coins amount / both)
16. Are medal rewards auto-granted or require manual claim? (auto / manual)

## E. Coin Economy & Store
17. Core store categories (confirm list or add/remove: frames, entry effects, mic skins, seat hearts, consumables)
18. Price range for common items (e.g., 5k–100k coins) – specify min/max per category
19. Rarity tiers naming (e.g., Common / Rare / Epic / Legendary)
20. Limit on consumable stack size (number or unlimited)
21. Refund policy for store items (none / limited 24h / other)

## F. CP (Couple Partnership)
22. CP formation fee (confirm 3M coins or adjust)
23. Full mutual coin thresholds & rewards (list at least first 5–10 levels)
24. Are CP frames/mic skins permanent or time-limited? (permanent / duration days)
25. Do CP hearts upgrade each level or at certain milestones only? (each level / milestone list)
26. Allow CP dissolution and refund? (no / partial / fee)

## G. Video/Music Mode
27. Allowed sources (YouTube only / YouTube + local audio / YouTube + streaming links)
28. Max playlist size (number)
29. Sync method (host-controlled / everyone can add / moderator-only additions)
30. Content moderation (filter NSFW automatically / manual report only)

## H. Room Creation Wizard
31. Max room name length (characters)
32. Max announcement length (characters)
33. Welcome message length (characters)
34. Cover image min resolution (e.g., 512x512)
35. Allowed formats (JPEG/PNG/WebP)

## I. Messaging
36. Initial DM limit (confirm 5 or adjust)
37. Attachments allowed? (none / images only / images+voice)
38. Read receipts enabled? (yes / no)
39. Message retention period (days or indefinite)
40. Report abusive message flow (in-message menu / separate screen)

## J. Wallet & Exchange
41. Minimum diamond amount to exchange (number)
42. Daily exchange limit (coins value or none)
43. Any fee beyond 30% conversion loss? (yes – specify / no)
44. Show preview before confirm exchange? (yes / no)

## K. Referral — Get Coins
45. Max referrals counted per user (unlimited / cap number)
46. Cap on 5% gift earnings per day (no cap / cap value)
47. Coin withdrawal flow exists here? (yes – same panel / no withdrawal, auto-credit)
48. Share message localization required at launch? (yes / no)
49. Pagination page size for referral table (5 / 10 / other)

## L. Referral — Get Cash
50. Minimum withdrawal amount (USD)
51. Withdrawal cooldown (hours)
52. Level rewards auto-claim when target reached? (yes / manual claim)
53. Ranking page size (20 / 50 / other)
54. Campaign end behavior (freeze UI / start new cycle automatically / archive history view)
55. Max total USD a user can earn per campaign (value or unlimited)
56. KYC threshold (USD amount requiring identity verification)

## M. VIP (SVIP)
57. VIP tiers (list names)
58. Benefits per tier (daily reward multiplier, EXP multiplier, extra exchange rate? list specifics)
59. VIP duration (monthly / custom periods)
60. Grace period after expiry (none / days)
61. Stackable with events? (yes / no)

## N. Family Feature
62. Max members per family (number)
63. Roles (leader / admin / member – confirm or adjust)
64. Family creation fee (coins)
65. Key perks (banner slot, shared events, boost % list)
66. Leaving cooldown (hours/days before joining new family)

## O. Privacy & Safety
67. Default online status (visible / hidden)
68. Friend request rules default (everyone / followers only / nobody)
69. Profile discoverability default (searchable / non-searchable)
70. NSFW content handling (auto filter / user report only)
71. Rate limit for login attempts (number per hour)

## P. Notifications
72. Channels required at launch (list: daily reward, messages, system, referral)
73. Daily reward reminder times (keep proposed / custom times)
74. Quiet hours respects user setting? (yes / no)
75. In-room notification style (banner / subtle icon only)

## Q. Accessibility
76. Minimum color contrast target (WCAG AA / AAA / custom)
77. Reduce motion disables particle & glow animations? (yes / no)
78. Font scaling support up to (1.2x / 1.5x / system default only)

## R. Internationalization
79. Launch languages (English only / list)
80. Number formatting style (1,000,000 / 1.000.000 / 1M)
81. Date format (YYYY-MM-DD / locale format / mixed)
82. Currency display (USD only / multi-currency future-ready)

## S. Security & Anti-Abuse
83. Device ID hashing approach (Android ID+salt / custom fingerprint)
84. Treat rooted devices differently? (block / warn / ignore)
85. Max API request rate for referrals endpoints (value per minute)
86. Fraud review triggers (list: rapid invites, high recharge velocity, large withdrawals)
87. Data encryption at rest for wallet balances? (yes / no)

## T. Analytics & Privacy
88. Allow users to opt out of non-essential analytics? (yes / no)
89. Retention of referral & cash transaction logs (days / indefinite)
90. Crash reporting tool (Firebase Crashlytics / other / none)

## U. Rounding & Formatting
91. Large coin values display style (exact 11797449 / formatted 11,797,449 / compact 11.79M)
92. EXP decimals (integer only / allow fractional)
93. Currency decimals for USD (2 always / adaptive removing .00)

## V. Error & Edge Case Policies
94. Offline tolerance for claiming daily reward (allow claim when offline & queue / require online)
95. What happens if claim request times out (retry silently / show error prompt)
96. Duplicate bind code attempts messaging (simple error / escalate after X attempts)

## W. Create Your Room Enhanced
97. Limit number of rooms a single user can create (1 / multiple – specify limit)
98. Editable room announcement after creation? (yes / no)
99. Room deletion allowed? (yes / no – with cooldown)
100. Auto-assign room ID format (numeric / alphanumeric / short code)

## X. Future Expansion Placeholders
101. Will you add transcription (voice-to-text) later? (yes / no / unsure)
102. Room recording consent model (opt-in host / all participants approval / none planned)
103. Ads or monetization placements on Home banners (planned / not planned)

---

You can reply with just the numbers and your choices. If a number is fine with defaults, answer “<number>: default” or skip. I’ll integrate all answers into the final consolidated documentation.
