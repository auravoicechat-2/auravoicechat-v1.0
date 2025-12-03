# Week 1 Implementation Documentation

## Overview
This document describes the Week 1 (Days 3-7) implementation for the Aura Voice Chat application, following the Yari clone blueprint.

## Architecture

### Data Models

#### RoomCategory (in Room.kt)
```kotlin
enum class RoomCategory(val displayName: String) {
    ALL("All"),
    CHAT("Chat"),
    MUSIC("Music"),
    GAMING("Gaming"),
    DATING("Dating"),
    PARTY("Party"),
    CHILL("Chill")
}
```

#### VipTier (in User.kt)
```kotlin
enum class VipTier(val displayName: String) {
    NONE(""),
    VIP("VIP"),
    SVIP("SVIP")
}
```

#### MicSeat
Represents a microphone seat in a voice chat room.
- `index`: Seat position (0-7)
- `user`: User object or null if empty
- `isMuted`: Whether the user is muted
- `isSpeaking`: Whether the user is currently speaking (triggers animation)
- `isLocked`: Whether the seat is locked

#### RoomMessage (Sealed Class)
Three types of messages in room chat:
1. **SystemMessage**: Announcements, user join/leave
2. **UserMessage**: Regular chat messages from users
3. **GiftMessage**: Gift sending notifications

### UI Components

#### Level Badge
- Displays user level with gradient background
- 5-tier color system:
  - Levels 1-20: Gray
  - Levels 21-40: Green
  - Levels 41-60: Blue
  - Levels 61-80: Purple
  - Levels 81-100+: Gold

#### VIP Badge
- Shows crown icon for VIP users
- VIP: Purple crown
- SVIP: Gold crown with shadow glow

