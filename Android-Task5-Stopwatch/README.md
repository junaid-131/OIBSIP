# Stopwatch & Timer App — Task 5

Android stopwatch with countdown timer built with Java + XML using MVVM.

## Features
### Required
- Large HH:MM:SS.mmm time display
- Start / Pause / Reset buttons
- Buttons visually change state (Start ↔ Pause, Lap greyed when stopped)
- Timer keeps running when app is backgrounded (SystemClock.elapsedRealtime)
- Lap button with scrollable list

### Extras
- **Dual mode**: Stopwatch + Countdown Timer (chip switch)
- **Presets**: 1, 3, 5, 10, 15 min + Custom dialog
- **Finish alert**: sound + vibration + notification when timer ends
- **Lap stats**: Fastest / Average / Slowest breakdown
- **Share laps**: sends as text via any app
- **Persistent laps** across app restart
- **Keep screen on** while running
- **Pulse animation** on Start button
- **Long-press Reset** = instant reset
- Dark / Light theme with persistence
- Gradient card UI, haptic feedback

## Tech Stack
- Java 17
- XML layouts + Material 3 + ConstraintLayout
- MVVM (AndroidViewModel + LiveData)
- ViewBinding
- NotificationManager, Vibrator, RingtoneManager

## Architecture
- `model/` — Lap
- `ui/` — StopwatchViewModel, LapAdapter
- `util/` — TimeFormatter

## How to Run
1. Open in Android Studio
2. Sync Gradle
3. Run on API 24+ device/emulator
4. Grant notification permission on Android 13+ to receive timer alerts



<img width="702" height="1600" alt="5" src="https://github.com/user-attachments/assets/f2af7110-a786-47e2-b448-4a3f402beda7" />

