package com.goloviznin.eldar.minesweeper

import android.app.Application
import android.arch.persistence.room.Room
import com.goloviznin.eldar.minesweeper.database.AppDatabase

class MinesweeperApplication: Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        MinesweeperApplication.database = Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "minesweeper-db")
                .allowMainThreadQueries()
                .build()
    }
}