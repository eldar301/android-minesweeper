package com.goloviznin.eldar.minesweeper.database

import android.arch.persistence.room.*
import com.goloviznin.eldar.minesweeper.entity.Record

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM records ORDER BY time ASC, field_size DESC, bombs_count DESC")
    fun fetchAll(): List<Record>

    @Query("SELECT * FROM records WHERE time = (SELECT MAX(time) FROM records) ORDER BY time DESC, field_size ASC, bombs_count ASC LIMIT 1")
    fun fetchRecordsWithMaxTime(): List<Record>

    @Query("SELECT COUNT(*) FROM records")
    fun numberOfRecords(): Int

    @Delete
    fun delete(record: Record)

    @Insert
    fun add(record: Record)

}