package com.vrickey123.network

import android.util.Log
import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import com.vrickey123.network.local.MetDatabase
import com.vrickey123.network.remote.MetNetworkClient
import com.vrickey123.network.remote.from
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class MetRepositoryImpl(
    override val metNetworkClient: MetNetworkClient,
    override val metDatabase: MetDatabase,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MetRepository {

    companion object {
        const val QUERY = "impressionism"
        val TAGS = listOf<String>("impressionism")
        private val TAG by lazy { MetRepositoryImpl::class.java.simpleName }
    }

    override suspend fun fetchMetSearchResult(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Result<MetSearchResult> = withContext(dispatcher) {
        Log.d(TAG, "Search query: $query")
        return@withContext try {
            Result.from(metNetworkClient.search(query, hasImages, tags))
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }


    override suspend fun fetchMetObject(objectID: Int): Result<MetObject> =
        withContext(dispatcher) {
            Log.d(TAG, "Fetch objectID: $objectID")
            return@withContext try {
                Result.from(metNetworkClient.fetchMetObject(objectID))
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }

    override fun getMetObjects(): Flow<List<MetObject>> {
        return metDatabase.metObjectDAO().getAllAsFlow()
    }

    override fun getMetObjectAsFlow(objectID: String): Flow<MetObject> {
        return metDatabase.metObjectDAO().getAsFlow(objectID)
    }

    override suspend fun getMetObject(objectID: String): Result<MetObject> =
        withContext(dispatcher) {
            Log.d(TAG, "Get from DB: $objectID")
            return@withContext try {
                Result.success(metDatabase.metObjectDAO().get(objectID))
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }

    // See code comment in interface MetRepository
    override suspend fun getLocalThenRemoteMetObjects(): Result<List<MetObject>> =
        withContext(dispatcher) {
            Log.d(TAG, "get local or remote data")
            return@withContext try {
                if (metDatabase.metObjectDAO().getAll().isNotEmpty()) {
                    Log.d(TAG, "returning local data from DB")
                    Result.success(metDatabase.metObjectDAO().getAll())
                } else {
                    Log.d(TAG, "fetching all met objects")
                    val metSearchResult = fetchMetSearchResult(QUERY, true, TAGS)
                    val metObjects = fetchAndInsertAllMetObjects(metSearchResult.getOrThrow().objectIDs)
                    Result.success(metObjects.getOrThrow())
                }
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }

    private suspend fun fetchAndInsertAllMetObjects(objectIDs: List<Int>): Result<List<MetObject>> =
        withContext(dispatcher) {
            return@withContext try {
                val metObjects: List<MetObject> = objectIDs
                    .map { objectID -> fetchMetObject(objectID).getOrThrow() }
                    // API hasImages=true sometimes returns MetObject's with empty primary images
                    .filter { it.primaryImageSmall.isNotEmpty() }
                metDatabase.metObjectDAO().insertAll(metObjects)
                Result.success(metObjects)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
}