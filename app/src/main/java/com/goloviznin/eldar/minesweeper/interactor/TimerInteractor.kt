package com.goloviznin.eldar.minesweeper.interactor

import android.os.Handler
import java.util.*

interface TimerInteractorDelegate {
    fun onTimerUpdate(secondsElapsed: Int)
}

interface TimerInteractor {

    var delegate: TimerInteractorDelegate?

    fun startTimer()
    fun stopTimer()
}

class TimerInteractorDefault: TimerInteractor {

    private val timerPeriod: Long = 1000

    override var delegate: TimerInteractorDelegate? = null

    private var startedAt: Long = 0

    private var handler = Handler()

    private val task = object: Runnable {
        override fun run() {
            val currentTime = Calendar.getInstance().timeInMillis
            val secondsElapsed = ((currentTime - startedAt) / 1000).toInt()
            delegate?.onTimerUpdate(secondsElapsed)
            handler.postDelayed(this, timerPeriod)
        }
    }

    override fun startTimer() {
        startedAt = Calendar.getInstance().timeInMillis
        handler.postDelayed(task, 0)
    }

    override fun stopTimer() {
        handler.removeCallbacks(task)
    }

}