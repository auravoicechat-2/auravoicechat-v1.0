# Aura Voice Chat — Decisions Locked & Remaining Open Questions (Round 2)

Below: 
1) Consolidated decisions you just provided (with minor clarifications proposed where you left “choose whatever”). 
2) Detailed explanations for items you asked about (jackpot, CP formation). 
3) Remaining open questions grouped by domain WITH short option sets to make answering fast.

Reply by listing number: answer (option letter or value). You can also say “default” to accept the recommended default shown.

---

## 1. Decisions Locked (Your Latest Answers Incorporated)

Branding & Platform
- (1) Gradient: Single Aura gradient theme (we’ll apply top→bottom by default). 
- (2) Dark mode: Same layout/components; just palette inversion (no structural change). 
- (3) Iconography: Entire app uses skeuomorphic/Aura style; coins remain gold; cash uses green.
- (4) Minimum Android: API 28 (Android 9+). Proposed target SDK: API 34 (Android 14) unless you object.
  
Authentication & Onboarding
- (5) OTP provider: Firebase primary; Twilio fallback (global). 
- (6) OTP max attempts: 5 per day. 
- (7) First-time tutorial: Short overlay, skippable, show Daily Reward popup explanation only (for now).

Home & Search
- (8) Top bar: Left Home icon (room creation/join own room), center tabs Mine/Popular, right search icon. Search opens overlay with two tabs: Users | Rooms; lookup by UID; if room ID found, join; if user ID found, view profile.
- (9) Banners: Database/Admin panel + owner panel submission (currently leaning to owner direct publish, choice B). Source assets from DB or drawable; needs a unified CMS (approved later at #65).
- (10) Shortcut tiles: Player Ranking, Room Ranking, CP (fixed). Shows top 3 rooms, top 3 players, top 1 CP pair with crowns/hearts.
- (11) Tabs distinction: Popular (all rooms by participant count) and Video/Music (rooms actively playing video). They are separate tabs (not chips).

Rooms & Stage
- (12) Seat counts: Default 8. Owner can upgrade to 12. Owner at Level ≥20 unlocks 16 seats. Super Mic seat (extra slot beside owner) toggled in settings; available to Level ≥40 OR VIP4+. 
- (13) Seat interaction: Anyone can tap/join an open seat. Owner/Admin can remove, kick/ban (1h / 24h / forever), invite into locked seats. Seat-leave via seat menu. Users can self-mute.
- (14) Admin powers: Kick/ban (durations above), lock/unlock seats, mute/unmute, start/stop video/music, clear chat.
- (15) Announcement length: Max 300 characters (≈30 words). Animated emojis (each with a single sound effect to prevent spam).
- (16) Events triggered via owner panel (list still to finalize; rocket & pinball deferred).
- (17) Gifts sending: Owner panel config for catalog; custom animations for select gifts; multi-send (choose recipients + quantity).
- (18) Video/Music: YouTube only (Spotify deferred).
- (19) Playlist: Host + admins manage. Max items: 15.
- (20) Media controls: Volume sync, latency management, quality options (as previously defined).
  
Gifts & Economy
- (21) Gift catalog: Owner panel + regional variants (region based on user’s registered region).
- (22) Gift prices: 50 coins → 200,000,000 coins (“200M”). Will display each price explicitly (K/M formatting allowed in grid).
- (24) Baggage: Holds event probability gifts, CP rewards, friend/event rewards. Items not convertible; user can send baggage gifts as normal gifts at zero coin cost (recipient receives diamonds value).
- (25) Diamond→Coin exchange: User enters amount; no max limit. Anti-spam protection allowed (you said “yes you can add antispam”).
- (26) Coin transfers: Direct coin or diamond transfers NOT allowed; only via gifting (coins → diamonds).
- (27) Number formatting: UI uses K/M (e.g., 2.5M); transactional records show exact numbers with separators.

VIP
- (24 again clarified) VIP purchase: Real money only; details & screenshots to come.
- (25) VIP visuals: Will supply frames/effects per tier via screenshots (pending).
  
Medals
- (26) Default ordering priority under profile: Achievement > Activity > Gift unless user reorders.
- (27) Hidden medals: Hidden from public display (both under name and in public gallery). Confirmed.

CP
- (28) CP formation: You asked “what is this?” (clarification below; decision pending).
- (29) CP thresholds: You’ll provide screenshots (pending).
- (30) CP dissolution: Deferred (pending full section).

Referral & CMS
- (65) CMS: Build now for banners, gift catalog, medal art, CP rewards.
- (66) Feature flags: Yes, across all areas.
  
Analytics & Admin
- (63) Admin dashboards: YES (list still needs specifics).
- (64) Crash & analytics tools: YES (Firebase Crashlytics + Analytics assumed).

Misc
- (21 CP & Friend rewards auto-claim) Deferred to later detailed definition.
- Super Mic occupation: Only Admins (and they can invite qualified users).

---

## 2. Explanations Requested

A) Jackpot (Question #16 earlier “what is jackpot?”)
A “jackpot” gift mechanic typically: 
- Users send qualifying gifts; each send adds to a prize pool or triggers chance-based drops.
- Either: 
  1. Progressive pool (grows until someone wins), OR 
  2. Per-send probability (slot-style: win immediate bonus gift).
- Visual: Banner encouraging sends; when a user wins, broadcast small toast/banner.
Decisions needed: Pool vs per-send probability, visible odds, maximum pool, reward types (coins, diamonds, exclusive gift), announcement style.

B) CP Formation (“what is this?”)
Formation models:
1. Single Payer: One user pays full CP fee (3M coins). Partner must accept.
2. Split Cost: Each pays 50% (1.5M) → both confirm.
3. Staged Cost: Initiator pays 25% upfront; second pays remaining 75%.
Need: Choose model; set confirmation prompts; dissolution rules (refund or not; cooldown).

