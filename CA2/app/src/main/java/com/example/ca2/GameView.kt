package com.example.ca2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.ca2.objects.Bullet
import com.example.ca2.objects.Enemy
import com.example.ca2.objects.Plane

class GameView(context: Context, private val sensorHandler: SensorHandler) : SurfaceView(context), SurfaceHolder.Callback {

    private val thread = GameLoopThread(this)
    private val plane = Plane(context)
    private val bullets = mutableListOf<Bullet>()
    private val enemies = mutableListOf<Enemy>()

    private var score = 0
    private var gameOver = false

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
     }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        if (gameOver) return

        plane.update(sensorHandler)

        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            bullet.update()

            val hitEnemies = enemies.filter { Rect.intersects(it.getRect(), bullet.getRect()) }
            if (hitEnemies.isNotEmpty()) {
                bulletIterator.remove()
                enemies.removeAll(hitEnemies)
                score += hitEnemies.size * 10
            }
        }

        enemies.forEach {
            it.update()
            if (Rect.intersects(it.getRect(), plane.getRect())) {
                gameOver = true
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.BLACK)

        plane.draw(canvas)
        bullets.forEach { it.draw(canvas) }
        enemies.forEach { it.draw(canvas) }

        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 60f
            isAntiAlias = true
        }
        canvas.drawText("Score: $score", 50f, 100f, paint)

        if (gameOver) {
            paint.color = Color.RED
            paint.textSize = 100f
            canvas.drawText("Game Over", width / 4f, height / 2f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val bullet = Bullet(plane.x + plane.getBitmap().width / 2f, plane.y)
            bullets.add(bullet)

            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
