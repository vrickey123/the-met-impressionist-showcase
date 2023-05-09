package com.vrickey123.network

import com.vrickey123.met_api.MetObject
import com.vrickey123.met_api.MetSearchResult
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.remote.MetNetworkClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

/**
 * A [MetRepositoryImpl] test that uses [MockK] to mock [MetNetworkClient] and [MetDatabase]
 * instances and their functions.
 *
 * [Coroutines test docs](https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-test/README.md)
 * [Android flow test docs](https://developer.android.com/kotlin/flow/test)
 * [mockk docs](https://mockk.io/)
 * */
@OptIn(ExperimentalCoroutinesApi::class)
class MetRepositoryImplTestWithMockk {

    companion object {
        private const val QUERY_STRING = "query"
        private val TAGS = listOf("tag")
        private const val message = "Fake Exception"

        private const val OBJECT_ID = 671456
        private val MET_OBJECT = MetObject(
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

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var mockMetObject: MetObject

    @MockK
    lateinit var mockMetSearchResult: MetSearchResult

    @MockK
    lateinit var mockMetNetworkClient: MetNetworkClient

    @MockK
    lateinit var mockMetDatabase: MetDatabase

    @InjectMockKs
    private lateinit var subject: MetRepositoryImpl

    @Test
    fun search_networkClientCalledOnce() = runTest {
        coEvery { mockMetNetworkClient.search(any(), any(), any()) } returns Response.success(mockMetSearchResult)
        subject.fetchMetSearchResult(QUERY_STRING, true, emptyList())
        coVerify(exactly = 1) { mockMetNetworkClient.search(QUERY_STRING, true, emptyList()) }
    }

    @Test
    fun search_networkResponseSuccess_resultSuccess() = runTest {
        coEvery { mockMetNetworkClient.search(any(), any(), any()) } returns Response.success(mockMetSearchResult)
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY_STRING, true, emptyList())
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun search_networkResponseError_resultFailure() = runTest {
        coEvery { mockMetNetworkClient.search(any(), any(), any()) } returns Response.error(404, ResponseBody.create(null, ""))
        val result: Result<MetSearchResult> = subject.fetchMetSearchResult(QUERY_STRING, true, emptyList())
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetObject_networkClientCalledOnce() = runTest {
        coEvery { mockMetNetworkClient.fetchMetObject(any()) } returns Response.success(mockMetObject)
        subject.fetchMetObject(OBJECT_ID)
        coVerify(exactly = 1) { mockMetNetworkClient.fetchMetObject(OBJECT_ID) }
    }

    @Test
    fun fetchMetObject_networkResponseSuccess_resultSuccess() = runTest {
        coEvery { mockMetNetworkClient.fetchMetObject(any()) } returns Response.success(mockMetObject)
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun fetchMetObject_networkResponseError_resultFailure() = runTest {
        coEvery { mockMetNetworkClient.fetchMetObject(any()) } returns Response.error(404, ResponseBody.create(null, ""))
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isFailure)
    }

    // As a result of the Room database architecture, which requires a Context, we're unable to test
    // the real end-to-end implementation in a unit test: it can only run with a Context in an
    // instrumented test in /androidTest. That is outside the scope of this project. However, we can
    // still test the MetRepository API's that return data from local storage by either implementing
    // a `class FakeMetDatabase(): MetDatabase` or by using a mocking framework.
    //
    // This example uses mockk to control control success and error results for a function call by
    // defining returning a value for a given function call using coEvery returns or coEvery throws.
    @Test
    fun getAllMetObjects_databaseCalledOnce() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().loadAllAsFlow() } returns flow {
            emit(emptyList())
        }
        subject.getAllMetObjects()
        coVerify(exactly = 1) { mockMetDatabase.metObjectDAO().loadAllAsFlow() }
    }

    @Test
    fun getAllMetObjects_databaseResponseSuccess_resultSuccess() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().loadAllAsFlow() } returns flow {
            emit(emptyList())
        }
        val result: Result<List<MetObject>> = subject.getAllMetObjects().first()
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(emptyList<MetObject>(), result.getOrThrow())
    }

    @Test
    fun getAllMetObjects_databaseResponseFailure_resultFailure() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().loadAllAsFlow() } returns flow {
            throw Exception(message)
        }
        val result: Result<List<MetObject>> = subject.getAllMetObjects().first()
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun insertMetObjects_databaseCalledOnce() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().insertList(emptyList()) } returns Unit
        subject.insertMetObjects(emptyList())
        coVerify(exactly = 1) { mockMetDatabase.metObjectDAO().insertList(emptyList()) }
    }

    @Test
    fun insertMetObjects_databaseResponseSuccess_resultSuccess() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().insertList(emptyList()) } returns Unit
        val result: Result<Unit> = subject.insertMetObjects(emptyList())
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun insertMetObjects_databaseResponseFailure_resultFailure() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().insertList(emptyList()) } throws Exception(message)
        val result: Result<Unit> = subject.insertMetObjects(emptyList())
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun prefetchMetObjectsIfEmpty_emptyState_nonNullSearchResult_resultSuccess() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().isEmpty() } returns true
        coEvery { mockMetNetworkClient.search(any(), any(), any()) } returns Response.success(mockMetSearchResult)
        coEvery { mockMetDatabase.metObjectDAO().insertList(any()) } returns Unit
        coEvery { mockMetSearchResult.objectIDs } returns listOf(OBJECT_ID)
        coEvery { mockMetNetworkClient.fetchMetObject(any()) } returns Response.success(MET_OBJECT)
        val result = subject.prefetchMetObjectsIfEmpty(QUERY_STRING, TAGS)
        Assert.assertTrue(result.isSuccess)
        coVerify(exactly = 2) {
            mockMetDatabase.metObjectDAO()
            //mockMetDatabase.metObjectDAO().isEmpty()
            //mockMetDatabase.metObjectDAO().insertList(any())
        }
        coVerify(exactly = 1) {
            mockMetNetworkClient.search(any(), any(), any())
            mockMetNetworkClient.fetchMetObject(any())
        }
    }

    @Test
    fun prefetchMetObjectsIfEmpty_emptyState_nullSearchResult_resultSuccess() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().isEmpty() } returns true
        coEvery { mockMetNetworkClient.search(any(), any(), any()) } returns Response.success(mockMetSearchResult)
        coEvery { mockMetSearchResult.objectIDs } returns null
        coEvery { mockMetDatabase.metObjectDAO().insertList(any()) } returns Unit
        val result = subject.prefetchMetObjectsIfEmpty(QUERY_STRING, TAGS)
        Assert.assertTrue(result.isSuccess)
        coVerify(exactly = 2) {
            mockMetDatabase.metObjectDAO()
            //mockMetDatabase.metObjectDAO().isEmpty()
            //mockMetDatabase.metObjectDAO().insertList(any())
        }
        coVerify(exactly = 1) {
            mockMetNetworkClient.search(any(), any(), any())
        }
    }

    @Test
    fun prefetchMetObjectsIfEmpty_emptyStateAndNetworkFailure_resultFailure() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().isEmpty() } returns true
        coEvery { mockMetNetworkClient.search(any(), any(), any()) } returns Response.error(404, ResponseBody.create(null, ""))
        val result = subject.prefetchMetObjectsIfEmpty(QUERY_STRING, TAGS)
        Assert.assertTrue(result.isFailure)
        coVerify(exactly = 1) {
            mockMetDatabase.metObjectDAO()
        }
        coVerify(exactly = 1) {
            mockMetNetworkClient.search(any(), any(), any())
        }
    }

    @Test
    fun prefetchMetObjectsIfEmpty_notEmptyState_resultSuccess() = runTest {
        coEvery { mockMetDatabase.metObjectDAO().isEmpty() } returns false
        val result = subject.prefetchMetObjectsIfEmpty(QUERY_STRING, TAGS)
        Assert.assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            mockMetDatabase.metObjectDAO()
        }
    }
}