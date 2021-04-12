package com.example

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_notifikacie.*


open class  Notifikacie  : AppCompatActivity(){

    var firebaseUser: FirebaseUser? = null
    private var alarmManager: AlarmManager?=null
    lateinit var context: Context
    private var pendingIntent: PendingIntent? = null
    lateinit var notificationManager: NotificationManager

    fun Notifikacie() {

    }

    override fun onStart() {
        super.onStart()

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser!!.uid).child(
                "switch4"
        ).get().addOnSuccessListener {
            switch4.isChecked = it.value as Boolean
        }.addOnFailureListener {
            switch4.isChecked = false
        }

            switch4.setOnCheckedChangeListener { buttonView, isChecked ->

            println("test5"+isChecked)
            println("test5"+ FirebaseDatabase.getInstance())
            println("test5"+ firebaseUser!!.uid)
            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser!!.uid)
                    .child("switch4").setValue(isChecked)   // uloz true or false
            if (isChecked) {
             //  setAlarm()
                // calendarInActivity()

                Toast.makeText(applicationContext, "Hlásenie je zapnuté", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Hlásenie je vypnuté", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikacie)

        firebaseUser = FirebaseAuth.getInstance().currentUser
//
//        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser!!.uid).child(
//                "switch4"
//        ).get().addOnSuccessListener {
//            switch4.isChecked = it.value as Boolean
//        }.addOnFailureListener {
//            switch4.isChecked = false
//        }

  //      switch4.isChecked = Users().getSwitch4()

        val toolbar: Toolbar = findViewById(R.id.toolbar_notifikacie)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Hlásenia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }
    }

//    class Receiver : BroadcastReceiver() {
//        @SuppressLint("UnsafeProtectedBroadcastReceiver")
//        override fun onReceive(
//                context: Context,
//                intent: Intent) {
//            Notifikacie(context).getApiAirConditions()
//            Notifikacie(context).getApiPollen()
//            println("Test........................................Bezi broadcast")
//
//        }
//    }

//    fun setAlarm() {
//        val intent = Intent(this, Receiver::class.java)
//        alarmManager =  getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//        alarmManager!!.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis(), 1000 * 60 * 1, pendingIntent
//        )
//        Toast.makeText(this, "Alarm je zapnuty", Toast.LENGTH_SHORT).show()
//    }

//            fun calendarInActivity() {
//
//                calendar[Calendar.HOUR_OF_DAY] = 20
//                calendar[Calendar.MINUTE] = 0
//                calendar[Calendar.SECOND] = 0
//                calendar[Calendar.MILLISECOND] = 0
//
//                val eventStart = calendar.timeInMillis
//
//                calendar.add(Calendar.HOUR, 3)
//                val eventEnd = calendar.timeInMillis
//
//                val event = Event(eventStart, "Test.............kalendar", eventEnd.toInt())
//
//                val events: MutableList<Event> = ArrayList()
//                events.add(event)
//
//            }

//    fun calendarEvent() {
//
//        val startMillis = Calendar.getInstance().timeInMillis
//
//        val intent = Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
//                .putExtra(CalendarContract.Events.EVENT_COLOR, Color.RED)
//                .putExtra(CalendarContract.Events.TITLE, "Pelinka")
//                .putExtra(
//                        CalendarContract.Events.DESCRIPTION,
//                        "Zmena hladiny pelov v ovzdusi")
//                .putExtra(
//                        CalendarContract.Events.AVAILABILITY,
//                        CalendarContract.Events.AVAILABILITY_FREE
//                )
//                .putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, "NAme test")
//        startActivity(intent)
//    }

//    fun getApiAirConditions() {
//        Thread {
//            try {
//                val client = OkHttpClient()
//                val request = Request.Builder()
//
//                        .url("https://air-quality.p.rapidapi.com/current/airquality?lon=17.58723&lat=48.37741")
//                        .get()
//                        .addHeader(
//                                "x-rapidapi-key",
//                                "99e79c0f6bmshc123686311b75c7p12063ajsn680bf32ad0ca"
//                        )
//                        .addHeader("x-rapidapi-host", "air-quality.p.rapidapi.com")
//                        .build()
//
//                val response = client.newCall(request).execute()
//                val jsonData: String = response.body().string()
//                val jObjectData = JSONObject(jsonData).getJSONArray("data").getJSONObject(0)
//
//                val aqi = jObjectData.getString("aqi")
//                if (aqi.toFloat() > 78 || aqi.toFloat() < 78) {
////                    hlasenia()
//                    calendarEvent()
//                }
//                println("Test........................................Aqi" + aqi)
//            } catch (ex: java.lang.Exception) {
//                ex.printStackTrace()
//            }
//        }.start()
//        println("Test.................................after air condition")
//    }



}