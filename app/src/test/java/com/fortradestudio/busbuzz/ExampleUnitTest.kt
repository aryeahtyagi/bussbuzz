package com.fortradestudio.busbuzz

import com.fortradestudio.busbuzz.location.Location
import com.fortradestudio.busbuzz.location.LocationRepository
import com.fortradestudio.busbuzz.location.LocationRepositoryImpl
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun calculateDifference_checkDistance() {

        val locationRepository = LocationRepositoryImpl()
        val location = Location(18.55,73.2)
        val target = Location(18.56,73.2)
        val dist = locationRepository.calculateLinearDistance(location,target)

        assertEquals(dist,2)

    }
}