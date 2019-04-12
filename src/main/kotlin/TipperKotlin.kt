import net.sourceforge.jFuzzyLogic.FIS
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart
import parser.Match
import parser.Parser

object TipperKotlin {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val TEAM_A = Team.MAN_CITY
        val TEAM_B = Team.CHELSEA

        val matches = Parser.getMatches()
        val ranking = getRanking(matches)
        val goals = getGoals(matches)

        val pointDiff = ranking[TEAM_A]?.minus(ranking[TEAM_B]!!)!!
        val goalDiff = goals[TEAM_A]?.minus(goals[TEAM_B]!!)!!

        fuzzy(pointDiff, goalDiff)
    }

    private fun fuzzy(pointDiff: Int, goalDiff: Int) {
        // Load from 'FCL' file
        val fileName = "fcl/typer.fcl"
        val fis = FIS.load(fileName, true)

        val functionBlock = fis.getFunctionBlock("typer")

        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '$fileName'")
            return
        }

        // Show
        JFuzzyChart.get().chart(functionBlock)

        // Set inputs
        fis.setVariable("pointsDiff", pointDiff.toDouble())
        fis.setVariable("goalsDiff", goalDiff.toDouble())

        // Evaluate
        fis.evaluate()

        // Show output variable's chart
        val result = functionBlock.getVariable("result")
        JFuzzyChart.get().chart(result, result.defuzzifier, true)

        // Print ruleSet
        System.out.println(fis)
    }

    private fun getRanking(matches: List<Match>): Map<Team, Int> {
        val teams = matches.groupBy { it.homeTeam }.keys.toList()
        val rank = teams.associateBy({ it }, { 0 }).toMutableMap()

        matches.forEach {
            when (it.result) {
                "H" -> rank[it.homeTeam] = rank[it.homeTeam]!!.plus(3)
                "A" -> rank[it.awayTeam] = rank[it.awayTeam]!!.plus(3)
                "D" -> {
                    rank[it.homeTeam] = rank[it.awayTeam]!!.plus(1)
                    rank[it.awayTeam] = rank[it.homeTeam]!!.plus(1)
                }
            }
        }
        return rank
    }

    private fun getGoals(matches: List<Match>): Map<Team, Int> {
        val teams = matches.groupBy { it.homeTeam }.keys.toList()
        val rank = teams.associateBy({ it }, { 0 }).toMutableMap()

        matches.forEach {
            rank[it.homeTeam] = rank[it.homeTeam]!!.plus(it.homeGoals)
            rank[it.awayTeam] = rank[it.awayTeam]!!.plus(it.awayGoals)
        }
        return rank
    }

    enum class Team constructor(val teamName: String) {
        BURNLEY("Burnley"),
        CRYSTAL_PALACE("Crystal Palace"),
        EVERTON("Everton"),
        HULL("Hull"),
        MAN_CITY("Man City"),
        MIDDLESBROUGH("Middlesbrough"),
        SOUTHAMPTON("Southampton"),
        ARSENAL("Arsenal"),
        BOURNEMOUTH("Bournemouth"),
        CHELSEA("Chelsea"),
        MAN_UNITED("Man United"),
        LEICESTER("Leicester"),
        STOKE("Stoke"),
        SWANSEA("Swansea"),
        TOTTENHAM("Tottenham"),
        WATFORD("Watford"),
        WEST_BROM("West Brom"),
        SUNDERLAND("Sunderland"),
        WEST_HAM("West Ham"),
        LIVERPOOL("Liverpool");

        companion object {
            fun from(team: String): Team? = Team.values().firstOrNull { it.teamName == team }
        }
    }
}