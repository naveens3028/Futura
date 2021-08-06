package com.trisys.rn.baseapp.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trisys.rn.baseapp.database.dao.CompletedTestDAO
import com.trisys.rn.baseapp.database.dao.TestDAO
import com.trisys.rn.baseapp.database.model.NotificationItem
import com.trisys.rn.baseapp.model.*
import com.trisys.rn.baseapp.model.onBoarding.AverageBatchTests

@Database(
    entities = [NotificationItem::class, AverageBatchTests::class, TestPaperVo::class, Quesion::class, TestResultsModel::class, CompletedTest::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(SectionsDatumConverter::class, ListTopRankerConverter::class, SectionQuestionConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val notificationDao: NotificationDao
    abstract val averageBatchDao: AverageBatchDao
    abstract val resultsDao: TestResponseDao
    abstract val testDAO: TestDAO
    abstract val completedTestDAO: CompletedTestDAO

    companion object {

        val DB_NAME = "upmyranks"
        private var INSTANCE: AppDatabase? = null
        private val sLock = Any()

        fun getInstance(context: Context): AppDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
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