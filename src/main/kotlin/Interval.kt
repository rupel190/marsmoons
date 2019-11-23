import kotlin.math.abs

private const val MINUTESINHOUR = 100

/**
 * Mars day is defined as having 25 hours whereas an hour has 100 minutes.
 * @param hours Possible values (not enforced): 00-24
 * @param minutes Possible values (not enforced): 00-99
 */
data class MarsTime(var hours: Int, var minutes: Int) : Comparable<MarsTime> {
    constructor() : this(0, 0)

    val hoursValid: Boolean
        get() = hours in 0..24
    val minutesValid: Boolean
        get() = minutes in 0..99

    /**
     * Substracts two MarsTimes.
     * The substraction result will always be an absolute value.
     */
    operator fun minus(other: MarsTime): MarsTime {
        val result = MarsTime()
        val diffInMinutes = hours * MINUTESINHOUR + minutes - (other.hours * MINUTESINHOUR + other.minutes)
        result.hours = abs(diffInMinutes / MINUTESINHOUR)
        result.minutes = abs(diffInMinutes % MINUTESINHOUR)
        return result
    }

    /**
     * Compares two MarsTimes.
     */
    override operator fun compareTo(other: MarsTime): Int {
        return when {
            hours > other.hours -> 1
            hours < other.hours -> -1
            minutes > other.minutes -> 1
            minutes < other.minutes -> -1
            else -> 0
        }
    }
}

/**
 * @param rise Denotes time when moon rises
 * @param set Denotes time when moon sets
 */
class MoonInterval(var rise: MarsTime, var set: MarsTime) {
    fun onSkyOverIntraDayLimit(): Boolean {
        //TODO: Equal values here mean that a full 25 hours have passed. Purely by definition.
        if (rise < set) {
            return false
        }
        return true
    }
}
