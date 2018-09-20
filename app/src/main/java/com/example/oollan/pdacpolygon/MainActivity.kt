package com.example.oollan.pdacpolygon

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oollan.pdacpolygon.checker.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.kml.KmlLayer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMarker: Marker
    private lateinit var mChecker: Checker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (map as SupportMapFragment).getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        initMap(googleMap)
        initPolygon()
    }

    private fun initMap(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(LatLng(32.068713, 34.793430), 14F)
        )

        mMap.setOnMapClickListener {
            if (::mMarker.isInitialized)
                mMarker.remove()

            val results = FloatArray(3)
            val nearestPoint = mChecker.findNearestPoint(it)
            Location.distanceBetween(
                it.latitude,
                it.longitude,
                nearestPoint.latitude,
                nearestPoint.longitude,
                results
            )

            mMarker = mMap.addMarker(
                MarkerOptions().position(it)
                    .title("Distance: " + String.format("%.3f", results[0]) + "m")
            )

            mMarker.showInfoWindow()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initPolygon() {
        val kmlLayer = KmlLayer(mMap, R.raw.allowed_area, this)
        kmlLayer.addLayerToMap()
        kmlLayer.containers.first().placemarks.forEach {
            val list = it.geometry.geometryObject as ArrayList<*>
            mChecker = CheckerImpl(list[0] as ArrayList<LatLng>)
        }

        kmlLayer.setOnFeatureClickListener {
            if (::mMarker.isInitialized)
                mMarker.remove()

            Toast.makeText(this, "Inside the polygon!", Toast.LENGTH_SHORT).show()
        }
    }
}