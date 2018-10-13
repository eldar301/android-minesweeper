package com.goloviznin.eldar.minesweeper.scenes.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.goloviznin.eldar.minesweeper.R
import kotlin.properties.Delegates

/**
 * TODO: document your custom view class.
 */

interface GameViewDelegate {
    fun didTapOnCellWithId(id: Int)
}

class GameView : View {

    var delegate: GameViewDelegate? = null

    var fieldSize: Int by Delegates.observable(
            initialValue = 4,
            onChange = { _, _, _ ->
                this.invalidate()
            }
    )

    private var drawnCorrectly = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (fieldSize < 1) {
            drawnCorrectly = false
            return
        }

        val fillPaint = Paint()
        fillPaint.color = ContextCompat.getColor(context, R.color.gameViewBackgroundColor)
        fillPaint.style = Paint.Style.FILL

        val splitPaint = Paint()
        splitPaint.color = ContextCompat.getColor(context, R.color.gameViewSplitColor)
        splitPaint.strokeWidth = context.resources.getInteger(R.integer.gameViewSplitLineWidth).toFloat()

        canvas.drawPaint(fillPaint)

        val cellSize = width.toFloat() / fieldSize.toFloat()

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
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                if (!drawnCorrectly) {
                    return true
                }

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
