package com.joaquimley.heetch.heetchest.ui

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.joaquimley.heetch.heetchest.R
import com.joaquimley.heetch.heetchest.di.viewmodel.ViewModelFactory
import com.joaquimley.heetch.heetchest.model.DriverModel
import com.joaquimley.heetch.heetchest.utils.toBitmapDescriptor
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject


private const val RC_LOCATION_PERMISSION = 1231

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java] }

    private val markers = HashMap<Int, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottom_bar)

        getMapAsync()
    }

    private fun getMapAsync() {
        (map as SupportMapFragment).getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                showPermissionRequiredSnackbar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            initMap(googleMap)
            observeData(googleMap)
        } else {
            showLocationPermissionExplainView()
        }
    }

    private fun initMap(googleMap: GoogleMap) {
        try {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false

            LocationServices.getFusedLocationProviderClient(this)
                .lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Device location acquired with success
                    googleMap.animateCamera(getMapCameraLocation(LatLng(it.latitude, it.longitude)))
                }
            }
        } catch (exception: SecurityException) {
            showPermissionRequiredSnackbar()
        }
    }

    private fun observeData(googleMap: GoogleMap) {
        viewModel.getDrivers().observe(this, Observer {
            it.forEach { driver -> updateOrCreateMarker(driver, googleMap) }
        })

        viewModel.getClosestDriverInfo().observe(this, Observer {
            closest_driver_view.set(it)
            closest_driver_view.isVisible = it != null

            if (fab.hasOnClickListeners().not()) {
                fab.setOnClickListener {
                    viewModel.onCenterCameraLocationButtonClicked()
                }
            }
        })

        viewModel.getCenteredLocation().observe(this, Observer {
            googleMap.animateCamera(getMapCameraLocation(it))
        })

        viewModel.getErrors().observe(this, Observer {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })


    }

    private fun getMapCameraLocation(latLng: LatLng, zoom: Float = 12f): CameraUpdate? {
        return CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude))
                .zoom(zoom) // Should be calculated with closest 'distance' instead of hardcoded
                .bearing(0f)
                .tilt(25f)
                .build()
        )
    }

    /**
     * There's a bug here, if a certain driver were to "disconnect" that [Marker] wouldn't be removed.
     * A solution is to mark a marker's model/tag (probably [DriverModel]) as 'dirty' if not touched
     * from the new data coming in, therefore marked to be removed (both from the map as well
     * the [markers]). But it is outside of the scope.
     */
    private fun updateOrCreateMarker(driver: DriverModel, googleMap: GoogleMap) {
        markers[driver.id]?.let {
            it.position = driver.position
        } ?: run {
            markers[driver.id] = googleMap.addMarker(
                MarkerOptions()
                    .position(driver.position)
                    .title(driver.displayName)
                    .snippet("Id: ${driver.id} | Position: ${driver.position}")
                    .icon(ContextCompat.getDrawable(this, R.drawable.ic_car)?.toBitmapDescriptor())
            )
        }
    }


    /************************************************************
     *                                                          *
     *   Location Permission section, this could've been moved  *
     *   to a separate UI controller                            *
     *                                                          *
     ************************************************************/

    private fun showLocationPermissionExplainView() {
        RequestLocationExplainFragment.display(supportFragmentManager) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RC_LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    getMapAsync()
                    Toast.makeText(this, getString(R.string.location_permission_granted), Toast.LENGTH_SHORT).show()
                } else {
                    showPermissionRequiredSnackbar()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Shows a persistent snackbar explaining the user of the Location permission importance
     */
    private fun showPermissionRequiredSnackbar() {
        Snackbar.make(
            home_activity_container,
            getString(R.string.request_location_required_description),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.request_location_action_request)) {
            showLocationPermissionExplainView()
        }.show()
    }
}
