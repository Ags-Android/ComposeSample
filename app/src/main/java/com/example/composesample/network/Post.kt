package com.example.composesample.network
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

@Serializable
data class MatchDetail(
    val Team_Home: String,
    val Team_Away: String,
    val Match: Match,
    val Series: Series,
    val Venue: Venue,
    val Officials: Officials,
    val Weather: String,
    val Tosswonby: String,
    val Status: String,
    val Status_Id: String,
    val Player_Match: String,
    val Result: String,
    val Winningteam: String,
    val Winmargin: String,
    val Equation: String
)

@Serializable
data class Match(
    val Livecoverage: String,
    val Id: String,
    val Code: String,
    val League: String,
    val Number: String,
    val Type: String,
    val Date: String,
    val Time: String,
    val Offset: String,
    val Daynight: String
)

@Serializable
data class Series(
    val Id: String,
    val Name: String,
    val Status: String,
    val Tour: String,
    val Tour_Name: String
)

@Serializable
data class Venue(
    val Id: String,
    val Name: String
)

@Serializable
data class Officials(
    val Umpires: String,
    val Referee: String
)




@Serializable
data class Player(
    val Position: String,
    val Name_Full: String,
    val Iscaptain: Boolean? = null,
    val Iskeeper: Boolean? = null,
    val Batting: Batting,
    val Bowling: Bowling
) {

    override fun toString(): String {
        return "Position: $Position, Name: $Name_Full, Captain: $Iscaptain, Keeper: $Iskeeper, Batting: $Batting, Bowling: $Bowling"

    }
}

@Serializable
data class Batting(
    val Style: String,
    val Average: String,
    val Strikerate: String,
    val Runs: String
)

@Serializable
data class Bowling(
    val Style: String,
    val Average: String,
    val Economyrate: String,
    val Wickets: String
)

@Serializable
data class Teams(
    val Name_Full: String,
    val Name_Short: String,
    val Players: Map<String, Player>
)

@Serializable
data class MatchData(
    val Matchdetail: MatchDetail,
    val Teams: Map<String, Teams>
){
    // Define the getTeamName function inside the MatchDetail class
    fun getTeamName(teamId: String): String {
        return Teams[teamId]?.Name_Full ?: "Unknown Team"
    }
}
@Serializer(forClass = MatchData::class)
object PlayerMapSerializer : KSerializer<Map<Int, Player>> {
    private val mapSerializer = MapSerializer(Int.serializer(), Player.serializer())

    override fun serialize(encoder: Encoder, value: Map<Int, Player>) {
        mapSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Map<Int, Player> {
        return mapSerializer.deserialize(decoder)
    }
}