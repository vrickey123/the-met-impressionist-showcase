package com.vrickey123.network

import com.vrickey123.met_api.MetObject
import com.vrickey123.met_api.MetSearchResult
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.remote.MetNetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface MetRepository {
    val metNetworkClient: MetNetworkClient
    val metDatabase: MetDatabase
    val dispatcher: CoroutineDispatcher

    /**
     * One-shot network request to fetch a [MetSearchResult] from The Met Collection Search API.
     *
     * @return [MetSearchResult]
     * */
    suspend fun fetchMetSearchResult(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<MetSearchResult>

    /**
     * One-shot network request to fetch a [MetObject] using [MetNetworkClient].
     *
     * Inserts [MetObject] into local [MetDatabase] on successful response.
     *
     * @param id: the id of the [MetObject]
     *
     * @return [Result]<[MetObject]>
     * */
    suspend fun fetchMetObject(id: Int): Result<MetObject>

    /**
     * One-shot network request to fetch a [List]<[MetObject]> using [MetNetworkClient].
     *
     * Inserts a [List]<[MetObject]> into local [MetDatabase] on successful response.
     *
     * @return [Result]<[List]<[MetObject]>>
     * */
    suspend fun fetchMetObjects(ids: List<Int>): Result<List<MetObject>>

    /**
     * Emits [MetObject] any time it is updated in the local [MetDatabase].
     *
     * @return [Flow]<[Result]<[MetObject]>>
     * */
    fun getMetObject(id: Int): Flow<Result<MetObject>>

    /**
     * Emits a [List]<[MetObject]> any time they are updated in the local [MetDatabase].
     *
     * @return [Flow]<[Result]<[MetObject]>>
     * */
    fun getMetObjects(ids: List<Int>): Flow<Result<List<MetObject>>>

    /**
     * Emits a [List]<[MetObject]> in the in the local [MetDatabase] any time they are updated.
     *
     * @return [Flow]<[Result]<[List]<[MetObject]>>>
     * */
    fun getAllMetObjects(): Flow<Result<List<MetObject>>>

    /**
     * One-shot database request to insert a [MetObject] into the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun insertMetObject(metObject: MetObject): Result<Unit>

    /**
     * One-shot database request to insert a [List]<[MetObject]> into the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun insertMetObjects(metObjects: List<MetObject>): Result<Unit>

    /**
     * One-shot database request to delete all [MetObject]'s from the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun deleteAllMetObjects(): Result<Unit>

    /**
     * One-shot database request to delete a [List]<[MetObject]> from the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun deleteMetObjects(ids: List<Int>): Result<Unit>

    /**
     * One-shot database request to delete a [MetObject] from the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun deleteMetObject(id: Int): Result<Unit>

    /**
     * One-shot network request using [MetNetworkClient] to fetch a [List]<[MetObject]>, then insert
     * the response into the [MetDatabase]; but only if the [MetDatabase] is empty.
     *
     * Note: In an API at scale with data that changes frequently, the backend could use a "304 Not
     * Modified" status code, e-tags, or updatedAfter filter in a [fetchMetObjects] response so that
     * the clients only make a network call to download data if the underlying data set on the
     * server was different from the data that exists in the client-side cache.
     *
     * In this case where we're working with largely static data in an academic resource, this
     * function makes a network API call to seed our database on the first launch so that we're not
     * making unnecessary - and slow - network API calls every time the user opens the app.
     * */
    suspend fun prefetchMetObjectsIfEmpty(query: String, tags: List<String>): Result<Unit>
}