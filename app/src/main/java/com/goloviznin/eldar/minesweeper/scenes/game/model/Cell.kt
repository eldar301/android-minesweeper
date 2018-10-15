package com.goloviznin.eldar.minesweeper.scenes.game.model

sealed class Cell {
    class Free(val countOfBombsAround: Int): Cell()
    class Bomb : Cell()
    class Unknown : Cell()
}