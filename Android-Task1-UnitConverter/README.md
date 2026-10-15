# Unit Converter App — Task 1

Android unit converter built with Java + XML using MVVM architecture.

## Features
- 8 categories: Length, Weight, Temperature, Volume, Area, Speed, Time, Data
- Custom numeric keypad (no system keyboard)
- Live conversion as you type
- Swap units (⇅)
- Save conversions to history (persists across app restarts)
- Copy result on long-press
- Dark / Light theme with persistence

## Tech Stack
- Java 17
- XML layouts + Material 3
- MVVM (ViewModel + LiveData + Repository)
- ViewBinding

## Architecture
- `model/` — Unit, Category, ConversionRecord
- `data/` — UnitRegistry, HistoryRepository
- `ui/` — ConverterViewModel, HistoryAdapter
- `util/` — UnitConverter, NumberFormatter

## How to Run
1. Open in Android Studio
2. Sync Gradle
3. Run on API 24+ device/emulator

## Screenshots
<img width="702" height="1600" alt="1" src="https://github.com/user-attachments/assets/cf044340-d8b2-4b87-b56c-160d9b8b85f7" />
<img width="702" height="1600" alt="2" src="https://github.com/user-attachments/assets/c8eeec45-adb8-4c95-8279-03403155bff3" />

