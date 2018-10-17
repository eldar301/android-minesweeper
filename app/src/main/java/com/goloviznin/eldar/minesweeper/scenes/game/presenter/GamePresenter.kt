package com.goloviznin.eldar.minesweeper.scenes.game.presenter

import com.goloviznin.eldar.minesweeper.entity.Record
import com.goloviznin.eldar.minesweeper.interactor.DatabaseInteractorDefault
import com.goloviznin.eldar.minesweeper.interactor.TimerInteractorDefault
import com.goloviznin.eldar.minesweeper.interactor.TimerInteractorDelegate
import com.goloviznin.eldar.minesweeper.scenes.game.model.Cell
import com.goloviznin.eldar.minesweeper.scenes.game.model.GameState
import com.goloviznin.eldar.minesweeper.scenes.game.model.MineSweeper

interface GameView {
    fun gameStarted(fieldSize: Int, numberOfBombs: Int, field: Array<Cell>)
    fun fieldChanged(field: Array<Cell>)
    fun win()
    fun lose()
    fun timerUpdate(seconds: Int)
}

interface GamePresenter {

    var view: GameView?

    fun viewOnResume()
    fun viewOnPause()

    fun startNewGame(fieldSize: Int, numberOfBombs: Int)
    fun open(index: Int)
}

class GamePresenterDefault : GamePresenter, TimerInteractorDelegate {

    private val maxNumberOfRecords = 20

    private var game: MineSweeper? = null

    private var currentGameDuration = 0
    private var currentGameFieldSize = 0
    private var currentGameBombsCount = 0

    private val timerInteractor = TimerInteractorDefault()

    private val databaseInteractor = DatabaseInteractorDefault()

    override var view: GameView? = null

    init {
        timerInteractor.delegate = this
    }

    override fun viewOnResume() {
        game?.let {
            if (it.state == GameState.ACTIVE) {
                timerInteractor.resumeTimer()
            }
        }
    }

    override fun viewOnPause() {
        game?.let {
            if (it.state == GameState.ACTIVE) {
                timerInteractor.pauseTimer()
            }
        }
    }

    override fun startNewGame(fieldSize: Int, numberOfBombs: Int) {
        currentGameDuration = 0
        currentGameFieldSize = fieldSize
        currentGameBombsCount = numberOfBombs

        timerInteractor.startTimer()

        game = MineSweeper(fieldSize, numberOfBombs)
        view?.gameStarted(game!!.size, game!!.bombsCount, game!!.openedField)
    }

    override fun open(index: Int) {
        game?.let { game ->
            if (game.open(index)) {
                view?.fieldChanged(game.openedField)
            }

            when (game.state) {
                GameState.WIN -> {
                    view?.win()
                    timerInteractor.stopTimer()
                    updateRecord()
                }
                GameState.LOSE -> {
                    view?.lose()
                    timerInteractor.stopTimer()
                }
                GameState.ACTIVE -> return
            }
        }
    }

    override fun onTimerUpdate(secondsElapsed: Int) {
        currentGameDuration = secondsElapsed
        view?.timerUpdate(currentGameDuration)
    }

    private fun updateRecord() {
        val currentProbableRecord = Record(currentGameFieldSize, currentGameBombsCount, currentGameDuration)
        if (databaseInteractor.count != maxNumberOfRecords) {
            databaseInteractor.add(currentProbableRecord)
        } else {
            val worstRecord = databaseInteractor.fetchRecordWithMaxTime()!!
            if (currentProbableRecord.time < worstRecord.time) {
                databaseInteractor.delete(worstRecord)
                databaseInteractor.add(currentProbableRecord)
            }
        }
    }

}