---

## 3. Remaining Open Questions (Answer with number + option/value)

Branding & Platform
1. Target SDK: A) 33, B) 34, C) Accept latest always (will update yearly).
2. Non-gold icons (tabs, tools) style specifics: A) Slight gradient skeuomorphic, B) Flat monochrome tinted, C) Outline + subtle emboss.

Authentication & Onboarding
3. Twilio fallback trigger: A) Auto if Firebase fails, B) Manual fallback after 2 failures, C) Region-based (list countries).
4. Tutorial scope expansion later? A) Only Daily Reward (now), B) Add Seat & Gift steps, C) Full multi-tip sequence (5 tips).

Home & Banners
5. Banner workflow finalize: A) Owner direct publish (risk of abuse), B) Admin approval required, C) Owner drafts + auto publish if passes automated checks.
6. Banner rotation interval chosen: You said 5–6s. Pick one exact number.
7. Banner refresh: A) Daily cache purge, B) Real-time fetch each view, C) Cached for session only.

Search
8. Advanced filters now or phase later? A) Phase later (basic UID search), B) Launch with filters (online, VIP).

Rooms & Stage
9. 16-seat toggle mid-session: A) Allowed seamlessly, B) Only between sessions (must restart room).
10. Super Mic visual style: A) Animated ring gradient, B) Static dual-color ring, C) Pulsing aura only on speech.
11. Ban durations default: A) 24h, B) 1h, C) Forever, D) Last chosen remembered.
12. Invite seat popup timeout: A) 15s, B) 30s, C) 60s before invite expires.
13. Announcement edit cooldown (spam prevention): A) None, B) 30s between edits, C) 5 min between edits.

Events
14. Initial owner-triggered events list: Select those to implement first: A) Lucky Bag only, B) Lucky Bag + Rocket, C) Lucky Bag + Seasonal banner.
15. Event trigger cooldown (Lucky Bag): A) None, B) 60s, C) 5 min.

Gifts & Multi-Send
16. Multi-send recipient selection UX: A) “Select All” + checkboxes, B) Drag-to-select row, C) Quick toggle icons.
17. Anti-spam for multi-send: A) 2s cooldown after each send, B) 5s, C) Rate-limit by cost total per minute.
18. Gift animation concurrency cap: A) 5 simultaneous, B) 10, C) Unlimited.

