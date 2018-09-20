package com.example.oollan.pdacpolygon.checker

import com.google.android.gms.maps.model.LatLng

interface Checker {
    fun findNearestPoint(test: LatLng): LatLng
    fun findNearestPoint(p: LatLng, start: LatLng, end: LatLng): LatLng
}