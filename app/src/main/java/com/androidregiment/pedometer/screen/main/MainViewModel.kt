package com.androidregiment.pedometer.screen.main

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(context: Application) : AndroidViewModel(context), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    val doesSensorExist: Boolean =
        context.packageManager.hasSystemFeature(Sensor.TYPE_STEP_COUNTER.toString())


    private val stepStateFlow = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = stepStateFlow

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun startCounting() {
        sensorManager.registerListener(
            this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stopCounting() {
        sensorManager.unregisterListener(this, stepSensor)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            stepStateFlow.value = event.values[0].toInt()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit


}