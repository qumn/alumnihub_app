package xyz.qumn.alumnihub_app.api

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.streams.asInput
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

var token: String? = null

object ApiClient {

    //Configure the HttpCLient
    @OptIn(ExperimentalSerializationApi::class)
    var client = HttpClient(Android) {
        defaultRequest {
            host = "192.168.10.48"
            port = 8080
            url {
                protocol = URLProtocol.HTTP
            }
        }
        // For Logging
        install(Logging) {
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
            Log.d("api-client", "current token $token")
            if (token != null)
                header("Authorization", "Bearer $token")
        }
    }

    suspend fun upload(uri: Uri): String {
        val file = uri.toFile()
        Log.d("api-client", "uploading file $file")

        val form = formData {
            append("file", InputProvider(file.length()) {
                file.inputStream().asInput()
            })
        }

        return client.submitFormWithBinaryData("/files/upload", form).body<Rsp<String>>().data!!
    }
}
