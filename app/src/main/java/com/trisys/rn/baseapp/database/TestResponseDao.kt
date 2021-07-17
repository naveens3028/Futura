package com.trisys.rn.baseapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trisys.rn.baseapp.model.TestResultsModel


@Dao
interface TestResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addResult(sectionItem: TestResultsModel)

    @Query("SELECT * FROM ResultReview")
    fun getAll(): MutableList<TestResultsModel>

    @Query("SELECT * FROM ResultReview where testPaperId=:testPaperId")
    fun getResponse(testPaperId: String): TestResultsModel

}