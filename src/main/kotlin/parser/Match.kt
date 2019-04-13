package parser

import Team
import java.time.LocalDate

data class Match(
    val date: LocalDate,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeGoals: Int,
    val awayGoals: Int,
    val result: String
)

