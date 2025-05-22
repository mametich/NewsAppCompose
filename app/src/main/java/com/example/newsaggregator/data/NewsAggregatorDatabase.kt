package com.example.newsaggregator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATA_BASE = "News_aggregator_data_base"

@Database(entities = [NewsEntity::class], version = 4, exportSchema = false)
abstract class NewsAggregatorDatabase : RoomDatabase() {

    abstract fun newsAggregatorDao() : NewsAggregatorDao

    companion object {
        @Volatile
        private var INSTANCE : NewsAggregatorDatabase? = null

        fun getDatabase(context: Context) : NewsAggregatorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsAggregatorDatabase::class.java,
                    DATA_BASE
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}