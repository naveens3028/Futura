package com.trisys.rn.baseapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trisys.rn.baseapp.database.model.DataModel

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun adds(sectionItem: DataModel)

    @Query("SELECT * FROM person")
    fun getAll(): MutableList<DataModel>

}