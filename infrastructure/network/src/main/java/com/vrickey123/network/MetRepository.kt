package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.remote.MetNetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface MetRepository {
    val metNetworkClient: MetNetworkClient
    val metDatabase: MetDatabase
    val dispatcher: CoroutineDispatcher

    /**
     * One-shot request to The Met search API.
     * @return [MetSearchResult]
     * */
    suspend fun fetchMetSearchResult(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<MetSearchResult>

    /**
     * One-shot request to The Met Object API.
     * @return [MetObject]
     * */
    suspend fun fetchMetObject(objectID: Int): Result<MetObject>

    /**
     * Emits [MetObject]'s from the database any time they are updated.
     * @return [Flow<List<MetObject>>]
     * */
    fun getMetObjects(): Flow<List<MetObject>>

    /**
     * Emits [MetObject] from the database any time they are updated.
     * @return [Flow<<MetObject>]
     * */
    fun getMetObjectAsFlow(objectID: String): Flow<MetObject>

    /**
     * One-shot request to the local Met Room Database.
     * @return [MetObject]
     * */
    suspend fun getMetObject(objectID: String): Result<MetObject>

    // One day various caching strategies such as e-tags or updatedAfter filters with 304 HTTP
    // Not Modified response codes for REST services could be used in the repository to return
    // remote or local data. Apollo GraphQL has similar niceties. For now, we'll return local
    // data if it exists without making another network call. The Met API takes about 30 seconds
    // in its initial request, so we'll seed the database and return from it after the fact. The
    // Met's impressionist collection doesn't change often, so no big deal as a user...
    suspend fun getLocalThenRemoteMetObjects(): Result<List<MetObject>>
}