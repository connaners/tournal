package com.plete.tournal

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.net.IDN
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

open class MapsActivity : AppCompatActivity() {

    open lateinit var mMap: GoogleMap
//    open lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        getCurrentLocation()

        btnSet.setOnClickListener {
            addRecord()
        }

        val history = Intent(application, history::class.java)
        btnHistory.setOnClickListener {
           startActivity(history)
        }

    }

    /*
    fun getLoc(){
        address
    }

     */

    private fun addRecord(){
        val desc = etDesc.text.toString()

//        val loc = address.toString()

        val dbHandler = dbHandler(this)

        if (!desc.isEmpty()){
            val status = dbHandler.save((dbModel(0, desc, "date", "loc")))
            if (status > 0){
                Toast.makeText(this, "record saved", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "gak ke ssave dah", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun getCurrentLocation() {
//        mMap = googleMap

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        val fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest()
            .setInterval(1000)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations){
                        mapFragment.getMapAsync(OnMapReadyCallback {
                            mMap = it
                            if (ActivityCompat.checkSelfPermission(this@MapsActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(this@MapsActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
                                ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
                            }
                            mMap.clear()
                            mMap.isMyLocationEnabled = true
                            mMap.uiSettings.isZoomControlsEnabled = true
                            val locationResult = LocationServices.getFusedLocationProviderClient(this@MapsActivity).lastLocation
                            locationResult.addOnCompleteListener(this@MapsActivity) {
                                if (it.isSuccessful && it.result != null){
                                    var currentLocation = it.result
                                    var currentLatitude = currentLocation.latitude
                                    var currentLongitude = currentLocation.longitude

                                    val geocoder = Geocoder(this@MapsActivity)
                                    var geoCoderResult = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)

                                    var myLocation = LatLng(currentLatitude, currentLongitude)

//                                    address = geoCoderResult[0].getAddressLine(0).toString()

                                    mMap.addMarker(MarkerOptions().position(myLocation).title(geoCoderResult[0].getAddressLine(0))).showInfoWindow()
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
//                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                                }
                            }
                        })
                    }
                }
            },
            Looper.myLooper()
        )
    }

}