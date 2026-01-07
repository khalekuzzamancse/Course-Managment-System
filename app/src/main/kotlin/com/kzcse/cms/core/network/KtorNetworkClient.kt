package com.kzcse.cms.core.network
import com.kzcse.cms.core.language.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class KtorNetworkClient : NetworkClient {
    private val httpClient: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    override suspend fun getOrThrow(url: String, headers: Headers?): String {
        Logger.off(this.javaClass.simpleName.toString(), "getOrThrow", "url", url)
        val response = httpClient.get(url) {
            headers?.let { setHeaders(it) }
        }
        return parseJson(response)
    }

    override suspend fun postOrThrow(url: String, payload: Any, headers: Headers?): String {

        val response = httpClient.post(url) {
            headers?.let { setHeaders(it) }
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
        return parseJson(response)
    }

    override suspend fun putOrThrow(url: String, payload: Any?, headers: Headers?): String {
        val response = httpClient.put(url) {
            headers?.let { setHeaders(it) }
            contentType(ContentType.Application.Json)
            payload?.let { setBody(it) }
        }
        return parseJson(response)
    }

    override suspend fun patchOrThrow(url: String, payload: Any, headers: Headers?): String {
        val response = httpClient.patch(url) {
            headers?.let { setHeaders(it) }
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
        return parseJson(response)
    }

    override suspend fun deleteOrThrow(url: String, headers: Headers?): String {
        val response = httpClient.delete(url) {
            headers?.let { setHeaders(it) }
        }
        return parseJson(response)
    }

    private fun HttpRequestBuilder.setHeaders(headers: Headers) {
        headers.headers.forEach { header ->
            this.headers.append(header.key, header.value)
        }
    }

    private suspend fun parseJson(response: HttpResponse): String {
        return response.bodyAsText()
    }

    fun close() {
        httpClient.close()
    }
}