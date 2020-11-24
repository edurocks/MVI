package com.joaquimley.heetch.heetchest.data

import com.joaquimley.heetch.heetchest.data.remote.HiringHeetchApi
import com.joaquimley.heetch.heetchest.data.remote.model.DriverRemoteModel
import com.joaquimley.heetch.heetchest.model.DriverModel
import com.joaquimley.heetch.heetchest.util.DataFactory
import com.joaquimley.heetch.heetchest.util.DataFactory.randomString
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class DriverRepositoryImplTest {

    private val robot = Robot()

    private val mockRemoteApi = mock<HiringHeetchApi>()
    private val mockMapper = mock<DriverMapper>()

    private val testBackgroundThread = Schedulers.trampoline()
    private val testMainThread = Schedulers.trampoline()
    private val testDelayThread = TestScheduler()

    private val testIntervalInSeconds: Long = 5

    private var testSubscriber: TestSubscriber<List<DriverModel>>? = null
    private lateinit var repository: DriversRepositoryImpl

    @Before
    fun setup() {
        repository =
            DriversRepositoryImpl(mockRemoteApi, testBackgroundThread, testMainThread, testDelayThread,
                testIntervalInSeconds, mockMapper)
    }

    @After
    fun tearDown() {
        testSubscriber?.dispose()
    }

    @Test
    fun `Get drivers position does not terminate`() {
        // Given
        robot.remoteGetDriversSuccess()
        // When
        testSubscriber = repository.getDriversPosition().test()
        // Then
        testSubscriber?.assertNotTerminated()
    }

    @Test
    fun `Get drivers position requests data from remote`() {
        // Given
        robot.remoteGetDriversSuccess()
        // When
        testSubscriber = repository.getDriversPosition().test()
        // Then
        verify(mockRemoteApi, atLeastOnce()).getCoordinates()
    }

    @Test
    fun `Get drivers position maps data from remote`() {
        // Given
        val remoteData = robot.remoteGetDriversSuccess()
        robot.mapperRemoteToModel(remoteData)
        // When
        testSubscriber = repository.getDriversPosition().test()
        // Then
        verify(mockMapper, atLeastOnce()).toModel(remoteData)
    }

    @Test
    fun `Get drivers position passes data`() {
        // Given
        val remoteData = robot.remoteGetDriversSuccess()
        val data = robot.mapperRemoteToModel(remoteData)
        // When
        testSubscriber = repository.getDriversPosition().test()
        // Then
        testSubscriber?.assertValue(data)
    }

    @Test
    fun `When get drivers position errors from remote, correct error is passed`() {
        // Given
        val error = robot.remoteGetDriversFailed()
        // When
        testSubscriber = repository.getDriversPosition().test()
        // Then
        testSubscriber?.assertError(error)
    }

    @Test
    fun `When get drivers position errors subscription terminates`() {
        // Given
        robot.remoteGetDriversFailed()
        // When
        testSubscriber = repository.getDriversPosition().test()
        // Then
        testSubscriber?.assertTerminated()
    }

    @Test
    fun `Get drivers position makes multiple data requests from remote with correct interval`() {
        // Given
        robot.remoteGetDriversSuccess()
        // When
        testSubscriber = repository.getDriversPosition().test()
        testDelayThread.advanceTimeBy(testIntervalInSeconds, TimeUnit.SECONDS)
        // Then
        assert(testSubscriber?.valueCount() == 2)
        {"Expected 2 but values there were ${testSubscriber?.valueCount()} values"}
    }

    inner class Robot {

        fun mapperRemoteToModel(from: List<DriverRemoteModel> = DataFactory.driverRemoteModelList(),
                                list: List<DriverModel> = DataFactory.driverModelList(from.size)): List<DriverModel> {

            whenever(mockMapper.toModel(from)).then { list }
            return list
        }

        fun remoteGetDriversSuccessMultiple(firstData: List<DriverRemoteModel> = DataFactory.driverRemoteModelList(),
                                            secondData: List<DriverRemoteModel> = DataFactory.driverRemoteModelList()) {

            whenever(mockRemoteApi.getCoordinates()).thenReturn(Single.just(firstData), Single.just(secondData))
        }

        fun remoteGetDriversSuccess(data: List<DriverRemoteModel> = DataFactory.driverRemoteModelList())
                : List<DriverRemoteModel>{
            whenever(mockRemoteApi.getCoordinates()).then{ Single.just(data) }
            return data
        }

        fun remoteGetDriversFailed(errorMessage: String = randomString()): Throwable {
            val throwable = Throwable(errorMessage)
            whenever(mockRemoteApi.getCoordinates()).then { Single.error<Throwable>(throwable) }
            return throwable
        }
    }
}