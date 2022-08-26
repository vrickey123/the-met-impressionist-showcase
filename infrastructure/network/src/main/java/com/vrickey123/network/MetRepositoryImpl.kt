package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MetRepositoryImpl(
    override val metNetworkClient: MetNetworkClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MetRepository {

    override suspend fun search(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<MetSearchResult> = withContext(ioDispatcher) {
        Result.from(metNetworkClient.search(query, hasImages, tags))
    }


    override suspend fun fetchMetObject(objectID: Int): Result<MetObject> =
        withContext(ioDispatcher) {
            Result.from(metNetworkClient.fetchMetObject(objectID))
        }

    // One day this could be implemented to emit data from a local persistent store
    // such as Room or a SQL Lite Database every time that it is updated. This would complete a
    // Reactive architecture. Additionally, various caching strategies such as e-tags or
    // updatedAfter filters for REST services could be used in the repository to return remote or
    // local data. Apollo GraphQL has similar niceties.
    // fun getMetObject(objectID): Flow<Result<MetObject>> {
    //   metLocalDataStore.getMetObject(objectID)
    // }
}