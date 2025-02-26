package com.example.composesample

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composesample.network.MatchData
import com.example.composesample.network.Player
import com.example.composesample.network.PostViewModel
import com.example.composesample.network.Teams
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostApp()
        }
    }

}

@Composable
fun PostApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "post_list") {
        composable("post_list") { PostListScreen(navController) }
        composable("post_details/{teamhome}/{teamaway}/{teamHomePlayers}/{teamAwayPlayers}") { backStackEntry ->

            val teamHome = backStackEntry.arguments?.getString("teamhome") ?: "No Title"
            val teamAway = backStackEntry.arguments?.getString("teamaway") ?: "No Title"
            val teamHomePlayers = backStackEntry.arguments?.getString("teamHomePlayers") ?: "No Title"
            val teamAwayPlayers = backStackEntry.arguments?.getString("teamAwayPlayers") ?: "No Title"
            PostDetailsScreen(teamHome, teamAway,teamHomePlayers,teamAwayPlayers)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(navController: NavController, postViewModel: PostViewModel = viewModel()) {
    val posts by postViewModel.posts.collectAsState()
    val isConnected by postViewModel.isConnected.collectAsState()
    LaunchedEffect(Unit) {
        postViewModel.fetchMatches()

    }
    if (!isConnected) {
        Text("No internet connection", color = MaterialTheme.colorScheme.error)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Matches", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )

                )
            },
                    containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()

            ) {
                items(1) { post ->
                    posts?.let { PostItem(
                        posts!!,
                        onClick = {

                            val teamHomePlayers= Uri.encode(Json.encodeToString( posts?.Teams?.get(posts!!.Matchdetail.Team_Home)))
                            val teamAwayplayers= Uri.encode(Json.encodeToString( posts?.Teams?.get(posts!!.Matchdetail.Team_Away)))

                            navController.navigate("post_details/" +
                                    "${posts!!.Teams[posts!!.Matchdetail.Team_Home]?.Name_Short}/" +
                                    posts!!.Teams[posts!!.Matchdetail.Team_Away]?.Name_Short+"/"+
                                    Json.encodeToString(teamHomePlayers)+"/"+
                                    Json.encodeToString(teamAwayplayers)
                            ) }
                    )

                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(post: MatchData,onClick: () -> Unit) {

    // Use the team ID to fetch the full team name from the Teams map
    val teamHome = post.Teams[post.Matchdetail.Team_Home]?.Name_Full ?: "Unknown Team"
    val teamAway= post.Teams[post.Matchdetail.Team_Away]?.Name_Full ?: "Unknown Team"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.Matchdetail.Series.Tour_Name, style = MaterialTheme.typography.titleMedium,color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = teamHome, style = MaterialTheme.typography.titleMedium,color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = teamAway, style = MaterialTheme.typography.titleMedium,color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.Matchdetail.Match.Date+" "+post.Matchdetail.Match.Time+" , "+post.Matchdetail.Venue.Name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen1(
    teamHome: String,
    teamAway: String,
    teamHomePlayers: String,
    teamAwayPlayers: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val teamHomeData = Json.decodeFromString<Teams>(teamHomePlayers.trim('"'))
    val teamAwayData = Json.decodeFromString<Teams>(teamAwayPlayers.trim('"'))
    Scaffold(
        topBar = { TopAppBar(title = { Text("Match Details") },colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            var selectedTab by remember { mutableIntStateOf(0) }

            Column {
                // TabRow to switch between tabs
                TabRow(
                    selectedTabIndex = selectedTab,

                            tabs = {
                        Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.padding(bottom = 20.dp)) {
                            Text(teamHome)
                        }
                        Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.padding(bottom = 20.dp)) {
                            Text(teamAway)
                        }
                    }
                )

                // Content based on selected tab
                when (selectedTab) {
                   // 0 -> Tab1Content(teamHomeData.Players)
                  //  1 -> Tab1Content(teamAwayData.Players)

                    0 -> Tab1Content(teamHomeData.Players) { player ->
                        selectedPlayer = player
                        showDialog = true
                    }
                    1 -> Tab1Content(teamAwayData.Players) { player ->
                        selectedPlayer = player
                        showDialog = true
                    }
                }
            }

        }

    }
    if (showDialog && selectedPlayer != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(selectedPlayer?.Name_Full ?: "Player Details") },
            text = {
                Column {
                    Text("Batting Style: ${selectedPlayer?.Batting?.Style ?: "N/A"}")
                    Text("Bowling Style: ${selectedPlayer?.Bowling?.Style ?: "N/A"}")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    teamHome: String,
    teamAway: String,
    teamHomePlayers: String,
    teamAwayPlayers: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val teamHomeData = Json.decodeFromString<Teams>(teamHomePlayers.trim('"'))
    val teamAwayData = Json.decodeFromString<Teams>(teamAwayPlayers.trim('"'))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match Details", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ) {
            var selectedTab by remember { mutableIntStateOf(0) }

            Column {
                // TabRow with colors
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.padding(bottom = 20.dp, top = 20.dp)

                    ) {
                        Text(
                            teamHome,
                           // color = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.padding(bottom = 20.dp, top = 20.dp)
                    ) {
                        Text(
                            teamAway,
                       //     color = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Tab content
                when (selectedTab) {
                    0 -> Tab1Content(teamHomeData.Players) { player ->
                        selectedPlayer = player
                        showDialog = true
                    }
                    1 -> Tab1Content(teamAwayData.Players) { player ->
                        selectedPlayer = player
                        showDialog = true
                    }
                }
            }
        }
    }

    // Player Details Dialog
    if (showDialog && selectedPlayer != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    selectedPlayer?.Name_Full ?: "Player Details",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column {
                    Text("Batting Style: ${selectedPlayer?.Batting?.Style ?: "N/A"}", color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Bowling Style: ${selectedPlayer?.Bowling?.Style ?: "N/A"}", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Close")
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}


@Composable
fun Tab1Content(playerMap: Map<String, Player>, onPlayerClick: (Player) -> Unit) {
    // List for Tab 1
    LazyColumn {
                   items(playerMap.entries.toList()) { (key, player) ->
                        PlayerItem(key, player,onPlayerClick)
                    }
    }
}@Composable
fun PlayerItem(key: String, player: Player, onPlayerClick: (Player) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth().clickable { onPlayerClick(player)}
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            if (player.Iscaptain == true) {
                Text(text = "${player.Name_Full} (c)", style = MaterialTheme.typography.bodyLarge,color = MaterialTheme.colorScheme.primary)
            }
            else if (player.Iskeeper == true) {
                Text(text = "${player.Name_Full} (wk)", style = MaterialTheme.typography.bodyLarge,color = MaterialTheme.colorScheme.primary)
            }
            else{
                Text(text = "${player.Name_Full}", style = MaterialTheme.typography.bodyLarge,color = MaterialTheme.colorScheme.primary)
            }
            Text(text = "Position: ${player.Position}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Batting: ${player.Batting.Style}, Avg: ${player.Batting.Average}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Bowling: ${player.Bowling.Style}, Wickets: ${player.Bowling.Wickets}",
                style = MaterialTheme.typography.bodySmall
            )


        }
    }
}







