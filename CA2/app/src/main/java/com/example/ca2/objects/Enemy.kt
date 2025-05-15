package com.example.ca2.objects

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.ca2.R

class Enemy(private val context: Context, var x: Float, var y: Float) {
    private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemyleft)
    private val speed = 5f

    fun update() {
        y += speed
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, x, y, null)
    }

    fun getRect(): Rect = Rect(x.toInt(), y.toInt(), x.toInt() + bitmap.width, y.toInt() + bitmap.height)
}
