package com.example.ca2
import com.example.ca2.GameView

class GameLoopThread(private val gameView: GameView) : Thread() {
    private var running = false
    fun setRunning(isRunning: Boolean) { running = isRunning }

    override fun run() {
        while (running) {
            val canvas = gameView.holder.lockCanvas()
            if (canvas != null) {
                synchronized(gameView.holder) {
                    gameView.update()
                    gameView.draw(canvas)
                }
                gameView.holder.unlockCanvasAndPost(canvas)
            }
            sleep(16) // ~60 FPS
        }
    }
}