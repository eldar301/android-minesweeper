package com.goloviznin.eldar.minesweeper.scenes.rating.presenter

import com.goloviznin.eldar.minesweeper.entity.Record
import com.goloviznin.eldar.minesweeper.interactor.DatabaseInteractorDefault
import kotlin.properties.Delegates

interface RatingView {
    fun setup(records: List<Record>)
}

interface RatingPresenter {
    var view: RatingView?
}

class RatingPresenterDefault: RatingPresenter {

    private val databaseInteractor = DatabaseInteractorDefault()

    override var view: RatingView? by Delegates.observable<RatingView?>(
            initialValue = null,
            onChange = { _, _, _ ->
                view?.setup(databaseInteractor.records)
            }
    )

}