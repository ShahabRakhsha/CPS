package com.example.ca2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorHandler(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelFiltered = FloatArray(3)
    val gyroFiltered = FloatArray(3)
    val magnetFiltered = FloatArray(3)
    val kalmanPitch = KalmanFilter()
    val kalmanRoll = KalmanFilter()
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)


    private val alpha = 0.8f


    fun registerSensors() {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                for (i in 0..2) {
                    accelFiltered[i] = alpha * accelFiltered[i] + (1 - alpha) * event.values[i]
                }
            }
            Sensor.TYPE_GYROSCOPE -> {
                for (i in 0..2) {
                    gyroFiltered[i] = alpha * gyroFiltered[i] + (1 - alpha) * event.values[i]
                }
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                for (i in 0..2) {
                    magnetFiltered[i] = alpha * magnetFiltered[i] + (1 - alpha) * event.values[i]
                }
            }
        }
    }
    fun computeOrientation(): Pair<Float, Float> {
        val success = SensorManager.getRotationMatrix(rotationMatrix, null, accelFiltered, magnetFiltered)
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            val pitch = orientationAngles[1] * (180f / Math.PI).toFloat()
            val roll = orientationAngles[2] * (180f / Math.PI).toFloat()

            val filteredPitch = kalmanPitch.update(pitch)
            val filteredRoll = kalmanRoll.update(roll)

            return Pair(filteredPitch, filteredRoll)
        }
        return Pair(0f, 0f)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
