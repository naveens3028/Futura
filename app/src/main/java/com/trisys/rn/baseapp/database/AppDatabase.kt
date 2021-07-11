package com.trisys.rn.baseapp.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trisys.rn.baseapp.database.model.NotificationItem
import com.trisys.rn.baseapp.model.onBoarding.AverageBatchTests

@Database(entities = [ NotificationItem::class,  AverageBatchTests::class ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val notificationDao: NotificationDao
    abstract val averageBatchDao: AverageBatchDao

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
