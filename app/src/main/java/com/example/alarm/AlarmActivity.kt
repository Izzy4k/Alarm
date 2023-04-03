package com.example.alarm

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    private var ringtone: Ringtone? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        startAlarm()
    }

    private fun startAlarm() {
        var notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, notification)
        if (ringtone == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this, notification)
        }
        ringtone?.play()
    }

    override fun onDestroy() {
        if (ringtone != null && ringtone!!.isPlaying) {
            ringtone?.stop()
        }
        super.onDestroy()
    }
}