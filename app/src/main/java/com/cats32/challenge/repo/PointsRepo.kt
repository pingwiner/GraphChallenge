package com.cats32.challenge.repo

import com.cats32.challenge.db.DbPoint
import com.cats32.challenge.db.MyDatabase
import com.cats32.challenge.db.PointsDao
import com.cats32.challenge.network.ProjectResources
import com.cats32.challenge.network.entities.Point
import com.cats32.challenge.network.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointsRepo @Inject constructor(
    private val pointsDao: PointsDao,
    private val projectResources: ProjectResources,
    private val database: MyDatabase
) {

    suspend fun fetchPoints(count: Int): Result<Boolean> = withContext(Dispatchers.IO) {
        val response = safeCall {
            projectResources.getPoints(count)
        }
        return@withContext if (response.isSuccess) {
            val points = response.body.points
            savePoints(points)
            Result.success(true)
        } else {
            Result.failure(response.throwable!!)
        }

    }

    private suspend fun savePoints(points: List<Point>) {
        database.runInTransaction {
            pointsDao.clearPoints()
            for (point in points) {
                pointsDao.addPoint(point.convertToDbInstance())
            }
        }
    }

    fun pointsAsFlow(): Flow<List<DbPoint>> {
        return pointsDao.getPointsFlow()
    }
}
