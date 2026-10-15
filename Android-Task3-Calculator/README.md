# Calculator App — Task 3

Android calculator built with Java + XML using MVVM architecture.

## Features
### Required
- Display for current input and result
- Digits 0–9, decimal point
- Operators: + − × ÷
- Equals, Clear (C), Backspace (⌫)
- Division-by-zero → "Error" (no crash)
- GridLayout button grid
- No crashes on rapid taps

### Extras
- Scientific mode: sin, cos, tan, log, √, xʸ, parentheses
- Proper operator precedence (5 + 3 × 2 = 11, not 16)
- Right-associative power (2^3^2 = 512)
- Calculation history (RecyclerView dialog)
- Dark / Light theme with persistence
- Haptic feedback on every tap
- Long-press display → copy result
- Custom recursive-descent parser (no eval())
- 26 unit tests for the expression evaluator

## Tech Stack
- Java 17
- XML layouts + Material 3
- MVVM (ViewModel + LiveData + Repository)
- ViewBinding
- JUnit 4

## Architecture
- `model/` — data holders
- `data/` — CalculatorRepository
- `ui/` — CalculatorViewModel, HistoryAdapter
- `util/` — ExpressionEvaluator (recursive-descent parser)

## Testing
26 JUnit tests covering precedence, parentheses, right-assoc power,
unary minus, division-by-zero, malformed input, Unicode symbol normalization.

## How to Run
1. Open in Android Studio
2. Sync Gradle
3. Run on API 24+ device/emulator

<img width="702" height="1600" alt="3" src="https://github.com/user-attachments/assets/b3996c4d-ab80-4ee6-b53b-4a3ee166e05c" />
<img width="702" height="1600" alt="4" src="https://github.com/user-attachments/assets/9b49e465-469f-4c4b-b164-c64653aae426" />

