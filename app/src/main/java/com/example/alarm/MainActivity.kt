package com.example.alarm

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val materialTimePicker: MaterialTimePicker by lazy {
        MaterialTimePicker.Builder().apply {
            setTimeFormat(TimeFormat.CLOCK_24H)
            setHour(12)
            setMinute(0)
            setTitleText("Set Alarm")
        }.build()
    }

    private val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBtn()
        initListener()
    }

    private fun initListener() {
        materialTimePicker.addOnPositiveButtonClickListener {
            initAlarmManager()
        }
    }

    private fun initAlarmManager() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MILLISECOND, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MINUTE, materialTimePicker.minute)
            set(Calendar.HOUR, materialTimePicker.hour)
        }
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmClockInfo = AlarmClockInfo(calendar.timeInMillis, getAlarmInfoPendingIntent())
        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())
        initTxt(calendar)
    }

    private fun initTxt(calendar: Calendar) {
        findViewById<TextView>(R.id.txt_alarm).text = simpleDateFormat.format(calendar.time)
    }

    private fun initBtn() {
        findViewById<Button>(R.id.btn_set_alarm).setOnClickListener {
            initTimePicker()
        }
    }

    private fun initTimePicker() {
        materialTimePicker.show(supportFragmentManager, "")
    }

    private fun getAlarmInfoPendingIntent(): PendingIntent {
        val alarmInfoIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                0,
                alarmInfoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                alarmInfoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return pendingIntent
    }

    private fun getAlarmActionPendingIntent(): PendingIntent {
        val intent = Intent(this, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        } else{
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return pendingIntent
    }
}