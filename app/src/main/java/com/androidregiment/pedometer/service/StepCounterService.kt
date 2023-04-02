package com.androidregiment.pedometer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.androidregiment.pedometer.screen.utils.Constant.ACTION_SERVICE_START
import com.androidregiment.pedometer.screen.utils.Constant.ACTION_SERVICE_STOP
import com.androidregiment.pedometer.screen.utils.Constant.NOTIFICATION_CHANNEL_ID
import com.androidregiment.pedometer.screen.utils.Constant.NOTIFICATION_CHANNEL_NAME
import com.androidregiment.pedometer.screen.utils.Constant.NOTIFICATION_ID
import com.androidregiment.pedometer.screen.utils.Constant.STEP_COUNTER_STATE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService() : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null


    var steps = mutableStateOf(0)
        private set
    var currentState = mutableStateOf(StepCounterState.Stopped.name)
        private set

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StepCounterBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STEP_COUNTER_STATE)) {
            StepCounterState.Started.name -> {
                registerSensorListener()
                startForegroundService()
                currentState.value = StepCounterState.Started.name
            }

            StepCounterState.Stopped.name -> {
                stopForegroundService()
                unregisterSensorListener()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    currentState.value = StepCounterState.Started.name
                    registerSensorListener()
                    startForegroundService()
                }

                ACTION_SERVICE_STOP -> {
                    unregisterSensorListener()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onCreate() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        super.onCreate()
    }

    override fun onDestroy() {
        unregisterSensorListener()
        super.onDestroy()
    }

    private fun registerSensorListener() {
        sensorManager.registerListener(
            this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun unregisterSensorListener() {
        sensorManager.unregisterListener(this, stepSensor)
    }


    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        currentState.value = StepCounterState.Stopped.name
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(stepsCount: String) {
        notificationManager.notify(
            NOTIFICATION_ID, notificationBuilder.setContentText(
                stepsCount
            ).build()
        )
    }


    override fun onBind(intent: Intent?): IBinder? = StepCounterBinder()

    inner class StepCounterBinder : Binder() {
        fun getService(): StepCounterService = this@StepCounterService
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            steps.value = event.values[0].toInt()
            updateNotification(event.values[0].toInt().toString())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

}

enum class StepCounterState {
    Started, Stopped,
}