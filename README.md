Overview

The MainActivity is a Jetpack Compose screen designed to display match details, including team matches, timings, and player details via a dialog box. The UI incorporates Material 3 design principles with dynamic color theming.

Features

Tabbed Navigation: Users can switch between home and away teams using a TabRow in details screen.

Player Details Dialog: On clicking a player's name, an alert dialog shows their batting and bowling styles.

Dynamic Colors: The UI adapts to Material 3 color schemes.

Scaffold Layout: Includes a top app bar with consistent padding.

Components

1. PostDetailsScreen

The main composable function that:

Displays team names in tabs.

Handles tab selection.

Triggers player detail dialogs.

Parameters:

teamHome: The full name of the home team.

teamAway: The full name of the away team.

teamHomePlayers: JSON-encoded string containing home team player details.

teamAwayPlayers: JSON-encoded string containing away team player details.

2. TabRow

Used for team selection. The selected tab's text color changes dynamically.

3. AlertDialog

Shows the selected player's batting and bowling styles.



Dependencies

Ensure the following dependencies are added to your build.gradle:

dependencies {
    implementation "androidx.compose.material3:material3:1.1.0"
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0"
}

Customization

Colors: Customize using Material 3's colorScheme.

Future Enhancements

Add player images.

Include more detailed player stats.

Implement animations for tab switching.
