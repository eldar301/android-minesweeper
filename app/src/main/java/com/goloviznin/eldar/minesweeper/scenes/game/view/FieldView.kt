package com.goloviznin.eldar.minesweeper.scenes.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.goloviznin.eldar.minesweeper.R
import com.goloviznin.eldar.minesweeper.scenes.game.model.Cell
import kotlin.properties.Delegates

interface GameViewDelegate {
    fun didTapOnCellWithId(id: Int)
}

class FieldView : View {

    var delegate: GameViewDelegate? = null

    var fieldSize: Int by Delegates.observable(
            initialValue = 0,
            onChange = { _, _, _ ->
                this.invalidate()
            }
    )

    var field: Array<Cell> by Delegates.observable(
            initialValue = arrayOf(),
            onChange = { _, _, _ ->
                this.invalidate()
            }
    )

    private val backgroundPaint: Paint by lazy {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.gameViewBackgroundColor)
        paint.style = Paint.Style.FILL
        return@lazy paint
    }

    private val colorsForNumberOfBombs: IntArray by lazy {
        return@lazy resources.getIntArray(R.array.colorsForNumberOfBombs)
    }

    private val unknownCellPaint: Paint by lazy {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.gameViewUnknownCellColor)
        return@lazy paint
    }

    private val splitPaint: Paint by lazy {
        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.gameViewSplitColor)
        paint.strokeWidth = context.resources.getInteger(R.integer.gameViewSplitLineWidth).toFloat()
        return@lazy paint
    }

    private val textPaint: TextPaint by lazy {
        val paint = TextPaint()
        paint.textAlign = Paint.Align.CENTER
        return@lazy paint
    }

    private var drawnCorrectly = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (fieldSize < 1) {
            drawnCorrectly = false
            return
        }

        canvas.drawPaint(backgroundPaint)

        val cellSize = width.toFloat() / fieldSize.toFloat()

        textPaint.textSize = cellSize

        for ((index, cell) in field.withIndex()) {
            var text = ""

            val x = index % fieldSize
            val y = index / fieldSize

            when (cell) {
                is Cell.Unknown -> {
                    text = ""
                    canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, unknownCellPaint)
                }

                is Cell.Free -> {
                    if (cell.countOfBombsAround != 0) {
                        text = "${cell.countOfBombsAround}"
                        textPaint.color = colorsForNumberOfBombs[cell.countOfBombsAround - 1]
                    }
                }

                is Cell.Bomb -> {
                    text = "*"
                    textPaint.color = ContextCompat.getColor(context, R.color.black)
                }
            }

            canvas.drawText(text, cellSize / 2.0f + x * cellSize, cellSize * y + cellSize / 2.0f - (textPaint.descent() + textPaint.ascent()) / 2, textPaint)
        }

        for (lineId in 0..fieldSize) {
            val lineOffset = cellSize * lineId
            val horizontalLinePoints = floatArrayOf(
                    0.0f,
                    lineOffset,
                    width.toFloat(),
                    lineOffset)
            val verticalLinePoints = floatArrayOf(
                    lineOffset,
                    0.0f,
                    lineOffset,
                    width.toFloat())

            canvas.drawLines(horizontalLinePoints, splitPaint)
            canvas.drawLines(verticalLinePoints, splitPaint)
        }

        drawnCorrectly = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!drawnCorrectly) {
            return true
        }

        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                val cellSize = width.toFloat() / fieldSize.toFloat()
                val rowId = (event.y / cellSize).toInt()
                val columnId = (event.x / cellSize).toInt()
                val cellId = rowId * fieldSize + columnId
                delegate?.didTapOnCellWithId(cellId)
            }
        }

        return true
    }

}
