package com.trisys.rn.baseapp.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.trisys.rn.baseapp.database.model.NotificationItem

@Database(entities = arrayOf( NotificationItem::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val notificationDao: NotificationDao

    companion object {

        val DB_NAME = "upmyranks"
        private var INSTANCE: AppDatabase? = null
        private val sLock = Any()

        fun getInstance(context: Context): AppDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, DB_NAME
                    )
                            .addMigrations()
                            .allowMainThreadQueries()
                            .build()
                }
                return INSTANCE
            }
        }
    }

}
