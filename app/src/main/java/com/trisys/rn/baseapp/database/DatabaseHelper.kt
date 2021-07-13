package com.trisys.rn.baseapp.database

import android.content.Context
import android.os.AsyncTask
import com.trisys.rn.baseapp.database.model.NotificationItem
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.MergedTest
import com.trisys.rn.baseapp.model.TestPaperVo
import com.trisys.rn.baseapp.model.onBoarding.AverageBatchTests

class DatabaseHelper(context: Context) {
    private var db: AppDatabase? = null

    init {
        db = AppDatabase.getInstance(context)
    }

    fun saveAvg(notificationItem: AverageBatchTests) {
        db!!.averageBatchDao.addAvg(notificationItem)
    }

    fun getAllAverageBatchTest(): MutableList<AverageBatchTests> {
        return db!!.averageBatchDao.getAll()
    }

    fun saveNotificationItem(notificationItem: NotificationItem): Long {
        var notificationID: Long = 0

        object : AsyncTask<NotificationItem, Void, Long>() {
            override fun doInBackground(vararg params: NotificationItem): Long {
                if (params != null) {
                    val saveFavNews = params[0]
                    if (saveFavNews.notifyID > 0) {
                        db!!.notificationDao.update(notificationItem)
                    } else {
                        notificationID = db!!.notificationDao.add(notificationItem)
                    }
                } else {
                    notificationID = db!!.notificationDao.add(notificationItem)
                }
                return notificationID
            }


        }.execute(notificationItem)

        return notificationID
    }

    fun getAllNotification(): List<NotificationItem> {
        return db!!.notificationDao.getAll()
    }

    fun getAllUnreadNotification(): List<NotificationItem> {
        return db!!.notificationDao.getAllUnreadNotification()
    }

    fun saveTestList(mockTest: MOCKTEST) {
        db!!.testDAO.addTest(mockTest)
    }

    fun saveTestPaper(testPaperVo: TestPaperVo) {
        db!!.testDAO.addTestPaper(testPaperVo)
    }

    fun deleteTestList() {
        db!!.testDAO.deleteTest()
    }

    fun getAllTest(): MutableList<MergedTest> {
        return db!!.testDAO.getAll()
    }
}