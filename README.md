# SimpliCalc

SimpliCalc is a polished Android calculator app built with Jetpack Compose.

## Features

- Modern calculator UI with responsive layout
- Persistent calculation log with search and date grouping
- Copy full calculation to clipboard
- Privacy, About, and Contact screens
- Split-screen and floating-window friendly layout adjustments

## Tech Stack

- Kotlin
- Jetpack Compose
- Gradle Kotlin DSL
- SharedPreferences for local history storage

## Run Locally

1. Open the project in Android Studio
2. Let Gradle sync complete
3. Start an emulator or connect an Android device
4. Run the `app` configuration

You can also build from the terminal:

```powershell
.\gradlew.bat assembleDebug
```

## Package

- Application ID: `com.example.simplecalculator`

## Notes

- History is stored locally on-device
- The app does not require sign-in or network access for core calculator functionality

## Documentation

- Privacy Policy: [docs/privacy-policy.md](docs/privacy-policy.md)
