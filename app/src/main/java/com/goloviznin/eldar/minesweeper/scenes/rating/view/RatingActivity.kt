package com.goloviznin.eldar.minesweeper.scenes.rating.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.goloviznin.eldar.minesweeper.R
import com.goloviznin.eldar.minesweeper.entity.Record
import com.goloviznin.eldar.minesweeper.scenes.rating.presenter.RatingView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.TableRow
import com.goloviznin.eldar.minesweeper.scenes.rating.presenter.RatingPresenter
import com.goloviznin.eldar.minesweeper.scenes.rating.presenter.RatingPresenterDefault
import kotlinx.android.synthetic.main.activity_rating.*


class RatingActivity : AppCompatActivity(), RatingView {

    private var presenter: RatingPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        presenter = RatingPresenterDefault()
        presenter?.view = this
    }

    override fun setup(records: List<Record>) {
        fun createTextViewForTable(): TextView {
            val textView = TextView(this)
            textView.gravity = Gravity.CENTER
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.recordViewFontSize))
            return textView
        }

        for (record in records) {
            val fieldSizeView = createTextViewForTable()
            fieldSizeView.text = "${record.fieldSize} x ${record.fieldSize}"

            val bombsCountView = createTextViewForTable()
            bombsCountView.text = record.bombsCount.toString()

            val timeView = createTextViewForTable()
            timeView.text = record.time.toString()

            val tableRow = TableRow(this)
            tableRow.addView(fieldSizeView)
            tableRow.addView(bombsCountView)
            tableRow.addView(timeView)

            tableLayout.addView(tableRow)

            val splitView = View(this)
            splitView.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1)
            splitView.setBackgroundColor(Color.GRAY)

            tableLayout.addView(splitView)
        }
    }

}
