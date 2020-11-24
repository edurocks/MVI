package com.joaquimley.heetch.heetchest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.joaquimley.heetch.heetchest.data.DriverMapper
import com.joaquimley.heetch.heetchest.data.DriversRepository
import com.joaquimley.heetch.heetchest.data.error.DataError
import com.joaquimley.heetch.heetchest.location.LocationManager
import com.joaquimley.heetch.heetchest.location.model.ClosestDriverModel
import com.joaquimley.heetch.heetchest.model.DriverModel
import com.joaquimley.heetch.heetchest.model.DriverUiModel
import com.joaquimley.heetch.heetchest.util.DataFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.times


/**
 * Unit test for [HomeViewModel]
 */
class HomeViewModelTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val robot = Robot()

    private val mockRepository = mock<DriversRepository>()
    private val mockLocationManager = mock<LocationManager>()
    private val mockMapper = mock<DriverMapper>()

    private val driversPositionMockObserver = mock<Observer<List<DriverModel>>>()
    private val closestDriverInfoMockObserver = mock<Observer<DriverUiModel>>()
    private val midpointPositionMockObserver = mock<Observer<LatLng>>()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(mockRepository, mockLocationManager, mockMapper)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `Drivers location is not observed when there are no requests`() {
        // Given
        // No op
        // When
        // No observation/subscription

        // Then
        verify(mockRepository, never()).getDriversPosition()
    }


    @Test
    fun `Drivers location is requested when viewModel has observers`() {
        // Given
        robot.repositoryDriversPositionSuccess()
        // When
        viewModel.getDrivers().observeForever(driversPositionMockObserver)
        // Then
        verify(mockRepository, times(1)).getDriversPosition()
    }

    @Test
    fun `When drivers position is received, data is passed to observers`() {
        // Given
        val drivers = robot.repositoryDriversPositionSuccess()
        // When
        viewModel.getDrivers().observeForever(driversPositionMockObserver)
        // Then
        verify(driversPositionMockObserver).onChanged(drivers)
    }

    @Test
    fun `When drivers position errors, correct error is passed to observers`() {
        // Given
        robot.repositoryDriversPositionFailed()
        // When
        viewModel.getDrivers().observeForever(driversPositionMockObserver)
        // Then
        assert(viewModel.getErrors().value is DataError.DriversPositionError)
        { "Expected ${DataError.DriversPositionError::class.java.simpleName}\nActual value was ${viewModel.getErrors().value}" }
    }

    @Test
    fun `When drivers location data is received, new closest driver info is requested`() {
        // Given
        robot.locationManagerClosestDriverSuccess()
        val data = robot.repositoryDriversPositionSuccess()
        // When
        viewModel.getDrivers().observeForever(driversPositionMockObserver)
        viewModel.getClosestDriverInfo().observeForever(closestDriverInfoMockObserver)
        // Then
        verify(mockLocationManager, times(1)).updateClosestPositionDistanceData(data)
    }


    @Test
    fun `Closest driver location is not observed when there are no requests`() {
        // Given
        // No op
        // When
        // No observation

        // Then
        verify(mockLocationManager, never()).getClosestDriverPositionFromData()
    }


    @Test
    fun `Closest driver location is observed when viewModel has observers`() {
        // Given
        robot.locationManagerClosestDriverSuccess()
        // When
        viewModel.getClosestDriverInfo().observeForever(closestDriverInfoMockObserver)
        // Then
        verify(mockLocationManager, times(1)).getClosestDriverPositionFromData()
    }


    @Test
    fun `When closest driver location data is received it is passed to observers`() {
        // Given
        val closestModel = robot.locationManagerClosestDriverSuccess()
        val closestUiModel = robot.mapFromClosestModelToUi(closestModel)
        // When
        viewModel.getClosestDriverInfo().observeForever(closestDriverInfoMockObserver)
        // Then
        closestDriverInfoMockObserver.onChanged(closestUiModel)
    }

    @Test
    fun `When closest driver location errors, correct error is passed to observers`() {
        // Given
        robot.locationClosestDriverPositionFailed()
        // When
        viewModel.getClosestDriverInfo().observeForever(closestDriverInfoMockObserver)
        // Then
        assert(viewModel.getErrors().value is DataError.ClosestDriverError)
        { "Expected ${DataError.ClosestDriverError::class.java.simpleName}\nActual value was ${viewModel.getErrors().value}" }
    }


    @Test
    fun `When center camera button is clicked, new camera position is requested`() {
        // Given
        robot.locationManagerMidpointToClosestPositionSuccess()
        // When
        viewModel.onCenterCameraLocationButtonClicked()
        // Then
        verify(mockLocationManager, times(1)).getMidpointToClosestPosition()
    }

    @Test
    fun `When center position is received data is passed to observers`() {
        // Given
        val midpointPosition = robot.locationManagerMidpointToClosestPositionSuccess()
        viewModel.getCenteredLocation().observeForever(midpointPositionMockObserver)
        // When
        viewModel.onCenterCameraLocationButtonClicked()
        // Then
        verify(midpointPositionMockObserver).onChanged(midpointPosition)
    }

    @Test
    fun `When center position errors, correct error is passed to observers`() {
        // Given
        robot.locationMidpointToClosestPositionFailed()
        // When
        viewModel.onCenterCameraLocationButtonClicked()
        // Then
        assert(viewModel.getErrors().value is DataError.MidpointPositionError)
        { "Expected ${DataError.MidpointPositionError::class.java.simpleName}\nActual value was ${viewModel.getErrors().value}" }
    }


    inner class Robot {

        fun repositoryDriversPositionSuccess(list: List<DriverModel> = DataFactory.driverModelList()): List<DriverModel> {
            whenever(mockRepository.getDriversPosition()).then { Flowable.just(list) }
            return list
        }

        fun locationManagerClosestDriverSuccess(closestDriverModel: ClosestDriverModel = DataFactory.closestDriverModel()): ClosestDriverModel {
            whenever(mockLocationManager.getClosestDriverPositionFromData()).then { Observable.just(closestDriverModel) }
            return closestDriverModel
        }

        fun locationManagerMidpointToClosestPositionSuccess(latLng: LatLng = DataFactory.latLng()): LatLng {
            whenever(mockLocationManager.getMidpointToClosestPosition()).then { Single.just(latLng) }
            return latLng
        }

        fun mapFromClosestModelToUi(
            closestModel: ClosestDriverModel,
            driverUiModel: DriverUiModel = DataFactory.driverUiModel()
        )
                : DriverUiModel {
            whenever(mockMapper.toUi(closestModel.driver, closestModel.distance)).then { driverUiModel }
            return driverUiModel
        }

        /**
         * Errors
         */
        fun repositoryDriversPositionFailed() {
            whenever(mockRepository.getDriversPosition()).then { Flowable.error<Throwable>(Throwable("Connection timeout")) }
        }

        fun locationClosestDriverPositionFailed() {
            whenever(mockLocationManager.getClosestDriverPositionFromData()).then {
                Observable.error<Throwable>(
                    Throwable("No data found")
                )
            }
        }

        fun locationMidpointToClosestPositionFailed() {
            whenever(mockLocationManager.getMidpointToClosestPosition()).then { Single.error<Throwable>(Throwable("Location failed")) }
        }
    }
}