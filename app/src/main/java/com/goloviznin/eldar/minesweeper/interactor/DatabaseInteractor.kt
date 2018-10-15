package com.goloviznin.eldar.minesweeper.interactor

import com.goloviznin.eldar.minesweeper.MinesweeperApplication
import com.goloviznin.eldar.minesweeper.database.DatabaseDao
import com.goloviznin.eldar.minesweeper.entity.Record

interface DatabaseInteractor {
    val records: List<Record>
    val count: Int
    fun fetchRecordWithMaxTime(): Record?
    fun delete(record: Record)
    fun add(record: Record)
}

class DatabaseInteractorDefault(private val dao: DatabaseDao = MinesweeperApplication.database!!.databaseDao()): DatabaseInteractor {

    override val records: List<Record>
        get() = dao.fetchAll()

    override val count: Int
        get() = dao.numberOfRecords()

    override fun fetchRecordWithMaxTime(): Record? {
        return dao.fetchRecordsWithMaxTime().firstOrNull()
    }

    override fun delete(record: Record) {
        dao.delete(record)
    }

    override fun add(record: Record) {
        dao.add(record)
    }
}