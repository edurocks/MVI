package com.joaquimley.heetch.heetchest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.joaquimley.heetch.heetchest.data.DriverMapper
import com.joaquimley.heetch.heetchest.data.DriversRepository
import com.joaquimley.heetch.heetchest.data.error.DataError
import com.joaquimley.heetch.heetchest.location.LocationManager
import com.joaquimley.heetch.heetchest.model.DriverModel
import com.joaquimley.heetch.heetchest.model.DriverUiModel
import com.joaquimley.heetch.heetchest.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DriversRepository,
    private val locationManager: LocationManager,
    private val mapper: DriverMapper
) : ViewModel() {

    private val bag = CompositeDisposable()

    private val driversLiveData: MutableLiveData<List<DriverModel>> = MutableLiveData()
    private val closestDriverLiveData: MutableLiveData<DriverUiModel> = MutableLiveData()
    private val cameraMidpointPositionSingleEventLiveData: SingleLiveEvent<LatLng> = SingleLiveEvent()

    private val errorsLiveData: SingleLiveEvent<DataError> = SingleLiveEvent()

    override fun onCleared() {
        super.onCleared()
        bag.dispose()
    }

    fun onCenterCameraLocationButtonClicked() {
        requestCenteredCameraLocation()
    }

    fun getDrivers(): LiveData<List<DriverModel>> {
        if (driversLiveData.value == null) {
            observeDriversPosition()
        }
        return driversLiveData
    }

    fun getClosestDriverInfo(): LiveData<DriverUiModel> {
        if (closestDriverLiveData.value == null) {
            observeClosestDriver()
        }
        return closestDriverLiveData
    }

    fun getCenteredLocation(): LiveData<LatLng> = cameraMidpointPositionSingleEventLiveData

    fun getErrors(): LiveData<DataError> = errorsLiveData

    private fun observeDriversPosition() {
        bag.add(repository.getDriversPosition()
            .doOnNext { locationManager.updateClosestPositionDistanceData(it) }
            .subscribe({
                driversLiveData.postValue(it)
            }, {
                errorsLiveData.postValue(DataError.DriversPositionError())
            })
        )
    }

    private fun observeClosestDriver() {
        bag.add(
            locationManager.getClosestDriverPositionFromData()
                .subscribe({
                    closestDriverLiveData.postValue(mapper.toUi(it.driver, it.distance))
                }, {
                    errorsLiveData.postValue(DataError.ClosestDriverError())
                })
        )
    }

    private fun requestCenteredCameraLocation() {
        bag.add(
            locationManager.getMidpointToClosestPosition()
                .subscribe({
                    cameraMidpointPositionSingleEventLiveData.postValue(it)
                }, {
                    errorsLiveData.postValue(DataError.MidpointPositionError())
                })
        )
    }
}