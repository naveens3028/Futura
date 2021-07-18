package com.trisys.rn.baseapp.database.dao

import androidx.room.*
import com.trisys.rn.baseapp.database.model.NotificationItem
import com.trisys.rn.baseapp.model.CompletedTest
import com.trisys.rn.baseapp.model.Quesion
import com.trisys.rn.baseapp.model.TestPaperVo

@Dao
interface CompletedTestDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(completedTest: CompletedTest): Long
    @Update
    fun update(completedTest: CompletedTest)

    @Delete
    fun delete(completedTest: CompletedTest)

    @Query("SELECT * FROM completed_test ORDER BY id ASC")
    fun getAll(): List<CompletedTest>

//    @Transaction
//    @Query("SELECT * FROM completed_test WHERE testPaperId = :id")
//    fun getQuestion(id: String): MutableList<CompletedTest>

//    @Transaction
//    @Query("UPDATE completed_test SET answer=:answer WHERE id = :id")
//    fun updateQuestion(id: String, answer: String?)

}