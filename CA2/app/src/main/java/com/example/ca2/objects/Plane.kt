package com.example.ca2.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.ca2.R
import com.example.ca2.SensorHandler

class Plane(private val context: Context) {
    var x = 300f
    var y = 600f
    private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.fighterjet)
    private val speedFactor = 2
    fun update(sensorHandler: SensorHandler) {
        val (pitch, roll) = sensorHandler.computeOrientation()
        x += roll * speedFactor
        y += pitch * speedFactor

    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x, y, null)
    }

    fun getRect(): Rect = Rect(x.toInt(), y.toInt(), x.toInt() + bitmap.width, y.toInt() + bitmap.height)

    fun getBitmap(): Bitmap = bitmap
}
