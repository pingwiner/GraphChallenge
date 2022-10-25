package com.cats32.challenge.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


object DatabaseBuilder {
    const val DATABASE_NAME = "my_room_database"

    val MIGRATIONS = arrayOf(
        MIGRATION_0_1
    )

    @JvmStatic
    fun build(application: Context): MyDatabase {
        return Room.databaseBuilder(application, MyDatabase::class.java, DATABASE_NAME)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .addMigrations(*MIGRATIONS)
            .build()
    }
}