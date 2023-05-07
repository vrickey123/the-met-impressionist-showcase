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
     * One-shot request to The Met Collection Search API.
     *
     * @return [MetSearchResult]
     * */
    suspend fun fetchMetSearchResult(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<com.vrickey123.met_api.MetSearchResult>

    /**
     * One-shot network request to The Met Collection API using [MetNetworkClient].
     * Inserts [MetObject] into local [MetDatabase] on successful response.
     *
     * @param id: the id of the [MetObject]
     *
     * @return [Result]<[MetObject]>
     * */
    suspend fun fetchMetObject(id: Int): Result<com.vrickey123.met_api.MetObject>

    /**
     * One-shot network request to The Met Collection API using [MetNetworkClient].
     * Inserts [MetObject] into local [MetDatabase] on successful response.
     *
     * @return [Result]<[MetObject]>
     * */
    suspend fun fetchMetObjects(ids: List<Int>): Result<List<com.vrickey123.met_api.MetObject>>

    /**
     * Emits [MetObject] any time it is updated in the local [MetDatabase].
     *
     * @return [Flow]<[Result]<[MetObject]>>
     * */
    fun getMetObject(id: Int): Flow<Result<com.vrickey123.met_api.MetObject>>

    /**
     * Emits a [List]<[MetObject]> any time they are updated in the local [MetDatabase].
     *
     * @return [Flow]<[Result]<[MetObject]>>
     * */
    fun getMetObjects(ids: List<Int>): Flow<Result<List<com.vrickey123.met_api.MetObject>>>

    /**
     * Emits a [List] of all the [MetObject]s in the in the local [MetDatabase] any time they are
     * updated.
     *
     * @return [Flow]<[Result]<[MetObject]>>
     * */
    fun getAllMetObjects(): Flow<Result<List<com.vrickey123.met_api.MetObject>>>

    /**
     * One-shot database request to insert a [List]<[MetObject]> into the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun insertMetObject(metObject: com.vrickey123.met_api.MetObject): Result<Unit>

    /**
     * One-shot database request to insert a [List]<[MetObject]> into the local [MetDatabase].
     *
     * @return [Result]<[Unit]>
     * */
    suspend fun insertMetObjects(metObjects: List<com.vrickey123.met_api.MetObject>): Result<Unit>

    /**
     * One-shot database request to delete all [MetObject]s from the local [MetDatabase].
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

    suspend fun getLocalThenRemoteMetObjects(query: String, tags: List<String>): Result<Unit>
}