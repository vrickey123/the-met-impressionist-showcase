package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import kotlinx.coroutines.flow.Flow

interface MetRepository {
    val metNetworkClient: MetNetworkClient

    // One day this could be implemented to return data from a local persistent store
    // such as Room or a SQL Lite Database.
    // val metLocalDataStore: MetLocalDataStore

    fun search(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Flow<MetSearchResult>

    fun fetchMetObject(objectID: Int): Flow<MetObject>

    // One day this could be implemented to return data from a local persistent store
    // such as Room or a SQL Lite Database.
    //fun getMetObject()
}