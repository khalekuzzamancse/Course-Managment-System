package com.kzcse.cms.core.data_src.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Entity(tableName = "courses")
@TypeConverters(CourseConverters::class)
@Serializable
data class CourseSchema(
    @PrimaryKey val id: String,
    val name: String,
    val descriptionShort: String,
    val instructor: InstructorSchema,
    val durationWeeks: Int,
    val priceUsd: Double,
    val isPremium: Boolean,
    val tags: String,
    val rating: Double,
    val enrolled: Boolean
)


@Serializable
data class InstructorSchema(
    val name: String,
    val expertiseLevel: String
)

class CourseConverters {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun fromInstructor(instructor: InstructorSchema): String = json.encodeToString(instructor)

    @TypeConverter
    fun toInstructor(value: String): InstructorSchema = json.decodeFromString(value)

    @TypeConverter
    fun fromTags(tags: List<String>): String = json.encodeToString(tags)

    @TypeConverter
    fun toTags(value: String): List<String> = json.decodeFromString(value)
}