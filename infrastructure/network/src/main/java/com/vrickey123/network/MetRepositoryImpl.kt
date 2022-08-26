package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MetRepositoryImpl(
    override val metNetworkClient: MetNetworkClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MetRepository {

    override suspend fun search(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<MetSearchResult> = withContext(ioDispatcher) {
        handleResponse(metNetworkClient.search(query, hasImages, tags))
    }


    override suspend fun fetchMetObject(objectID: Int): Result<MetObject> =
        withContext(ioDispatcher) {
            handleResponse(metNetworkClient.fetchMetObject(objectID))
        }

    /**
     * Convenience function for transforming an HTTP Response into a Kotlin [Result]
     * */
    private fun <T> handleResponse(response: Response<T>): Result<T> {
        val body = response.body() // Kotlin smart cast is stupid
        return if (response.isSuccessful && body != null) {
            Result.success(body)
        } else {
            Result.failure(Throwable(response.message()))
        }
    }

    // One day this could be implemented to return data from a local persistent store
    // such as Room or a SQL Lite Database emit time that it is updated. Various caching
    // strategies such as e-tags or updatedAfter filters could be used in the repository to
    // return remote or local data. Apollo GraphQL has similar niceties.
    // fun getMetObject(objectID): Flow<Result<MetObject>> {
    //   metLocalDataStore.getMetObject(objectID)
    // }
}