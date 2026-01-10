@file:Suppress("FunctionName")
package com.kzcse.cms.core.data_src.remote
import com.kzcse.cms.core.data_src.api.CourseEntity
import com.kzcse.cms.core.data_src.api.CourseApi
import com.kzcse.cms.core.data_src.api.InstructorEntity
import com.kzcse.cms.core.language.Logger
import com.kzcse.cms.core.network.NetworkClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

//RDS=Remote data-source
abstract class CourseRDSTemplate: CourseApi {
    private val client = NetworkClient.createBase()
    private val tag=this.javaClass.simpleName.toString()
    abstract  fun parseOrThrow(response: String): List<CourseEntity>
    override suspend fun readOrThrow(): List<CourseEntity> {
        return _readOrThrow(method = "readOrThrow", url = URLFactory.urls.readCourse)
    }
    override suspend fun searchOrThrow(): List<CourseEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun detailsOrThrow(id: Int): CourseEntity {
        TODO("Not yet implemented")
    }
    private suspend fun _readOrThrow(method: String, url: String): List<CourseEntity> {
        Logger.on(tag, method, "url", url)
    val response: String = client.getOrThrow(url)
       // val response=fakeResponse
        Logger.on(tag, method, "response", response)
        return  parseOrThrow(response)
    }
}
class CourseRDSTemplateImpl: CourseRDSTemplate(){
    override fun parseOrThrow(response: String): List<CourseEntity> {
        val array = Json.parseToJsonElement(response).jsonArray

        return array.map { element ->
            val obj = element.jsonObject
            val instructorObj = obj["instructor"]!!.jsonObject

            CourseEntity(
                id = obj["course_id"]!!.jsonPrimitive.content,
                name = obj["title"]!!.jsonPrimitive.content,
                descriptionShort = obj["description_short"]!!.jsonPrimitive.content,
                instructor = InstructorEntity(
                    name = instructorObj["name"]!!.jsonPrimitive.content,
                    expertiseLevel = instructorObj["expertise_level"]!!.jsonPrimitive.content
                ),
                durationWeeks = obj["duration_weeks"]!!.jsonPrimitive.int,
                priceUsd = obj["price_usd"]!!.jsonPrimitive.double,
                isPremium = obj["is_premium"]!!.jsonPrimitive.boolean,
                tags = obj["tags"]!!.jsonArray.map { it.jsonPrimitive.content },
                rating = obj["rating"]!!.jsonPrimitive.double
            )
        }
    }
}

