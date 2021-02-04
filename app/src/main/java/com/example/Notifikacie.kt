package com.example

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.Fragments.KvalitaVzduchuFragment
import kotlinx.android.synthetic.main.activity_notifikacie.*


class Notifikacie:AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikacie)

        val toolbar: Toolbar = findViewById(R.id.toolbar_notifikacie)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Hlásenia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }




        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



            Notif_status.setOnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked) {
                   // hlasenia()
                    switch1.isChecked = if(switch1.isChecked)false else true
                    switch2.isChecked = if(switch2.isChecked)false else true
                    switch3.isChecked = if(switch3.isChecked)false else true
                    switch4.isChecked = if(switch4.isChecked)false else true
                    switch5.isChecked = if(switch5.isChecked)false else true
                    switch6.isChecked = if(switch6.isChecked)false else true
                    Toast.makeText(applicationContext, "Hlásenia sú zapnuté", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Hlásenia sú vypnuté", Toast.LENGTH_SHORT).show()

                    switch1.isChecked = if(switch1.isChecked)false else true
                    switch2.isChecked = if(switch2.isChecked)false else true
                    switch3.isChecked = if(switch3.isChecked)false else true
                    switch4.isChecked = if(switch4.isChecked)false else true
                    switch5.isChecked = if(switch5.isChecked)false else true
                    switch6.isChecked = if(switch6.isChecked)false else true
                }

            }


    }

    private fun hlasenia() {
            intent = Intent(this, KvalitaVzduchuFragment::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val contentView = RemoteViews(packageName, R.layout.after_notification)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
            } else {

                builder = Notification.Builder(this)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())
    }


}