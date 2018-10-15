package com.goloviznin.eldar.minesweeper.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.goloviznin.eldar.minesweeper.entity.Record

@Database(entities = [Record::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}