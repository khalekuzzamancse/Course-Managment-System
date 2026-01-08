package com.kzcse.cms.core.data_src.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Database(entities = [CourseSchema::class], version = 1)
    @TypeConverters(CourseConverters::class)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun courseDao(): CourseDao
    }
    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseSchema>>

    @Query("""
    SELECT * FROM courses 
    WHERE name LIKE '%' || :query || '%'
       OR tags LIKE '%' || :query || '%'
""")
    suspend fun searchCourses(query: String): List<CourseSchema>


    @Upsert
    suspend fun insertCourses(courses: List<CourseSchema>)
    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    suspend fun getCourseById(courseId: String): CourseSchema?
    @Query("""
    UPDATE courses
    SET enrolled = 1
    WHERE id = :courseId
""")
    suspend fun enrollCourse(courseId: String): Int
}
