package com.goloviznin.eldar.minesweeper.interactor

import android.os.Handler

interface TimerInteractorDelegate {
    fun onTimerUpdate(secondsElapsed: Int)
}

interface TimerInteractor {

    var delegate: TimerInteractorDelegate?

    fun startTimer()
    fun pauseTimer()
    fun resumeTimer()
    fun stopTimer()
}

class TimerInteractorDefault: TimerInteractor {

    private val timerPeriod: Long = 1000

    override var delegate: TimerInteractorDelegate? = null

    private var secondsElapsed = 0

    private var handler = Handler()

    private val task = object: Runnable {
        override fun run() {
            secondsElapsed++
            delegate?.onTimerUpdate(secondsElapsed)
            handler.postDelayed(this, timerPeriod)
        }
    }

    override fun startTimer() {
        stopTimer()
        delegate?.onTimerUpdate(secondsElapsed)
        handler.postDelayed(task, timerPeriod)
    }

    override fun pauseTimer() {
        handler.removeCallbacks(task)
    }

    override fun resumeTimer() {
        handler.postDelayed(task, timerPeriod)
    }

    override fun stopTimer() {
        secondsElapsed = 0
        handler.removeCallbacks(task)
    }

}