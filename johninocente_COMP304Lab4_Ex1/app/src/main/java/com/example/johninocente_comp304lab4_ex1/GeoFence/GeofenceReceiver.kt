package com.example.johninocente_comp304lab4_ex1.GeoFence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (GeofencingEvent.fromIntent(intent)?.hasError() == true) {
            Toast.makeText(context, "Geofence error occurred", Toast.LENGTH_LONG).show()
            return
        }

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val transition = geofencingEvent?.geofenceTransition

        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Toast.makeText(context, "You are near at your marked place", Toast.LENGTH_LONG).show()
        } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(context, "You are far now from the marked place", Toast.LENGTH_LONG).show()
        }
    }
}