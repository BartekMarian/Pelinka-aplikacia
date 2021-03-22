package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver () {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            Toast.makeText(context.applicationContext, "Hlásenia sú vypnuté", Toast.LENGTH_SHORT)
                    .show()

        }

    }

}