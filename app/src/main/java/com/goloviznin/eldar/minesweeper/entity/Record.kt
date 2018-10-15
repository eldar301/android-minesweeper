package com.goloviznin.eldar.minesweeper.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "records")
data class Record(@ColumnInfo(name = "field_size") var fieldSize: Int,
                  @ColumnInfo(name = "bombs_count") var bombsCount: Int,
                  @ColumnInfo(name = "time") var time: Int) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}