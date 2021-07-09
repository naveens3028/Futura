package com.trisys.rn.baseapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "Person")
class DataModel {
    @ColumnInfo(name = "name")
    private var name: String? = null

    @ColumnInfo(name = "age")
    private var age: String? = null
}
