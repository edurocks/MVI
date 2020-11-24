package com.joaquimley.heetch.heetchest.data.remote

import com.joaquimley.heetch.heetchest.data.remote.model.DriverRemoteModel
import io.reactivex.Single
import retrofit2.http.PUT

interface HiringHeetchApi {

    @PUT("coordinates")
    fun getCoordinates(): Single<List<DriverRemoteModel>>


}