Regional Gifts
19. Region precedence: A) Room owner region drives catalog, B) Viewer region overrides, C) Intersection (show shared + global).

Jackpot (if adopted)
20. Jackpot model: A) Progressive pool, B) Per-send probability, C) Hybrid (small chance + pool).
21. Show odds: A) Yes visible %, B) Hidden, C) Only rarity indicator (e.g., stars).

Video/Music Mode
22. Playlist maximum you chose 15 (confirm yes).
23. Seek & skip permissions: A) Host only, B) Host + admins, C) Anyone seated.
24. Latency strategy: A) Server authoritative timestamp, B) Peer host time sync, C) CDN adaptive only.

Baggage & Rewards
25. Sending baggage gifts zero cost: Confirm recipient always gets full diamond value? A) Yes, B) Yes but daily cap on free baggage sends (set value).
26. Baggage gift log visible to sender? A) Yes (history), B) No.
27. Baggage UI: A) Grid with send buttons, B) List with expandable details, C) Tabs by source (Event/CP/Friend).

Exchange & Transfers
28. Anti-spam exchange debounce interval: A) 2s, B) 5s, C) 10s.
29. Large gift warning threshold (coins per send): A) 1M, B) 10M, C) 50M (user gets confirmation dialog).

VIP
30. Billing model: A) Monthly subscription tiers, B) One-time purchase per tier (stack), C) Monthly + upgrade path (pay difference).
31. VIP expiry notification lead time: A) 3 days, B) 7 days, C) 24h only.
32. VIP seat frame per tier? A) Yes each tier unique, B) Only tier milestones (VIP4, VIP7, VIP10), C) None until we design assets.

Medals
33. Auto-claim activity medal reward toast? A) Yes minimal toast, B) Modal dialog, C) Silent (only inventory update).
34. Reorder mode activation: A) Long press medal row, B) “Edit” button, C) Settings toggle.

CP
35. Formation payment model: A) Single payer full fee (3M), B) Split 50/50, C) Initiator pays full + partner confirmation.
36. CP dissolution cooldown (days): A) 3, B) 7, C) 14.
37. CP dissolution refund: A) None, B) 25% to initiator, C) 50% split.

Referral — Get Coins
38. Records page size default: A) 5, B) 10.
39. Records sort order: A) Bind date desc, B) Total coins desc, C) Recently active desc.
40. Internal admin audit log for withdrawals: A) Yes, B) No.

Referral — Get Cash
41. External payout methods at launch: A) None (wallet only), B) Bank + PayPal, C) Bank + Card + PayPal + Payoneer.
42. KYC threshold: A) $100, B) $500, C) $1000.
43. Permanent frame at which level (if any): A) None, B) Lv.10 only, C) Lv.8 & Lv.10.
44. PST label display: A) Show “(PST)”, B) Show local converted, C) Dual (local + PST).

Messaging & Notifications
45. DM attachments after mutual follow: A) Images only, B) Images + voice (≤60s), C) Images + voice + short video (≤15s).
46. Read receipts default: A) On, B) Off (toggle in Settings).
47. Quiet hours setting: A) Yes (user chooses range), B) No. Channels under quiet hours (if yes): choose: messages/system/referrals/rewards (list).

Settings & Privacy
48. Discoverability default: A) Searchable, B) Non-searchable.
49. Analytics opt-out: A) Yes toggle, B) No (only legal privacy notice).
50. Cache auto-clear: A) Manual only, B) Auto after 30 days, C) Auto after 7 days.

Internationalization
51. Next languages: A) Urdu + Hindi first, B) Urdu + Hindi + Bengali, C) Urdu + Hindi + Arabic (needs RTL).
52. RTL (Arabic) support at launch: A) Yes, B) No (later).
53. Numeric formatting for finance: A) 1,234,567.89, B) Locale-specific fully, C) Always compact where possible (except payouts).

Security & Anti-Abuse
54. Device fingerprint fallback: A) SSAID + salt, B) Hardware build + brand hash, C) Hybrid (SSAID + hardware).
55. Rooted devices: A) Warn only, B) Block external payouts, C) Block app entirely.
56. Referrals rate limits (bind/withdraw/records per minute): A) 10/5/30, B) 5/3/20, C) Custom (provide numbers).

