package io.github.hanihashemi.eggmaster.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.hanihashemi.eggmaster.MainActivity
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.data.preferences.EggMasterPreferences
import io.github.hanihashemi.eggmaster.extensions.formatSecondsToMinutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    companion object {
        const val BOILING_TIME_PARAM = "boilingTime"
        const val REMAINING_TIME = "remainingTime"
        const val TIME_UPDATE_ACTION = "TIMER_UPDATED"
        private const val CHANNEL = "TIMER_SERVICE_CHANNEL"
    }

    private var remainingTime: Int = 0
    private var job: Job? = null

    @Inject
    lateinit var preferences: EggMasterPreferences

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        val timeInMillis = intent?.getIntExtra(BOILING_TIME_PARAM, 0) ?: 0
        startTimer(timeInMillis)
        return START_NOT_STICKY
    }

    private fun startForeground() {
        val notification = createNotification("Timer started", 0, 0)
        val notificationType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        } else 0

        ServiceCompat.startForeground(
            this@TimerService,
            1,
            notification,
            notificationType
        )
    }

    private fun startTimer(timeInMillis: Int) {
        remainingTime = timeInMillis
        job = CoroutineScope(Dispatchers.Default).launch {
            preferences.saveStartTimerServiceData()
            while (remainingTime > 0) {
                val progress = timeInMillis - remainingTime
                sendTimeUpdate()
                updateNotification(
                    "Remaining time: ${remainingTime.formatSecondsToMinutes()}",
                    progress,
                    timeInMillis
                )
                delay(1000) // delay for 1 second
                remainingTime -= 1
            }
            sendTimeUpdate()
            showCompletionNotification()
            preferences.saveEndTimerServiceData()
            stopSelf()
        }
    }

    private fun sendTimeUpdate() {
        val intent = Intent(TIME_UPDATE_ACTION)
        intent.putExtra(REMAINING_TIME, remainingTime)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL,
            "Timer Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(content: String, progress: Int, maxProgress: Int): Notification {
        return NotificationCompat.Builder(this, CHANNEL)
            .setContentTitle("Timer Service")
            .setContentText(content)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            )
            .setSmallIcon(R.drawable.ic_egg)
            .setProgress(maxProgress, progress, false)
            .setSilent(true) // Don't play a sound
            .build()
    }

    private fun updateNotification(content: String, progress: Int = 0, maxProgress: Int = 0) {
        val notification = createNotification(content, progress, maxProgress)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun showCompletionNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(this, CHANNEL)
            .setContentTitle("Egg Timer")
            .setContentText("Egg is ready!")
            .setSmallIcon(R.drawable.ic_egg)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}