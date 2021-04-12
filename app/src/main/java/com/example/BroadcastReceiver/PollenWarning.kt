package com.example.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.Services.PollenService
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONObject


class PollenWarning : BroadcastReceiver () {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context?, intent: Intent?) {

        // todo: (check) use api and if pollen level is hight, send notif, else do nothing.

        getApiPollen(context, intent)

    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getApiPollen(context: Context?, intent: Intent?) {
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()

                    .url("https://air-quality.p.rapidapi.com/current/airquality?lon=17.58723&lat=48.37741")
                    .get()
                    .addHeader(
                            "x-rapidapi-key",
                            "99e79c0f6bmshc123686311b75c7p12063ajsn680bf32ad0ca"
                    )
                    .addHeader("x-rapidapi-host", "air-quality.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()
                val jsonData: String = response.body().string()
                val jobject = JSONObject(jsonData)

                val tree = jobject.getJSONArray("data").getJSONObject(0).getInt("pollen_level_tree")

                println("test5" + tree)
                if (tree == 1) {

                    // call the notification service
                    val service = Intent(context, PollenService::class.java)
                    if (intent != null) {
                        service.putExtra("reason", intent.getStringExtra("reason"))
                      //calendarEvent(context)
                    }
                    if (intent != null) {
                        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
                       // calendarEvent(context)
                    }

                    if (context != null) {
                        context.startService(service)
                        // add calendar event

                      //  calendarEvent(context)
                    }

//                    // add calendar event
//
//                    calendarEvent(context)
                    println("Test........................................nice kalendar")
                }
//                     }

                println("Test........................................Tree level" + tree)
//

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }.start()
        println("Test.................................after Pollen level")
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun calendarEvent(context: Context?) {
//        println("test5, calendar")
//        val startMillis = Calendar.getInstance().timeInMillis
//
//        val intent = Intent(Intent.ACTION_INSERT)
//            .setData(CalendarContract.Events.CONTENT_URI)
//            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
//            .putExtra(CalendarContract.Events.EVENT_COLOR, Color.RED)
//            .putExtra(CalendarContract.Events.TITLE, "Pelinka")
//            .putExtra(
//                CalendarContract.Events.DESCRIPTION,
//                "Zmena hladiny pelov v ovzdusi")
//            .putExtra(
//                CalendarContract.Events.AVAILABILITY,
//                CalendarContract.Events.AVAILABILITY_FREE
//            )
//            .putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, "NAme test")
//        if (context != null) {
//            context.startActivity(intent)
//        }
//    }
}