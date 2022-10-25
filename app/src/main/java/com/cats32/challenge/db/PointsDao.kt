package com.cats32.challenge.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PointsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addPoint(point: DbPoint)

    @Query("SELECT * FROM point ORDER BY x")
    abstract fun getPointsFlow(): Flow<List<DbPoint>>

    @Query("DELETE FROM point")
    abstract fun clearPoints(): Int

}