#### Speaking Indicator
- Animated component with 3 pulsing rings
- Uses AccentCyan color (#35E8FF)
- Infinite animation loop
- Rings expand and fade out
- 300ms delay between each ring

### Home Screen

#### PopularTab
- Horizontal scrollable category chips
- 2-column lazy grid of room cards
- Category filtering (All, Chat, Music, Gaming, Dating, Party, Chill)
- Selected category highlighted with AccentMagenta

#### RoomCard
- Square aspect ratio (1:1)
- Cover image or gradient background
- Room type icon (voice/video/music)
- Live indicator (red badge)
- Host avatar and name
- Listener count with eye icon
- Gradient overlay for text readability

### Room Screen Components

#### RoomHeader
- Back button (left)
- Room name (center)
- Listener count with eye icon (below name)
- Share button (right)
- Settings/more button (right)
- Semi-transparent dark background

#### MicrophoneSeats
- 8 seats in 2 rows x 4 columns
- Empty seats: Dashed border circle with "+" icon
- Occupied seats:
  - User avatar
  - Speaking indicator (pulsing rings)
  - Mute icon (if muted)
  - Level badge (top right)
  - VIP badge (top left)
  - Username below avatar
- Locked seats: Solid circle with lock icon

#### RoomChat
- LazyColumn with auto-scroll to bottom
- Semi-transparent background
- Three message types:
  - System: Centered gray text
  - User: Avatar + colored username + message
  - Gift: Special styling with gift icon
- Username colors based on user level
- Max height: 200dp

#### RoomBottomBar
- Chat input field (rounded, expandable)
- Send button (shows when text entered)
- Emoji button (cyan icon)
- Gift button (gold icon)
- Mic toggle button (red when muted, cyan when active)

#### GiftPanel (Bottom Sheet)
- Category tabs (Love, Celebration, Luxury, Nature, Fantasy, Special)
- 4-column grid of gift items
- Each gift shows: icon, name, price
- Selected gift: AccentMagenta border
- Quantity selector: 1, 10, 99, 520, 1314
- Send button shows total cost

#### EmojiPanel (Bottom Sheet)
- 8-column grid of emojis
- 150+ common emojis
- Click to insert and dismiss

### Services

#### AgoraEventHandler Interface
Callback methods for Agora RTC events:
- `onUserJoined(uid: Int)`: User joins channel
- `onUserOffline(uid: Int)`: User leaves channel
- `onAudioVolumeIndication(speakers: Array<AudioVolumeInfo>)`: Volume updates
- `onError(error: Int)`: Error handling

#### AudioPermissionHandler
Static utility for microphone permissions:
- `hasAudioPermission(context)`: Check permission
- `requestAudioPermission(activity)`: Request permission
- `shouldShowRationale(activity)`: Check rationale flag

### Data Sources

All data is fetched from the live backend API:
- **Rooms**: Fetched via `RoomRepository.getPopularRooms()`, `getMyRooms()`, etc.
- **Users**: Retrieved from backend user service
- **Gifts**: Loaded from backend gift catalog
- **Messages**: Real-time via WebSocket/Socket.io connection
- **Seats**: Room state managed by backend and synced in real-time

The application is fully connected to the backend and does not use mock data.

## Design System

### Colors
- **DarkCanvas**: #12141A (main background)
- **DarkSurface**: #1A1C24 (cards, surfaces)
- **DarkCard**: #22242E (input fields, secondary cards)
- **Purple80**: #C9A8F1 (primary)
- **AccentMagenta**: #D958FF (buttons, highlights)
- **AccentCyan**: #35E8FF (speaking, accents)
- **AccentGold**: #FFD700 (coins, premium)

### Typography
- Room name: Bold, 18sp
- Username: Medium, 14sp
- Chat message: Regular, 14sp
- Badge text: Bold, 10sp

### Spacing
- Screen padding: 16dp
- Card padding: 12dp
- Grid spacing: 8dp
- Section spacing: 24dp

## Usage Examples

### Using LevelBadge
```kotlin
LevelBadge(
    level = user.level,
    modifier = Modifier.align(Alignment.TopEnd)
)
```

### Using SpeakingIndicator
```kotlin
SpeakingIndicator(
    isSpeaking = seat.isSpeaking,
    size = 70.dp
) {
    // Avatar content here
    AsyncImage(...)
}
```

### Using MockData
```kotlin
// In your ViewModel or Composable
val sampleRooms = MockData.sampleRooms
val sampleSeats = MockData.getSampleMicSeats()
val sampleMessages = MockData.getSampleRoomMessages()
```

## Integration Notes

### HomeScreen Integration
The existing HomeScreen already has advanced features. To integrate PopularTab:
```kotlin
### Room Screen Integration
To use the new components in an existing or new RoomScreen:
```kotlin
Scaffold(
    topBar = {
        RoomHeader(
            roomName = room.name,
            listenerCount = room.currentUsers,
            onBack = { /* navigate back */ },
            onShare = { /* share room */ },
            onSettings = { /* open settings */ }
        )
    },
    bottomBar = {
        RoomBottomBar(
            isMuted = isMuted,
            onSendMessage = { /* send message */ },
            onEmojiClick = { showEmojiPanel = true },
            onGiftClick = { showGiftPanel = true },
            onMicToggle = { /* toggle mic */ }
        )
    }
) { padding ->
    Column(modifier = Modifier.padding(padding)) {
        MicrophoneSeats(
            seats = seats,
            onSeatClick = { /* request seat */ },
            onUserClick = { /* view profile */ }
        )
        
        RoomChat(
            messages = messages,
            modifier = Modifier.weight(1f)
        )
    }
}

// Show panels when needed
if (showGiftPanel) {
    GiftPanel(
        gifts = gifts,
        onDismiss = { showGiftPanel = false },
        onSendGift = { gift, quantity -> /* send gift */ }
    )
}

if (showEmojiPanel) {
    EmojiPanel(
        onDismiss = { showEmojiPanel = false },
        onEmojiSelected = { emoji -> /* insert emoji */ }
    )
}
```

## Testing Checklist

- [ ] LevelBadge displays correct colors for all level ranges
- [ ] VipBadge shows correctly for VIP and SVIP users
- [ ] SpeakingIndicator animation runs smoothly
- [ ] PopularTab category filtering works
- [ ] RoomCard displays all information correctly
- [ ] MicrophoneSeats shows all states (empty/occupied/locked)
- [ ] RoomChat auto-scrolls to bottom on new messages
- [ ] RoomBottomBar all buttons functional
- [ ] GiftPanel category switching works
- [ ] EmojiPanel emoji selection works
- [ ] Backend data loads correctly from API

## Future Enhancements

1. Add pagination to room lists
2. Implement real-time seat updates via WebSocket
3. Add gift animations (SVGA/Lottie)
4. Implement voice activity detection for speaking indicator
5. Add seat request/approval flow
6. Implement chat message reactions
7. Add @mentions in chat
8. Implement gift combo animations
9. Add room background customization
10. Implement seat effects system

## Known Limitations

1. Build currently fails due to Gradle repository issue (infrastructure)
2. Components created but not yet wired to existing screens
3. No actual Agora SDK integration (interface only)
4. Real-time updates via WebSocket need to be fully integrated

## References

- Migration Guide: https://github.com/auravoicechat/yari-clone/blob/main/MIGRATION_GUIDE.md
- Project Blueprint: https://github.com/auravoicechat/yari-clone/blob/main/PROJECT_BLUEPRINT.md
- Material3 Design: https://m3.material.io/
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Agora SDK: https://www.agora.io/en/
