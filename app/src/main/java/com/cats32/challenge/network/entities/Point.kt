package com.cats32.challenge.network.entities

import com.cats32.challenge.db.DbPoint


data class Point(
    var x: Double,
    var y: Double
) {
    fun convertToDbInstance(): DbPoint {
        return DbPoint(x, y)
    }
}