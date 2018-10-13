package com.goloviznin.eldar.minesweeper.scenes.game.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.goloviznin.eldar.minesweeper.R

class GameActivity : AppCompatActivity(), GameViewDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameView: GameView = this.findViewById(R.id.gameView)
        gameView.delegate = this
        gameView.fieldSize = 8
    }

    override fun didTapOnCellWithId(id: Int) {
        Log.d("MYTAG", "${id}")
    }

}

