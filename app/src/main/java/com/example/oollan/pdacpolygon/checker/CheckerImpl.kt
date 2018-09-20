package com.example.oollan.pdacpolygon.checker

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

class CheckerImpl(private val polygonLng: ArrayList<LatLng>) : Checker {
    private var xMin: Double = 180.0
    private var xMax: Double = -180.0
    private var yMin: Double = 90.0
    private var yMax: Double = -90.0

    init {
        for (latLng in polygonLng) {
            if (latLng.longitude > xMax) xMax = latLng.longitude
            if (latLng.longitude < xMin) xMin = latLng.longitude
            if (latLng.latitude > yMax) yMax = latLng.latitude
            if (latLng.latitude < yMin) yMin = latLng.latitude
        }
    }

    override fun findNearestPoint(test: LatLng): LatLng {
        var distance = -1.0
        var minimumDistancePoint = test

        for (i in polygonLng.indices) {
            val point = polygonLng[i]

            var segmentPoint = i + 1
            if (segmentPoint >= polygonLng.size) {
                segmentPoint = 0
            }

            val currentDistance = PolyUtil
                .distanceToLine(test, point, polygonLng[segmentPoint])
            if (distance == -1.0 || currentDistance < distance) {
                distance = currentDistance
                minimumDistancePoint = findNearestPoint(test, point, polygonLng[segmentPoint])
            }
        }

        return minimumDistancePoint
    }

    override fun findNearestPoint(p: LatLng, start: LatLng, end: LatLng): LatLng {
        if (start == end)
            return start

        val s0lat = Math.toRadians(p.latitude)
        val s0lng = Math.toRadians(p.longitude)
        val s1lat = Math.toRadians(start.latitude)
        val s1lng = Math.toRadians(start.longitude)
        val s2lat = Math.toRadians(end.latitude)
        val s2lng = Math.toRadians(end.longitude)

        val s2s1lat = s2lat - s1lat
        val s2s1lng = s2lng - s1lng
        val u =
            ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng) /
                    (s2s1lat * s2s1lat + s2s1lng * s2s1lng)
        if (u <= 0)
            return start

        return if (u >= 1)
            end
        else LatLng(
            start.latitude + u * (end.latitude - start.latitude),
            start.longitude + u * (end.longitude - start.longitude)
        )
    }
}