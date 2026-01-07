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