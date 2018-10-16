package com.goloviznin.eldar.minesweeper.scenes.game.model

import java.util.*
import kotlin.collections.HashSet
import kotlin.math.max
import kotlin.math.min

class MineSweeper(val size: Int, val bombsCount: Int) {

    var state = GameState.ACTIVE
        private set

    private var openedCells = 0

    private var bombsField = BooleanArray(0)
    var openedField: Array<Cell>
        private set

    init {
        openedField = Array(size * size) { _ -> Cell.Unknown() }
    }

    private fun generateField(openedIndex: Int) {
        bombsField = BooleanArray(size * size)
        val random = Random()

        val (openedX, openedY) = indices(openedIndex)
        val prohibitedIndices = HashSet<Int>(9)

        for (xNext in max(openedX - 1, 0) .. min(openedX + 1, size - 1)) {
            for (yNext in max(openedY - 1, 0) .. min(openedY + 1, size - 1)) {
                prohibitedIndices.add(index(xNext, yNext))
            }
        }

        var numberOfSpawnedBombs = 0
        while (numberOfSpawnedBombs != bombsCount) {
            val randomIndex = random.nextInt(size * size)
            if (!bombsField[randomIndex] && !prohibitedIndices.contains(randomIndex)) {
                numberOfSpawnedBombs++
                bombsField[randomIndex] = true
            }
        }
    }

    private fun countNeighborBombs(index: Int): Int {
        var counter = 0

        val (x, y) = indices(index)
        for (xNext in max(x - 1, 0) .. min(x + 1, size - 1)) {
            for (yNext in max(y - 1, 0) .. min(y + 1, size - 1)) {
                if (bombsField[index(xNext, yNext)]) {
                    counter++
                }
            }
        }

        return counter
    }

    private fun indices(index: Int): Pair<Int, Int> {
        val x = index % size
        val y = index / size
        return Pair(x, y)
    }

    private fun index(x: Int, y: Int): Int {
        return y * size + x
    }

    private fun isWithinBounds(index: Int): Boolean {
        return index >= 0 && index < size * size
    }

    fun open(index: Int): Boolean {
        if (state != GameState.ACTIVE || !isWithinBounds(index) || openedField[index] !is Cell.Unknown) {
            return false
        }

        if (bombsField.isEmpty()) {
            generateField(index)
        }

        if (bombsField[index]) {
            for ((index, bomb) in bombsField.withIndex()) {
                if (bomb) {
                    openedField[index] = Cell.Bomb()
                }
            }

            state = GameState.LOSE

            return true
        }

        openedField[index] = Cell.Free(countNeighborBombs(index))

        if (countNeighborBombs(index) == 0) {
            val (x, y) = indices(index)
            for (xNext in max(x - 1, 0) .. min(x + 1, size - 1)) {
                for (yNext in max(y - 1, 0) .. min(y + 1, size - 1)) {
                    val index = index(xNext, yNext)
                    if (openedField[index] !is Cell.Unknown) {
                        continue
                    }

                    open(index(xNext, yNext))
                }
            }
        }

        openedCells++
        if (size * size - openedCells == bombsCount) {
            state = GameState.WIN
        }

        return true
    }


}