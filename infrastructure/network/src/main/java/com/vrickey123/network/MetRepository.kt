package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult

interface MetRepository {
    val metNetworkClient: MetNetworkClient
    // val metLocalDataStore: MetLocalDataStore

    /**
     * One-shot request to The Met search API.
     * @return [MetSearchResult]
     * */
    suspend fun search(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<MetSearchResult>

    /**
     * One-shot request to The Met Object API.
     * @return [MetObject]
     * */
    suspend fun fetchMetObject(objectID: Int): Result<MetObject>

    // One day this could be implemented to return data from a local persistent store
    // such as Room or a SQL Lite Database emit time that it is updated.
    //fun getMetObject(): Flow<Result<MetObject>>
}