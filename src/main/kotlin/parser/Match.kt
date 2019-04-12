package parser

import java.time.LocalDate

data class Match(
    val date: LocalDate,
    val homeTeam: TipperKotlin.Team,
    val awayTeam: TipperKotlin.Team,
    val homeGoals: Int,
    val awayGoals: Int,
    val result: String
)

