fun main(args: Array<String>) {
    print("Please read\n----------------------------------------------------------- and write\n\n\n")

    val daemosMoonrise = readMarsTime("Daemos moonrise 'hh:mm':\t")
    val daemosMoonset = readMarsTime("Daemos moonset 'hh:mm':\t")
    val phobosMoonrise = readMarsTime("Phobos moonrise 'hh:mm':\t")
    val phobosMoonset = readMarsTime("Phobos moonset 'hh:mm':\t")

    val daemosInterval = MoonInterval(daemosMoonrise, daemosMoonset)
    val phobosInterval = MoonInterval(phobosMoonrise, phobosMoonset)

    //debug statements
    println("daemos rise: " + daemosInterval.rise)
    println("daemos set: " + daemosInterval.set)
    println("daemos intraday: " + daemosInterval.onSkyOverIntraDayLimit())

    println("phobos rise: " + phobosInterval.rise)
    println("phobos set: " + phobosInterval.set)
    println("phobos intraday: " + phobosInterval.onSkyOverIntraDayLimit())

    val daemos = Moon("Daemos", daemosInterval)
    val phobos = Moon("Phobos", phobosInterval)

    calculateOverlap(daemos, phobos)
}

fun calculateOverlap(daemos: Moon, phobos: Moon): MarsTime {
    val smallerRise = if(daemos.interval.rise < phobos.interval.rise) daemos.interval.rise else phobos.interval.rise
    val smallerSet = if(daemos.interval.set < phobos.interval.set) daemos.interval.set else phobos.interval.set
    val greaterRise = if(daemos.interval.rise > phobos.interval.rise) daemos.interval.rise else phobos.interval.rise
    val greaterSet = if(daemos.interval.set > phobos.interval.set) daemos.interval.set else phobos.interval.set

    val phobosIntra = phobos.interval.onSkyOverIntraDayLimit()
    val daemosIntra = daemos.interval.onSkyOverIntraDayLimit()

    return if (!daemosIntra && !phobosIntra) {
        greaterRise - smallerSet
    } else if (daemosIntra && phobosIntra) {
        smallerSet.hours += 25
        greaterRise-smallerSet
    } else {
        smallerRise - smallerSet
    }
}

private fun readMarsTime(prompt: String): MarsTime {
    var marsTime = MarsTime()
    do {
        print(prompt)
        try {
            val time = readLine()!!.split(':')
            val hours = time[0].toInt()
            val minutes = time[1].toInt()
            marsTime = MarsTime(hours, minutes)
        } catch (ex: Exception) {
            println("Invalid input. Format: [hh:mm] with maximum time of [24:99]")
            continue;
        }
    } while (!marsTime.hoursValid || !marsTime.minutesValid)
    return marsTime
}
