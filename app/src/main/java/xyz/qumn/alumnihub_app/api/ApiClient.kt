package xyz.qumn.alumnihub_app.api

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.req.Page
import xyz.qumn.alumnihub_app.req.PageParam
import kotlin.reflect.full.memberProperties

object LoginUser {
    var token: String? = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjI2ODE1NDc4NTAyNDEyMjg4IiwidW5hIjoicXVtbiIsImV4cCI6MTcxNDI4MTAzMn0.o90ihTPcU-b-eBaspJ0BVOJE88IJzRQDBQjRe_vXURc"
}


object ApiClient {
    //Configure the HttpCLient
    val client = HttpClient(Android) {
        defaultRequest {
            host = "192.168.124.9"
            port = 8080
            url {
                protocol = URLProtocol.HTTP
            }
        }
        // For Logging
        install(Logging) {
            logger = CustomAndroidHttpLogger
            level = LogLevel.ALL
        }

        // Timeout plugin
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }

        // JSON Response properties
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }

        // Default request for POST, PUT, DELETE,etc...
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            //add this accept() for accept Json Body or Raw Json as Request Body
            accept(ContentType.Application.Json)
            Log.d("api-client", "current token ${LoginUser.token}")
            if (LoginUser.token != null)
                header("Authorization", "Bearer ${LoginUser.token}")
        }
    }

    suspend inline fun <reified T> get(
        uri: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        return runCatching { client.get(uri, block).body<Rsp<T>>().data!! }.onFailure {
            Log.e("api", "get: ${it.message}")
        }
    }

    suspend inline fun <reified T> post(
        uri: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        return runCatching { client.post(uri, block).body<Rsp<T>>().data!! }.onFailure {
            Log.e("api", "post: ${it.message}")
        }
    }

    suspend inline fun <reified R, reified P : PageParam> page(
        uri: String,
        params: P
    ): Page<R> {
        val paramsMap =
            P::class.memberProperties.filter { it.get(params) != null }.associate { prop ->
                prop.name to prop.get(params)
            }
        val page =
            runCatching {
                client.get(uri) {
                    for (entry in paramsMap) {
                        parameter(entry.key, entry.value)
                    }
                }.body<Rsp<Page<R>>>().data!!
            }.getOrElse { Page.empty() }
        return page
    }

    suspend inline fun <reified T> put(
        uri: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        return runCatching { client.put(uri, block).body<Rsp<T>>().data!! }.onFailure {
            Log.e("api", "put: ${it.message}")
        }
    }

    suspend inline fun <reified T> delete(
        uri: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): Result<T> {
        return runCatching { client.delete(uri, block).body<Rsp<T>>().data!! }.onFailure {
            Log.e("api", "delete: ${it.message}")
        }
    }

    suspend fun upload(contentResolver: ContentResolver, uri: Uri): Result<URL> {
        return runCatching {
            val inputStream = contentResolver.openInputStream(uri)!!
            val mimeType = contentResolver.getType(uri)

            val url = client.submitFormWithBinaryData("/files/upload", formData = formData {
                append("file", inputStream.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, mimeType!!)
                    append(
                        HttpHeaders.ContentDisposition,
                        "filename=image.${getFileExtensionFromMimeType(mimeType)}"
                    )
                })
            }).body<Rsp<URL>>().data
            inputStream.close()
            Log.d("image", "upload: the image url $url")
            url!!
        }
    }

    fun getFileExtensionFromMimeType(mimeType: String): String {
        return when (mimeType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            // Add more mappings for other MIME types as needed
            else -> {
                // For unknown MIME types, return a default extension or handle it as needed
                "unknown"
            }
        }
    }

}

private object CustomAndroidHttpLogger : Logger {
    private const val logTag = "CustomAndroidHttpLogger"

    override fun log(message: String) {
        Log.i(logTag, message)
    }
}