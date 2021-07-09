package com.trisys.rn.baseapp.database

import androidx.room.*
import com.trisys.rn.baseapp.database.model.NotificationItem
import com.trisys.rn.baseapp.database.model.User

@Dao
interface UserDao {

    @Update
    fun update(sectionItem: User)

    @Delete
    fun delete(sectionItem: User)

    @Query("SELECT * FROM user")
    fun getAll(): List<User>
}