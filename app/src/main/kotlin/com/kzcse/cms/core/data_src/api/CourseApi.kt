package com.kzcse.cms.core.data_src.api

interface CourseApi {
    suspend fun readOrThrow(): List<CourseEntity>
    suspend fun searchOrThrow(): List<CourseEntity>
    suspend fun detailsOrThrow(id: Int): CourseEntity
}
data class CourseEntity(
    val id: String,
    val name: String,
    val descriptionShort: String,
    val instructor: InstructorEntity,
    val durationWeeks: Int,
    val priceUsd: Double,
    val isPremium: Boolean,
    val tags: List<String>,
    val rating: Double
)

data class InstructorEntity(
    val name: String,
    val expertiseLevel: String
)
