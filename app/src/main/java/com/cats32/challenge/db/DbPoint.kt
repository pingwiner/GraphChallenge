package com.cats32.challenge.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point")
data class DbPoint (
    @PrimaryKey
    @ColumnInfo(name = "x")
    var x: Double = 0.0,

    @ColumnInfo(name = "y")
    var y: Double = 0.0
)
