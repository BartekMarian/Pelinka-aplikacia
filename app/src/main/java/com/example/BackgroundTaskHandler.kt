package com.example

import android.content.Intent
import com.google.android.gms.gcm.GcmNetworkManager
import com.google.android.gms.gcm.GcmTaskService
import com.google.android.gms.gcm.TaskParams

class BackgroundTaskHandler : GcmTaskService() {


    override fun onRunTask(p0: TaskParams?): Int {
        Notifikacie().hlasenia()

        return GcmNetworkManager.RESULT_SUCCESS
    }
    private fun x (){
        println("test                .............................      per")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        x()
        return super.onStartCommand(intent, flags, startId)

    }
}