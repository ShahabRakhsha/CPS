package com.example.ca2.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Bullet(var x: Float, var y: Float) {
    private val speed = 20f
    private val radius = 10f

    fun update() {
        y -= speed
    }

    fun draw(canvas: Canvas) {
        val paint = Paint().apply { color = Color.RED }
        canvas.drawCircle(x, y, radius, paint)
    }

    fun getRect(): Rect {
        return Rect(
            (x - radius).toInt(),
            (y - radius).toInt(),
            (x + radius).toInt(),
            (y + radius).toInt()
        )
    }
}
