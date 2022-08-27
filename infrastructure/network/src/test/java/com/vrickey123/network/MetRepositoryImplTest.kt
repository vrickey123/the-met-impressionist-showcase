package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.Result.Companion.failure

// https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-test/README.md
@OptIn(ExperimentalCoroutinesApi::class)
class MetRepositoryImplTest {

    companion object {
        private const val OBJECT_ID = 1
        private const val QUERY_STRING = "query"
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var metObject: MetObject

    @MockK
    lateinit var metSearchResult: MetSearchResult

    @MockK
    lateinit var client: MetNetworkClient

    @InjectMockKs
    private lateinit var subject: MetRepositoryImpl

    @Test
    fun search_networkClientCalledOnce() = runTest {
        coEvery { client.search(any(), any(), any()) } returns Response.success(metSearchResult)
        subject.search(QUERY_STRING, true, emptyList())
        coVerify(exactly = 1) { client.search(QUERY_STRING, true, emptyList()) }
    }

    @Test
    fun search_networkResponseSuccess_resultSuccess() = runTest {
        coEvery { client.search(any(), any(), any()) } returns Response.success(metSearchResult)
        val result: Result<MetSearchResult> = subject.search(QUERY_STRING, true, emptyList())
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun search_networkResponseError_resultFailure() = runTest {
        coEvery { client.search(any(), any(), any()) } returns Response.error(404, ResponseBody.create(null, ""))
        val result: Result<MetSearchResult> = subject.search(QUERY_STRING, true, emptyList())
        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun fetchMetObject_networkClientCalledOnce() = runTest {
        coEvery { client.fetchMetObject(any()) } returns Response.success(metObject)
        subject.fetchMetObject(OBJECT_ID)
        coVerify(exactly = 1) { client.fetchMetObject(OBJECT_ID) }
    }

    @Test
    fun fetchMetObject_networkResponseSuccess_resultSuccess() = runTest {
        coEvery { client.fetchMetObject(any()) } returns Response.success(metObject)
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun fetchMetObject_networkResponseError_resultFailure() = runTest {
        coEvery { client.fetchMetObject(any()) } returns Response.error(404, ResponseBody.create(null, ""))
        val result: Result<MetObject> = subject.fetchMetObject(OBJECT_ID)
        Assert.assertTrue(result.isFailure)
    }
}