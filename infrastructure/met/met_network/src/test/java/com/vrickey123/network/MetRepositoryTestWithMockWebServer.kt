package com.vrickey123.network

import com.vrickey123.met_api.MetObject
import com.vrickey123.met_api.MetSearchResult
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.remote.MetNetworkClient
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

// https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-test/README.md
// https://developer.android.com/kotlin/flow/test
// https://mockk.io/
@OptIn(ExperimentalCoroutinesApi::class)
class MetRepositoryTestWithMockWebServer {

    companion object {
        const val FILENAME_RESPONSE_SEARCH_SUCCESS = "response_search_success.json"
        const val FILENAME_RESPONSE_SEARCH_SUCCESS_EMPTY = "response_search_success_empty.json"
        const val FILENAME_RESPONSE_SEARCH_FAILURE_MALFORMED = "response_search_failure_malformed.json"
        const val FILENAME_RESPONSE_SEARCH_FAILURE_404 = "response_search_failure_404_not_found.json"

        const val FILENAME_RESPONSE_OBJECT_SUCCESS = "response_object_success.json"
        const val FILENAME_RESPONSE_OBJECT_FAILURE_MALFORMED = "response_object_failure_malformed.json"
        const val FILENAME_RESPONSE_OBJECT_FAILURE_404 = "response_object_failure_404_not_found.json"

        const val QUERY = "impressionism"
        const val HAS_IMAGES = true
        val TAGS = listOf("impressionism")
        const val OBJECT_ID = 437133

        val metSearchResult = MetSearchResult(total = 1, listOf(OBJECT_ID))
        val metSearchResultEmpty = MetSearchResult(total = 0, null)

        val metObject = MetObject(
            objectID = 671456,
            isHighlight = false,
            isPublicDomain = true,
            primaryImage = "https://images.metmuseum.org/CRDImages/ep/original/DP341200.jpg",
            primaryImageSmall = "https://images.metmuseum.org/CRDImages/ep/web-large/DP341200.jpg",
            department = "European Paintings",
            objectName = "Painting",
            title = "Chrysanthemums in the Garden at Petit-Gennevilliers",
            artistDisplayName = "Gustave Caillebotte",
            artistDisplayBio = "French, Paris 1848–1894 Gennevilliers",
            artistNationality = "French",
            objectDate = "1893",
            medium = "Oil on canvas",
            dimensions = "39 1/8 × 24 1/4 in. (99.4 × 61.6 cm)",
            objectURL = "https://www.metmuseum.org/art/collection/search/671456",
            GalleryNumber = "824"
        )
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockMetDatabase: MetDatabase

    lateinit var mockWebServer: MockWebServer

    lateinit var subject: MetRepositoryImpl

    @Before
    fun setup() = runTest {
        // Pass in the StandardTestDispatcher to the Repository and call Dispatchers#setMain so
        // that both the Repository and runTest functions are running on the same test dispatcher.
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = MetNetworkClient.buildRetrofit(
            baseUrl = mockWebServer.url("/").toString(),
            okHttpClient = OkHttpClient.Builder().build(),
            moshi = MetNetworkClient.buildMoshi()
        )
        val metNetworkClient = MetNetworkClient.create(retrofit)

        subject = MetRepositoryImpl(
            metNetworkClient = metNetworkClient,
            metDatabase = mockMetDatabase,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun fetchMetSearchResult_networkResponseSuccess_resultSuccess() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_SEARCH_SUCCESS, HttpURLConnection.HTTP_OK)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(metSearchResult, result.getOrThrow())
    }

    @Test
    fun fetchMetSearchResult_networkResponseSuccessEmpty_resultSuccess() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_SEARCH_SUCCESS_EMPTY, HttpURLConnection.HTTP_OK)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(metSearchResultEmpty, result.getOrThrow())
    }

    @Test
    fun fetchMetSearchResult_networkResponseError_resultFailure() = runTest {
        val mock400ErrorResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
        mockWebServer.enqueue(mock400ErrorResponse)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetSearchResult_networkResponseError404NotFound_resultFailure() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_SEARCH_FAILURE_404, HttpURLConnection.HTTP_NOT_FOUND)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetSearchResult_networkResponseErrorMalformed_resultFailure() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_SEARCH_FAILURE_MALFORMED, HttpURLConnection.HTTP_BAD_REQUEST)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetObject_networkResponseSuccess_resultSuccess() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_OBJECT_SUCCESS, HttpURLConnection.HTTP_OK)
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(metObject, result.getOrThrow())
    }

    @Test
    fun fetchMetObject_networkResponseError_resultFailure() = runTest {
        val mock400ErrorResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
        mockWebServer.enqueue(mock400ErrorResponse)
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetObject_networkResponseError404NotFound_resultFailure() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_OBJECT_FAILURE_404, HttpURLConnection.HTTP_NOT_FOUND)
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetObject_networkResponseErrorMalformed_resultFailure() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_OBJECT_FAILURE_MALFORMED, HttpURLConnection.HTTP_BAD_REQUEST)
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isFailure)
    }
}