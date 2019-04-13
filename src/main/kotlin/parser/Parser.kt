package parser

import Team
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Parser {
    fun getMatches(filePath: String): List<Match> {
        val reader = Files.newBufferedReader(Paths.get(filePath))
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT)

        return csvParser.records.map {
            Match(
                date = LocalDate.parse(it.get(1), DateTimeFormatter.ofPattern("dd/MM/yy")),
                homeTeam = Team.from(it.get(2))!!,
                awayTeam = Team.from(it.get(3))!!,
                homeGoals = it.get(4).toInt(),
                awayGoals = it.get(5).toInt(),
                result = it.get(6)
            )
        }
    }
}