Performance & Edge Cases
57. Offline daily reward claim: A) Require online (current), B) Queue & sync later (risk of duplication).
58. Timeout/backoff defaults: A) 3 retries exponential (1s,2s,4s), B) 5 retries (1s,2s,4s,8s,16s), C) 3 retries + user manual retry.
59. Preload next page (ranking & records): A) Yes at 80% scroll, B) At 90%, C) No preload.

Store & Inventory
60. Pricing ladders confirm: A) Use proposed ranges, B) Adjust (supply your own).
61. Flash sales: A) Yes (discount 10–30%, 24h duration), B) No.
62. Expiry notice lead time: A) 24h, B) 72h, C) None.

Family Feature (Future)
63. Member cap: A) 50, B) 100, C) Custom.
64. Roles permissions (if choose default): A) Leader (all), Admin (invite/kick/announce), Member (chat only) — confirm? If not, provide modifications.
65. Leaving cooldown: A) 24h, B) 72h, C) 7 days.
66. Perks (choose base set): A) Banner slot + 5% coin boost, B) Banner + 5% coin + 5% EXP, C) Custom list.

Create Your Room Wizard
67. Room name max length: A) 20 chars, B) 30 chars, C) Custom.
68. Profanity filter: A) Yes (blocked terms list), B) No.
69. Cover image min resolution: A) 512×512, B) 720×720, C) 1080×720 (16:9).
70. Image moderation: A) AI automated, B) Manual review queue, C) None at launch.
71. Ownership transfer: A) Allowed (with 7-day cooldown), B) Not allowed, C) Allowed immediate (no cooldown).

Accessibility
72. Large font fallback: A) Stack icons vertically if overflow, B) Hide labels keep icons, C) Horizontal scroll in header.
73. Reduce Motion coverage: A) Disable seat pulse + gift particles + banner animation (full), B) Only disable gift particles, C) Custom (list).
74. High-contrast theme: A) Adjust colors only, B) Provide separate theme variant, C) None now.

Analytics & Admin
75. Core metrics finalize: A) DAU, active rooms, gifts value, referrals earnings, VIP sales, payouts, CP formations, B) Add coin flow net + retention, C) Custom list (provide).
76. Fraud alerts triggers: A) Sudden spike gifts/hour, B) High referral withdrawals, C) Both + custom thresholds (specify).
77. Feature flag scope: You said “everywhere” — confirm we include: gift animations, seat upgrades, Super Mic, Lucky Bag, playlist, referrals cash payouts? A) Yes, B) Adjust (list exclusions).

API & CMS
78. Catalog change rollout: A) Feature flag + staged 10% increments, B) Hard deploy immediate, C) Staged region-first.
79. Medal art updates frequency: A) Event-based only, B) Monthly rotation, C) Admin manual whenever.

Jackpot (If you adopt after explanation)
80. Jackpot reward types: A) Coins + special gift, B) Diamonds only, C) Both + frame chance.
81. Jackpot pool cap (if progressive): A) 10M coins, B) 100M coins, C) Unlimited.

CP Thresholds (prep for your screenshots)
Provide later: mutual coin targets for levels 1–10 and reward description table.

---

## 4. Next Steps After Your Reply
- I will integrate all new choices into the consolidated spec, update relevant feature docs, and create initial data schemas for jackpot, Lucky Bag, Super Mic, seat upgrades, and playlist governance.
- I will leave placeholders for CP thresholds, VIP asset visuals, and event expansions until you supply screenshots or tables.

---

## 5. Quick Reminders Awaiting Your Input
- CP formation (choose payment model).
- Jackpot adoption (yes/no + model).
- External payout methods (you partially answered but need explicit selection).
- KYC threshold.
- Playlist permissions confirmed (host + admins) — just confirm seek/skip choice (#23).

---

Respond with: number: answer. Example:
6: 6s
12: B
30: A

If you want defaults for a batch, say “Defaults for 30–40” and I’ll apply recommended values and show them before finalizing.
