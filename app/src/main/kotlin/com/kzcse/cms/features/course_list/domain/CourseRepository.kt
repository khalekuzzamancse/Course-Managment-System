package com.kzcse.cms.features.course_list.domain

import kotlinx.coroutines.flow.Flow

interface  CourseRepository {
    suspend fun readOrThrow(): Flow<List<CourseModel>>
    suspend fun searchOrThrow(query: String): List<CourseModel>
    suspend fun detailsOrThrow(id: String): CourseModel
    suspend fun enrollOrThrow(id: String)
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
    val rating: Double,
    val isEnrolled: Boolean
)

data class InstructorModel(
    val name: String,
    val expertiseLevel: String
)
