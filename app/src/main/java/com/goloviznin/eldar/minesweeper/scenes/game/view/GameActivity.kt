package com.goloviznin.eldar.minesweeper.scenes.game.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import com.goloviznin.eldar.minesweeper.R
import com.goloviznin.eldar.minesweeper.scenes.game.model.Cell
import com.goloviznin.eldar.minesweeper.scenes.game.presenter.GamePresenter
import com.goloviznin.eldar.minesweeper.scenes.game.presenter.GamePresenterDefault
import com.goloviznin.eldar.minesweeper.scenes.game.presenter.GameView
import com.goloviznin.eldar.minesweeper.scenes.rating.view.RatingActivity
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameViewDelegate, SeekBar.OnSeekBarChangeListener, GameView {

    private val minFieldSize = 4
    private val maxFieldSize = 20
    private val minBombsCount = 2
    private val fieldSizeToBombsCountRate = 4

    private var presenter: GamePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameView.delegate = this

        presenter = GamePresenterDefault()
        presenter?.view = this

        fieldSizeSeekBar.max = maxFieldSize - minFieldSize
        fieldSizeSeekBar.progress = 0
        handleFieldSizeSeekBarProgressChange()

        bombsCountSeekBar.progress = 0
        handleBombsCountSeekBarProgressChange()

        fieldSizeSeekBar.setOnSeekBarChangeListener(this)
        bombsCountSeekBar.setOnSeekBarChangeListener(this)
    }

    override fun onResume() {
        super.onResume()

        presenter?.viewOnResume()
    }

    override fun onPause() {
        super.onPause()

        presenter?.viewOnPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent = Intent(baseContext, RatingActivity::class.java)
        startActivity(intent)

        return true
    }

    override fun didTapOnCellWithId(id: Int) {
        presenter?.open(id)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar == fieldSizeSeekBar) {
            handleFieldSizeSeekBarProgressChange()
        } else if (seekBar == bombsCountSeekBar) {
            handleBombsCountSeekBarProgressChange()
        }
    }

    private fun handleFieldSizeSeekBarProgressChange() {
        val newSize = fieldSizeSeekBar.progress + minFieldSize
        fieldSizeTextView.text = String.format(resources.getString(R.string.fieldSizeTitle), newSize)
        bombsCountSeekBar.max = newSize * newSize / fieldSizeToBombsCountRate - minBombsCount
    }

    private fun handleBombsCountSeekBarProgressChange() {
        val newCount = bombsCountSeekBar.progress + minBombsCount
        bombsCountTextView.text = String.format(resources.getString(R.string.bombsCountTitle), newCount)
    }

    fun newGame(view: View? = null) {
        presenter?.startNewGame(fieldSizeSeekBar.progress + minFieldSize, bombsCountSeekBar.progress + minBombsCount)
    }

    override fun gameStarted(fieldSize: Int, numberOfBombs: Int, field: Array<Cell>) {
        gameView.fieldSize = fieldSize
        gameView.field = field
        actionButton.text = resources.getString(R.string.restartButtonTitle)
    }

    override fun fieldChanged(field: Array<Cell>) {
        gameView.field = field
    }

    override fun win() {
        actionButton.text = resources.getString(R.string.winButtonTitle)
    }

    override fun lose() {
        actionButton.text = resources.getString(R.string.loseButtonTitle)
    }

    override fun timerUpdate(seconds: Int) {
        timerTextView.text = "$seconds"
    }

}

