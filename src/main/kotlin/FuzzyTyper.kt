import net.sourceforge.jFuzzyLogic.FIS
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart
import parser.Match
import parser.Parser
import java.math.RoundingMode

object FuzzyTyper {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val teamA = Team.CHELSEA
        val teamB = Team.EVERTON

        val matches2016 = Parser.getMatches("/Users/denisolek/Desktop/PROJECTS/dtsi/data/premier_league_2016.csv")
        val matches2017 = Parser.getMatches("/Users/denisolek/Desktop/PROJECTS/dtsi/data/premier_league_2017.csv")

        val lastNineWonA = getLastNineWon(teamA, matches2016)
        val lastNineWonB = getLastNineWon(teamB, matches2016)
        val homeWonPercentA = getHomeWonPercent(teamA, matches2016)
        val awayWonPercentB = getAwayWonPercent(teamB, matches2016)
        val first2017Winner = getFirst2017Winner(teamA, teamB, matches2017)
        fuzzy(teamA, teamB, first2017Winner, lastNineWonA, lastNineWonB, homeWonPercentA, awayWonPercentB)
    }

    private fun fuzzy(
        teamA: Team,
        teamB: Team,
        first2017Winner: Pair<Team, String>,
        lastNineWonA: Int,
        lastNineWonB: Int,
        homeWonPercentA: Int,
        awayWonPercentB: Int
    ) {
        val fileName = "fcl/fuzzyTyper.fcl"
        val fis = FIS.load(fileName, true)
        val functionBlock = fis.getFunctionBlock("typer")
        if (fis == null) {
            println("Can't load file: '$fileName'")
            return
        }

        // Show
        JFuzzyChart.get().chart(functionBlock)

        // Set inputs
        fis.setVariable("lastNineWonA", lastNineWonA.toDouble())
        fis.setVariable("lastNineWonB", lastNineWonB.toDouble())
        fis.setVariable("homeWonPercentA", homeWonPercentA.toDouble())
        fis.setVariable("awayWonPercentB", awayWonPercentB.toDouble())

        // Evaluate
        fis.evaluate()

        // Show output variable's chart
        val result = functionBlock.getVariable("result")
        JFuzzyChart.get().chart(result, result.defuzzifier, true)


        println("Home team: ${teamA.teamName}")
        println("Away team: ${teamB.teamName}")
        println("-------------------------------------------------")
        println("Based on premiere league stats from 2016:")
        println("${teamA.teamName} won $lastNineWonA/9 last matches.")
        println("${teamB.teamName} won $lastNineWonB/9 last matches.")
        println("${teamA.teamName} won $homeWonPercentA% matches playing home.")
        println("${teamB.teamName} won $awayWonPercentB% matches playing away.")
        println(
            "Chances of ${teamA.teamName} winning their next game against ${teamB.teamName} are ${
            result.latestDefuzzifiedValue.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}%"
        )
        println("-------------------------------------------------")
        println(result)
        println("-------------------------------------------------")
        println("Winner of first 2017 $teamA vs $teamB was:")
        println("${first2017Winner.first} - ${first2017Winner.second}")
    }

    private fun getLastNineWon(team: Team, matches: List<Match>): Int {
        var matchesWon = 0
        val teamMatches = matches
            .filter { it.homeTeam == team || it.awayTeam == team }
            .sortedByDescending { it.date }
            .take(9)

        teamMatches.forEach {
            when {
                it.homeTeam == team && it.result == "H" -> matchesWon += 1
                it.awayTeam == team && it.result == "A" -> matchesWon += 1
            }
        }

        return matchesWon
    }

    private fun getHomeWonPercent(team: Team, matches: List<Match>): Int {
        val homeMatches = matches.filter { it.homeTeam == team }
        val wonMatchesCount = homeMatches.filter { it.result == "H" }.count()
        val winPercent = wonMatchesCount.toDouble() / homeMatches.count().toDouble() * 100
        return winPercent.toInt()
    }

    private fun getAwayWonPercent(team: Team, matches: List<Match>): Int {
        val awayMatches = matches.filter { it.awayTeam == team }
        val wonMatchesCount = awayMatches.filter { it.result == "A" }.count()
        val winPercent = wonMatchesCount.toDouble() / awayMatches.count().toDouble() * 100
        return winPercent.toInt()
    }

    private fun getFirst2017Winner(teamA: Team, teamB: Team, matches: List<Match>): Pair<Team, String> {
        val firstMatch = matches.filter { it.homeTeam == teamA && it.awayTeam == teamB }.sortedBy { it.date }.first()
        val winner = when (firstMatch.result) {
            "H" -> teamA
            "A" -> teamB
            else -> Team.NONE
        }
        return Pair(winner, "${firstMatch.homeGoals}:${firstMatch.awayGoals}")
    }
}