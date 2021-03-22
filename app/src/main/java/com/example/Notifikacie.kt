package com.example

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.CalendarView
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ModelClasess.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.skyhope.eventcalenderlibrary.model.Event
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.activity_liecba.*
import kotlinx.android.synthetic.main.activity_notifikacie.*
import org.json.JSONObject
import java.util.*


class Notifikacie :AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikacie)



        firebaseUser = FirebaseAuth.getInstance().currentUser



        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser!!.uid).child(
                "switch4"
        ).get().addOnSuccessListener {
            switch4.isChecked = it.value as Boolean
        }.addOnFailureListener {
            switch4.isChecked = false
        }

        switch4.isChecked = Users().getSwitch4()

        val toolbar: Toolbar = findViewById(R.id.toolbar_notifikacie)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Hlásenia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }

//        val myTask = OneoffTask.Builder()
//            .setService(BackgroundTaskHandler::class.java)
//            .setExecutionWindow(
//                    (5 * DateUtil.SECONDS_PER_MINUTE).toLong(), (15 * DateUtil.SECONDS_PER_MINUTE).toLong()
//            )
//            .setTag("test-upload")
//            .build()
//        GcmNetworkManager.getInstance(this).schedule(myTask)

//        val alarmMgr: AlarmManager? = null
//        lateinit var alarmIntent: PendingIntent
//
//        alarmMgr?.setInexactRepeating(
//                AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//                alarmIntent
//
//        )

        switch4.setOnCheckedChangeListener { buttonView, isChecked ->

            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser!!.uid)
                    .child("switch4").setValue(isChecked)   // uloz true or false
            if (isChecked) {
                getApiPollen()
                getApiAirConditions()
               // calendarInActivity()

                Toast.makeText(
                        applicationContext,
                        "Hlásenie je zapnuté",
                        Toast.LENGTH_SHORT
                ).show()
            } else {

                Toast.makeText(
                        applicationContext,
                        "Hlásenie je vypnuté",
                        Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

            fun calendarInActivity() {
                val calendar1: CalendarView = findViewById(R.id.calendar)

                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = 20
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0

                val eventStart = calendar.timeInMillis

                calendar.add(Calendar.HOUR, 3)
                val eventEnd = calendar.timeInMillis

                val event = Event(eventStart, "Test.............kalendar", eventEnd.toInt())

                val events: MutableList<Event> = ArrayList()
                events.add(event)


//                val event1 = Event(calendar.Time, "Test")
//// to set desire day time milli second in first parameter
////or you set color for each event
//// to set desire day time milli second in first parameter
////or you set color for each event
//                val event = Event(calendar.getTimeInMillis(), "Test", Color.RED)
//                calendar.addEvent(event)

            }

            fun calendarEvent (){

                val startMillis = Calendar.getInstance().timeInMillis

                val intent = Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                        .putExtra(CalendarContract.Events.EVENT_COLOR, Color.RED)
                        .putExtra(CalendarContract.Events.TITLE, "Pelinka")
                        .putExtra(
                                CalendarContract.Events.DESCRIPTION,
                                "Zmena hladiny pelov v ovzdusi"
                        )
                        .putExtra(
                                CalendarContract.Events.AVAILABILITY,
                                CalendarContract.Events.AVAILABILITY_FREE
                        )
                        .putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, "NAme test")
                startActivity(intent)
            }

            fun getApiAirConditions (){
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
                        val jObjectData = JSONObject(jsonData).getJSONArray("data").getJSONObject(0)

                        val aqi = jObjectData.getString("aqi")
                        if (aqi.toFloat() > 78 || aqi.toFloat() < 78) {
                            hlasenia()
                            calendarEvent()
                        }

                        println("........................................" + aqi)

                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                    println(".................................after air")
                }.start()

            }

        fun getApiPollen () {
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
                    if (tree > 1) {
                        hlasenia()
                        calendarEvent()
                    }

                    println("........................................" + tree)

                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
                println(".................................after")
            }.start()
        }

        fun hlasenia() {

            lateinit var notificationManager: NotificationManager
            lateinit var notificationChannel: NotificationChannel
            lateinit var builder: Notification.Builder
            val channelId = "test"
            val description = "Test notification"

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            intent = Intent(this@Notifikacie, Liecba::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    this@Notifikacie,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            val contentView = RemoteViews(packageName, R.layout.after_notification)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(
                        channelId,
                        description,
                        NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this@Notifikacie, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(
                                BitmapFactory.decodeResource(
                                        this@Notifikacie.resources,
                                        R.drawable.ic_launcher_background
                                )
                        )
                        .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(this@Notifikacie)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(
                                BitmapFactory.decodeResource(
                                        this@Notifikacie.resources,
                                        R.drawable.ic_launcher_background
                                )
                        )
                        .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())
        }


}