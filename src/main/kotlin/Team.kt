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

    LIVERPOOL("Liverpool"),
    BRIGHTON("Brighton"),
    NEWCASTLE("Newcastle"),
    HUDDERSFIELD("Huddersfield"),
    NONE("none");

    companion object {
        fun from(team: String): Team? = Team.values().firstOrNull { it.teamName == team }
    }
}