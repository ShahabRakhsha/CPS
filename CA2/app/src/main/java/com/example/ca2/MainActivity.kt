package com.example.ca2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sensorHandler: SensorHandler
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorHandler = SensorHandler(this)
        gameView = GameView(this, sensorHandler)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        sensorHandler.registerSensors()
    }

    override fun onPause() {
        super.onPause()
        sensorHandler.unregisterSensors()
    }
}
