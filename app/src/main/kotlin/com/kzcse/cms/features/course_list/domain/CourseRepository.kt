package com.kzcse.cms.features.course_list.domain

interface  CourseRepository {
    suspend fun readOrThrow(): List<CourseModel>
    suspend fun searchOrThrow(): List<CourseModel>
    suspend fun detailsOrThrow(id: String): CourseModel
}

data class CourseModel(
    val id: String,
    val name: String,
    val descriptionShort: String,
    val instructor: InstructorModel,
    val durationWeeks: Int,
    val priceUsd: Double,
    val isPremium: Boolean,
    val tags: List<String>,
    val rating: Double
)

data class InstructorModel(
    val name: String,
    val expertiseLevel: String
)
