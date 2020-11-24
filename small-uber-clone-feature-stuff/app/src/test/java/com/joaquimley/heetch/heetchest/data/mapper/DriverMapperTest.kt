package com.joaquimley.heetch.heetchest.data.mapper

import com.joaquimley.heetch.heetchest.data.DriverMapper
import com.joaquimley.heetch.heetchest.util.DataFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random


/**
 * Unit test for [DriverMapper]
 */

class DriverMapperTest {

    private val testBaseUrl = "test.com"

    private lateinit var mapper: DriverMapper

    @Before
    fun setup() {
        mapper = DriverMapper(testBaseUrl)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `From DriverModel to DriverUiModel`() {
        val testDistance = Random.nextDouble()
        val from = DataFactory.driverModel()
        val model = mapper.toUi(from, testDistance)

        assert(model.name == from.displayName)
        assert(model.imageUrl == "$testBaseUrl${from.profilePictureUrl}")
        assert(model.distance == testDistance)
    }

    @Test
    fun `From DriverRemoteModel to DriverModel`() {
        val from = DataFactory.driverRemoteModel()
        val model = mapper.toModel(from)

        assert(model.id == from.id)
        assert(model.firstName == from.firstname)
        assert(model.lastName == from.lastname)
        assert(model.profilePictureUrl == from.image)
        assert(model.position.latitude == from.coordinates.latitude)
        assert(model.position.longitude == from.coordinates.longitude)
    }

}