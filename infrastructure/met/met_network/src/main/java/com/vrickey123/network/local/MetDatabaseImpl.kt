package com.vrickey123.network.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vrickey123.met_api.MetObject

@Database(entities = [com.vrickey123.met_api.MetObject::class], version = 1, exportSchema = false)
abstract class MetDatabaseImpl : RoomDatabase(), MetDatabase {
    abstract override fun metObjectDAO(): MetObjectDAO

    companion object {
        const val DATABASE_NAME = "met-showcase.db"
        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        fun buildDatabase(context: Context): MetDatabaseImpl {
            return Room.databaseBuilder(context, MetDatabaseImpl::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            /*val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                .build()
                            WorkManager.getInstance(context).enqueue(request)*/
                        }
                    }
                )
                .build()
        }
    }
}