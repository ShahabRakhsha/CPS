package com.example.ca2

class KalmanFilter(private val Q: Float = 0.001f, private val R: Float = 0.1f) {
    private var x = 0f     // estimated angle
    private var P = 1f     // estimation error covariance
    private var K: Float = 0.0f   // Kalman gain

    fun update(measurement: Float): Float {
        P += Q
        K = P / (P + R)
        x += K * (measurement - x)
        P *= (1 - K)

        return x
    }
}
