import io.kotlintest.be
import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec

class HelpersTest : FreeSpec({
    //region intra day limit
    "intra day limit check works for same day based on hours"{
        val moonRise = MarsTime(10, 30)
        val moonSet = MarsTime(20, 15)
        MoonInterval(moonRise, moonSet).onSkyOverIntraDayLimit().shouldBeFalse()
    }
    "intra day limit check works for intra day based on hours" {
        val moonRise = MarsTime(20, 30)
        val moonSet = MarsTime(10, 30)
        MoonInterval(moonRise, moonSet).onSkyOverIntraDayLimit().shouldBeTrue()
    }
    "intra day limit check works for intra day based on minutes" {
        val moonRise = MarsTime(0, 45)
        val moonSet = MarsTime(0, 30)
        //Act
        MoonInterval(moonRise, moonSet).onSkyOverIntraDayLimit().shouldBeTrue()
    }
    "intra day limit check works for equal hours and minutes" {
        val moonRise = MarsTime(15, 30)
        val moonSet = MarsTime(15, 30)
        MoonInterval(moonRise, moonSet).onSkyOverIntraDayLimit().shouldBeFalse()
    }
    "intra day limit check works for start of day!" {
        val moonRise = MarsTime(15, 0)
        val moonSet = MarsTime(0, 0)
        MoonInterval(moonRise, moonSet).onSkyOverIntraDayLimit().shouldBeTrue()
    }
    //endregion

    //region substraction
    "substracting simple marstimes works as expected" {
        val time1 = MarsTime(10, 30)
        val time2 = MarsTime(6, 20)
        time1 - time2 should be(MarsTime(4, 10))
        time2 - time1 should be(MarsTime(4, 10))
    }

    "substracting complex marstimes works as expected" {
        val time1 = MarsTime(17, 27)
        val time2 = MarsTime(27, 189)
        time1 - time2 should be(MarsTime(11, 62))
        time2 - time1 should be(MarsTime(11, 62))
    }
    //endregion

    //region comparison
    "compare marstime1 greater marstime2" {
        val time1 = MarsTime(7, 270)
        val time2 = MarsTime(17, 189)
        (time1>time2) should be(true)
    }
    "compare marstime2 less marstime1" {
        val time1 = MarsTime(17, 27)
        val time2 = MarsTime(17, 99)
        (time1<time2) should be(true)
    }
    "compare marstime1 equals marstime2" {
        val time1 = MarsTime(25, 99)
        val time2 = MarsTime(25, 99)
        (time1 == time2) should be(true)
    }
    //endregion

    //TODO: test for invalid input
}) {


}