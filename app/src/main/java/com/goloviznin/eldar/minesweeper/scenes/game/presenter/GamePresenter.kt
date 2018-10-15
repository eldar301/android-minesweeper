package com.goloviznin.eldar.minesweeper.scenes.game.presenter

import com.goloviznin.eldar.minesweeper.scenes.game.interactor.TimerInteractorDefault
import com.goloviznin.eldar.minesweeper.scenes.game.interactor.TimerInteractorDelegate
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

    fun startNewGame(fieldSize: Int, numberOfBombs: Int)
    fun open(index: Int)
}

class GamePresenterDefault: GamePresenter, TimerInteractorDelegate {

    private var game: MineSweeper? = null

    val interactor = TimerInteractorDefault()

    override var view: GameView? = null

    override fun startNewGame(fieldSize: Int, numberOfBombs: Int) {
        interactor.delegate = this
        interactor.startTimer()

        game = MineSweeper(fieldSize, numberOfBombs)
        view?.gameStarted(game!!.size, game!!.bombsCount, game!!.openedField)
    }

    override fun open(index: Int) {
        game?.let { game ->
            if (game.open(index)) {
                view?.fieldChanged(game.openedField)
            }

            when (game.state) {
                GameState.WIN -> view?.win()
                GameState.LOSE -> view?.lose()
                GameState.ACTIVE -> return
            }
        }
    }

    override fun onTimerUpdate(secondsElapsed: Int) {
        view?.timerUpdate(secondsElapsed)
    }

}