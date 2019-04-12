package parser

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Parser {
    fun getMatches(): List<Match> {
        val filePath = "/Users/denisolek/Desktop/PROJECTS/dtsi/data/premier_league_2016.csv"
        val reader = Files.newBufferedReader(Paths.get(filePath))
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT)

        val matches = csvParser.records.map {
            Match(
                date = LocalDate.parse(it.get(1), DateTimeFormatter.ofPattern("dd/MM/yy")),
                homeTeam = TipperKotlin.Team.from(it.get(2))!!,
                awayTeam = TipperKotlin.Team.from(it.get(3))!!,
                homeGoals = it.get(4).toInt(),
                awayGoals = it.get(5).toInt(),
                result = it.get(6)
            )
        }
        return matches
    }
}