package com.kzcse.cms.core.data_src.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kzcse.cms.core.language.CustomException
import com.kzcse.cms.core.language.Logger

@Database(entities = [CourseSchema::class], version = 1)
@TypeConverters(CourseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}

object RoomDbProvider {
    private var db: AppDatabase? = null
fun dbOrThrow()=db ?: throw CustomException(message = "Database not initialized", debugMessage = "src=RoomDbProvider")
    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "courses_db"
        ).build()
        Logger.on(context="RoomDbProvider","db","$db")
    }

}
