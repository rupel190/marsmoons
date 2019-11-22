import io.kotlintest.be
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.should
import io.kotlintest.specs.FreeSpec
import org.junit.Assert
import org.junit.Test

class OverlapTest {

    //region overlaps
    //SD = same day - interval doesn't cross the day-boundary
    //ND = next day or intra day - interval crosses the day-boundary
    fun helpCalcOverlap(
        riseDHours: Int, riseDMinutes: Int,
        setDHours: Int, setDMinutes: Int,
        risePHours: Int, risePMinutes: Int,
        setPHours: Int, setPMinutes: Int): MarsTime {
        val moonRiseDeimos = MarsTime(riseDHours, riseDMinutes)
        val moonSetDeimos = MarsTime(setDHours, setDMinutes)
        val moonRisePhobos = MarsTime(risePHours, risePMinutes)
        val moonSetPhobos = MarsTime(setPHours, setPMinutes)
        val deimos = Moon("Daemos", MoonInterval(moonRiseDeimos, moonSetDeimos))
        val phobos = Moon("Phobos", MoonInterval(moonRisePhobos, moonSetPhobos))
        return calculateOverlap(deimos, phobos)
    }

    @Test
    fun `SD-SD simple overlap`() {
        val overlap = helpCalcOverlap(
            5, 0,
            15, 0,
            11, 0,
            20, 0
        )
        Assert.assertEquals(MarsTime(4, 0), overlap)
    }

    @Test
    fun `SD-ND simple overlap`() {
        val overlap = helpCalcOverlap(
            0, 0,
            7, 0,
            15, 0,
            3, 0
        )
        Assert.assertEquals(MarsTime(3, 0), overlap)
    }

    @Test
    fun `ND-SD simple overlap`() {
        val overlap = helpCalcOverlap(
            20, 0,
            5, 0,
            22, 0,
            4, 0
        )
        Assert.assertEquals(MarsTime(7, 0), overlap)
    }

    @Test
    fun `ND-ND simple overlap`() {
        val overlap = helpCalcOverlap(
            20, 0,
            10, 0,
            15, 0,
            0, 0
        )
        Assert.assertEquals(MarsTime(5, 0), overlap)
    }

    @Test
    fun `ND ND identical intervals`() {
        val overlap = helpCalcOverlap(
            17, 0,
            7, 0,
            17, 0,
            7, 0
        )
        Assert.assertEquals(MarsTime(15, 0), overlap)
    }

    @Test
    fun `ND-ND one interval inclosed by other`() {
        val overlap = helpCalcOverlap(
            17, 0,
            14, 0,
            20, 0,
            12, 30
        )
        Assert.assertEquals(MarsTime(17, 30), overlap)
    }

    @Test
    fun `SD-SD no overlap at all`() {
        val overlap = helpCalcOverlap(
            12, 0,
            20, 0,
            0, 0,
            12, 0
        )
        Assert.assertEquals(MarsTime(0, 0), overlap)
    }

    @Test
    fun `ND-ND full or no overlap - undefined`() {
        val overlap = helpCalcOverlap(
            13, 0,
            13, 0,
            0, 0,
            0, 0
        )
        Assert.assertEquals(MarsTime(25, 0), overlap)
    }


}
