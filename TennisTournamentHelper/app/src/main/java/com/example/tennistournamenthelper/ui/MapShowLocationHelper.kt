package com.example.tennistournamenthelper.ui

import com.example.tennistournamenthelper.model.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapShowLocationHelper {
    var marker: Marker? = null
    fun showLocation(map: GoogleMap?, loc: Location, placeName: String?) {
        val latLng = LatLng(loc.latitude!!, loc.longitude!!)
        val option = MarkerOptions().title(placeName).position(latLng)
        val b = 5.5
        val camera = CameraUpdateFactory.newLatLngZoom(latLng, b.toFloat())
        marker = map?.addMarker(option)
        map?.animateCamera(camera)
    }
}