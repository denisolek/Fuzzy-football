package parser

import java.time.LocalDate

data class Match(
    val date: LocalDate,
    val homeTeam: FuzzyTyper.Team,
    val awayTeam: FuzzyTyper.Team,
    val homeGoals: Int,
    val awayGoals: Int,
    val result: String
)

