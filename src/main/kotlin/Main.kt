fun main(args: Array<String>) {
    print("Please read\n----------------------------------------------------------- and write\n\n\n")

    val daemosMoonrise = readMarsTime("Daemos moonrise 'hh:mm':\t")
    val daemosMoonset = readMarsTime("Daemos moonset 'hh:mm':\t")
    val phobosMoonrise = readMarsTime("Phobos moonrise 'hh:mm':\t")
    val phobosMoonset = readMarsTime("Phobos moonset 'hh:mm':\t")

    val daemosInterval = MoonInterval(daemosMoonrise, daemosMoonset)
    val phobosInterval = MoonInterval(phobosMoonrise, phobosMoonset)

    val daemos = Moon("Daemos", daemosInterval)
    val phobos = Moon("Phobos", phobosInterval)

    calculateOverlap(daemos, phobos)
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

/**
 * Calculates overlap between 2 moons
 */
fun calculateOverlap(daemos: Moon, phobos: Moon): MarsTime {
    val smallerRise = if (daemos.interval.rise < phobos.interval.rise) daemos.interval.rise else phobos.interval.rise
    val smallerSet = if (daemos.interval.set < phobos.interval.set) daemos.interval.set else phobos.interval.set
    val greaterRise = if (daemos.interval.rise > phobos.interval.rise) daemos.interval.rise else phobos.interval.rise

    val phobosIntra = phobos.interval.onSkyOverIntraDayLimit()
    val daemosIntra = daemos.interval.onSkyOverIntraDayLimit()

    //Stand-alone rules
    if (fullOrNoneAtAllOverlap(daemos, phobos)) return MarsTime(25, 0)
    if (noOverlap(daemosIntra, phobosIntra, greaterRise, smallerSet)) return MarsTime(0, 0)

    //Overrulings
    var result: MarsTime
    result = defaultOverlap(daemosIntra, phobosIntra, greaterRise, smallerSet, smallerRise)
    result = doubleTwilightRule(result, daemos, phobos)
    result = twilightRule(result)

    return result
}

private fun defaultOverlap(daemosIntra: Boolean, phobosIntra: Boolean, greaterRise: MarsTime, smallerSet: MarsTime, smallerRise: MarsTime): MarsTime {
    return if (!daemosIntra && !phobosIntra) {
        greaterRise - smallerSet
    } else if (daemosIntra && phobosIntra) {
        smallerSet.hours += 25
        greaterRise - smallerSet
    } else {
        smallerRise - smallerSet
    }
}

/**
 * Twilight Rule, intervals touching each other once
 */
private fun twilightRule(result: MarsTime): MarsTime {
    var result1 = result
    if (result1 == MarsTime(0, 0)) {
        result1 = MarsTime(0, 1)
    }
    return result1
}

/**
 * Double twilight rule, intervals touching each other twice
 */
private fun doubleTwilightRule(result: MarsTime, daemos: Moon, phobos: Moon): MarsTime {
    var result1 = result
    if (result1 == MarsTime(0, 0)
        && daemos.interval.rise == phobos.interval.set
        && phobos.interval.rise == daemos.interval.set
    ) {
        result1 = MarsTime(0, 2)
    }
    return result1
}

private fun noOverlap(daemosIntra: Boolean, phobosIntra: Boolean, greaterRise: MarsTime, smallerSet: MarsTime): Boolean {
    if ((!daemosIntra && !phobosIntra) && (greaterRise > smallerSet)) {
        return true
    }
    return false
}

private fun fullOrNoneAtAllOverlap(daemos: Moon, phobos: Moon): Boolean {
    if (daemos.interval.rise == daemos.interval.set && phobos.interval.rise == phobos.interval.set)
        return true
    return false
}
