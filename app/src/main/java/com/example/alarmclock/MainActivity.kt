package com.example.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private var alarmTimePicker: TimePicker? = null
    private var pendingIntent: PendingIntent? = null
    private var alarmManager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmTimePicker = findViewById(R.id.timePicker)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
    }

    fun onToggleClicked(view: View) {
        val toggleButton = view as? ToggleButton

        if (toggleButton?.isChecked == true) {
            Toast.makeText(view.context, "Alarm ON", Toast.LENGTH_SHORT).show()

            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = alarmTimePicker!!.currentHour
            calendar[Calendar.MINUTE] = alarmTimePicker!!.currentMinute
            calendar[Calendar.SECOND] = 0

            val intent = Intent(this, AlarmReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            ) // Add the FLAG_UPDATE_CURRENT flag here
            var time = calendar.timeInMillis

            if (System.currentTimeMillis() > time) {
                time += if (calendar[Calendar.AM_PM] == Calendar.AM) {
                    12 * 60 * 60 * 1000
                } else {
                    24 * 60 * 60 * 1000
                }
            }

            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            alarmManager?.cancel(pendingIntent)
            Toast.makeText(view.context, "Alarm OFF", Toast.LENGTH_SHORT).show()
        }
    }
}


