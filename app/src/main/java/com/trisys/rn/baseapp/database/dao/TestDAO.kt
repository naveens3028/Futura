package com.trisys.rn.baseapp.database.dao

import androidx.room.*
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.MergedTest
import com.trisys.rn.baseapp.model.TestPaperVo

@Dao
interface TestDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTest(mockTest: MOCKTEST)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTestPaper(testPaperVo: TestPaperVo)

    @Query("DELETE FROM test_detail")
    fun deleteTest()

    @Transaction
    @Query("SELECT * FROM test_detail JOIN test_paper WHERE test_detail.testPaperId = test_paper.id")
    fun getAll(): MutableList<MergedTest>

}