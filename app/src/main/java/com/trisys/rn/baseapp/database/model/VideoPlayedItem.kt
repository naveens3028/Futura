package com.trisys.rn.baseapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "VideoItems")
data class VideoPlayedItem(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "videoUrl")
    @SerializedName("videoUrl") var videoUrl: String,

    @ColumnInfo(name = "lastPlayed")
    @SerializedName("lastPlayed") var lastPlayed: String,

    @ColumnInfo(name = "logoImg")
    @SerializedName("logoImg") var logoImg: String,

    )
