package com.vrickey123.network

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

/**
 * Parses a JSON file as an input stream, then sets it as the [MockWebServer]'s [MockResponse].
 * */
@Throws(IOException::class)
internal fun MockWebServer.enqueue(file: String, code: Int) {
    val inputStream: InputStream? = javaClass.classLoader?.getResourceAsStream(file)
    inputStream?.let {
        inputStream.use { inputStream ->
            val body = inputStream.source().buffer().readString(StandardCharsets.UTF_8)
            val mockResponse = MockResponse().setResponseCode(code).setBody(body)
            enqueue(mockResponse)
        }
    }
}