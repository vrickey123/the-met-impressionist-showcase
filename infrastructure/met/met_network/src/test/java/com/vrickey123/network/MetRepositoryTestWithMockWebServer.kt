package com.vrickey123.network

import com.vrickey123.met_api.MetObject
import com.vrickey123.met_api.MetSearchResult
import com.vrickey123.network.local.FakeMetDatabase
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.remote.MetNetworkClient
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

/**
 * A [MetRepositoryImpl] test that uses [MockWebServer] in a [MetNetworkClient] to drive test
 * network responses with JSON files in the resource folder. A [FakeMetDatabase] instance can set
 * [FakeMetDatabase.setIsSuccess] to return a mocked successful or failure response.
 *
 * [Coroutines test docs](https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-test/README.md)
 * [Android flow test docs](https://developer.android.com/kotlin/flow/test)
 * [MockWebServer docs](https://github.com/square/okhttp/tree/master/mockwebserver)
 * */
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
        const val OBJECT_ID = 671456

        val MET_SEARCH_RESULT = MetSearchResult(total = 1, listOf(OBJECT_ID))
        val MET_SEARCH_RESULT_EMPTY = MetSearchResult(total = 0, null)

        val MET_OBJECT = MetObject(
            objectID = OBJECT_ID,
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

    lateinit var mockWebServer: MockWebServer
    lateinit var fakeMetDatabase: FakeMetDatabase
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
        fakeMetDatabase = FakeMetDatabase()

        subject = MetRepositoryImpl(
            metNetworkClient = metNetworkClient,
            metDatabase = fakeMetDatabase,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun fetchMetSearchResult_networkResponseSuccess_resultSuccess() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_SEARCH_SUCCESS, HttpURLConnection.HTTP_OK)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(MET_SEARCH_RESULT, result.getOrThrow())
    }

    @Test
    fun fetchMetSearchResult_networkResponseSuccessEmpty_resultSuccess() = runTest {
        mockWebServer.enqueue(FILENAME_RESPONSE_SEARCH_SUCCESS_EMPTY, HttpURLConnection.HTTP_OK)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY, HAS_IMAGES, TAGS)
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(MET_SEARCH_RESULT_EMPTY, result.getOrThrow())
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
        Assert.assertEquals(MET_OBJECT, result.getOrThrow())
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

    // As a result of the Room database architecture, which requires a Context, we're unable to test
    // the real end-to-end implementation in a unit test: it can only run with a Context in an
    // instrumented test in /androidTest. That is outside the scope of this project. However, we can
    // still test the MetRepository API's that return data from local storage by either implementing
    // a `class FakeMetDatabase(): MetDatabase` or by using a mocking framework.
    //
    // This example uses a FakeMetDatabase to control control success and error results for a
    // function call by setting FakeMetDatabase.isSuccess and having a corresponding success/failure
    // implementation in the FakeMetDatabase.
    @Test
    fun getAllMetObjects_databaseResponseSuccess_resultSuccess() = runTest {
        fakeMetDatabase.setIsSuccess(true)
        val result: Result<List<MetObject>> = subject.getAllMetObjects().first()
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(MetRepositoryImplTestWithMockk.ENTRIES, result.getOrThrow())
    }

    @Test
    fun getAllMetObjects_databaseResponseFailure_resultFailure() = runTest {
        fakeMetDatabase.setIsSuccess(false)
        val result: Result<List<MetObject>> = subject.getAllMetObjects().first()
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun insertMetObjects_databaseResponseSuccess_resultSuccess() = runTest {
        fakeMetDatabase.setIsSuccess(true)
        val result: Result<Unit> = subject.insertMetObjects(MetRepositoryImplTestWithMockk.ENTRIES)
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun insertMetObjects_databaseResponseFailure_resultFailure() = runTest {
        fakeMetDatabase.setIsSuccess(false)
        val result: Result<Unit> = subject.insertMetObjects(MetRepositoryImplTestWithMockk.ENTRIES)
        Assert.assertTrue(result.isFailure)
    }
}