private val fakeResponse="""
    [
      {
        "course_id": "KOTLIN-001",
        "title": "Android App Development with Compose",
        "description_short": "Build modern Android apps from scratch using Kotlin and Jetpack Compose.",
        "instructor": { "name": "Prof. Anika", "expertise_level": "Senior Developer" },
        "duration_weeks": 8,
        "price_usd": 49.99,
        "is_premium": true,
        "tags": ["Compose", "MVVM", "Coroutines"],
        "rating": 4.8
      },
      {
        "course_id": "KOTLIN-002",
        "title": "Advanced Kotlin Coroutines",
        "description_short": "Deep dive into structured concurrency and Flow.",
        "instructor": { "name": "Mr. Rahim", "expertise_level": "Android Architect" },
        "duration_weeks": 6,
        "price_usd": 39.99,
        "is_premium": false,
        "tags": ["Coroutines", "Flow"],
        "rating": 4.6
      },
      {
        "course_id": "KOTLIN-003",
        "title": "Clean Architecture for Android",
        "description_short": "Design scalable and testable Android apps.",
        "instructor": { "name": "Ms. Tania", "expertise_level": "Lead Engineer" },
        "duration_weeks": 7,
        "price_usd": 44.99,
        "is_premium": true,
        "tags": ["Clean Architecture", "MVVM"],
        "rating": 4.7
      },
      {
        "course_id": "KOTLIN-004",
        "title": "Jetpack Compose State Management",
        "description_short": "Master state, recomposition, and performance in Compose.",
        "instructor": { "name": "Dr. Hasan", "expertise_level": "UI Specialist" },
        "duration_weeks": 5,
        "price_usd": 34.99,
        "is_premium": false,
        "tags": ["Compose", "State"],
        "rating": 4.5
      },
      {
        "course_id": "KOTLIN-005",
        "title": "Room Database Deep Dive",
        "description_short": "Learn offline-first persistence using Room and Flow.",
        "instructor": { "name": "Ms. Nabila", "expertise_level": "Android Engineer" },
        "duration_weeks": 4,
        "price_usd": 29.99,
        "is_premium": false,
        "tags": ["Room", "Offline", "Flow"],
        "rating": 4.4
      },
      {
        "course_id": "KOTLIN-006",
        "title": "SQLDelight for Multiplatform",
        "description_short": "Type-safe SQL for Android and KMP projects.",
        "instructor": { "name": "Mr. Karim", "expertise_level": "KMP Expert" },
        "duration_weeks": 6,
        "price_usd": 42.99,
        "is_premium": true,
        "tags": ["SQLDelight", "KMP"],
        "rating": 4.6
      },
      {
        "course_id": "KOTLIN-007",
        "title": "MVVM in Real Android Apps",
        "description_short": "Apply MVVM properly in production-scale apps.",
        "instructor": { "name": "Prof. Anika", "expertise_level": "Senior Developer" },
        "duration_weeks": 6,
        "price_usd": 37.99,
        "is_premium": false,
        "tags": ["MVVM", "Architecture"],
        "rating": 4.5
      },
      {
        "course_id": "KOTLIN-008",
        "title": "Dependency Injection with Hilt",
        "description_short": "Simplify dependency management using Hilt.",
        "instructor": { "name": "Mr. Rahim", "expertise_level": "Android Architect" },
        "duration_weeks": 4,
        "price_usd": 31.99,
        "is_premium": false,
        "tags": ["Hilt", "DI"],
        "rating": 4.4
      },
      {
        "course_id": "KOTLIN-009",
        "title": "Navigation Compose Essentials",
        "description_short": "Build safe and scalable navigation with Compose.",
        "instructor": { "name": "Ms. Tania", "expertise_level": "Lead Engineer" },
        "duration_weeks": 3,
        "price_usd": 24.99,
        "is_premium": false,
        "tags": ["Navigation", "Compose"],
        "rating": 4.3
      },
      {
        "course_id": "KOTLIN-010",
        "title": "Flow & Reactive Streams",
        "description_short": "Reactive programming using Kotlin Flow.",
        "instructor": { "name": "Dr. Hasan", "expertise_level": "Concurrency Expert" },
        "duration_weeks": 5,
        "price_usd": 35.99,
        "is_premium": true,
        "tags": ["Flow", "Reactive"],
        "rating": 4.6
      },
      {
        "course_id": "KOTLIN-011",
        "title": "Ktor Client for Android",
        "description_short": "Networking with Ktor and REST APIs.",
        "instructor": { "name": "Ms. Nabila", "expertise_level": "Backend Integrator" },
        "duration_weeks": 4,
        "price_usd": 33.99,
        "is_premium": false,
        "tags": ["Ktor", "Networking"],
        "rating": 4.4
      },
      {
        "course_id": "KOTLIN-012",
        "title": "Error Handling in Android",
        "description_short": "Design robust error and retry mechanisms.",
        "instructor": { "name": "Mr. Karim", "expertise_level": "Senior Engineer" },
        "duration_weeks": 3,
        "price_usd": 22.99,
        "is_premium": false,
        "tags": ["Error Handling"],
        "rating": 4.2
      },
      {
        "course_id": "KOTLIN-013",
        "title": "Performance Optimization",
        "description_short": "Optimize UI and background tasks in Android.",
        "instructor": { "name": "Prof. Anika", "expertise_level": "Performance Specialist" },
        "duration_weeks": 5,
        "price_usd": 41.99,
        "is_premium": true,
        "tags": ["Performance", "Compose"],
        "rating": 4.7
      },
      {
        "course_id": "KOTLIN-014",
        "title": "Testing Android Apps",
        "description_short": "Unit, integration, and UI testing strategies.",
        "instructor": { "name": "Ms. Tania", "expertise_level": "QA Lead" },
        "duration_weeks": 4,
        "price_usd": 29.99,
        "is_premium": false,
        "tags": ["Testing", "JUnit"],
        "rating": 4.3
      },
      {
        "course_id": "KOTLIN-015",
        "title": "Compose Animations",
        "description_short": "Create smooth animations using Compose.",
        "instructor": { "name": "Dr. Hasan", "expertise_level": "UI Animator" },
        "duration_weeks": 3,
        "price_usd": 26.99,
        "is_premium": false,
        "tags": ["Compose", "Animation"],
        "rating": 4.4
      },
      {
        "course_id": "KOTLIN-016",
        "title": "Android Security Basics",
        "description_short": "Secure storage, encryption, and best practices.",
        "instructor": { "name": "Mr. Rahim", "expertise_level": "Security Engineer" },
        "duration_weeks": 4,
        "price_usd": 38.99,
        "is_premium": true,
        "tags": ["Security"],
        "rating": 4.5
      },
      {
        "course_id": "KOTLIN-017",
        "title": "WorkManager in Practice",
        "description_short": "Background work done right with WorkManager.",
        "instructor": { "name": "Ms. Nabila", "expertise_level": "System Engineer" },
        "duration_weeks": 3,
        "price_usd": 23.99,
        "is_premium": false,
        "tags": ["WorkManager"],
        "rating": 4.2
      },
      {
        "course_id": "KOTLIN-018",
        "title": "Modularization Strategies",
        "description_short": "Scale Android projects with modular design.",
        "instructor": { "name": "Mr. Karim", "expertise_level": "Tech Lead" },
        "duration_weeks": 5,
        "price_usd": 43.99,
        "is_premium": true,
        "tags": ["Modularization", "Architecture"],
        "rating": 4.6
      },
      {
        "course_id": "KOTLIN-019",
        "title": "Compose Theming & Material 3",
        "description_short": "Build consistent UI with Material 3.",
        "instructor": { "name": "Dr. Hasan", "expertise_level": "Design Engineer" },
        "duration_weeks": 3,
        "price_usd": 27.99,
        "is_premium": false,
        "tags": ["Material3", "Compose"],
        "rating": 4.3
      },
      {
        "course_id": "KOTLIN-020",
        "title": "Production Readiness Checklist",
        "description_short": "Prepare Android apps for real-world release.",
        "instructor": { "name": "Prof. Anika", "expertise_level": "Senior Developer" },
        "duration_weeks": 4,
        "price_usd": 45.99,
        "is_premium": true,
        "tags": ["Production", "Best Practices"],
        "rating": 4.8
      }
    ]

""".trimIndent()