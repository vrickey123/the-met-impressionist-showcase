package com.vrickey123.network

import android.util.Log
import com.vrickey123.met_api.MetObject
import com.vrickey123.met_api.MetSearchResult
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

    override suspend fun fetchMetObject(id: Int): Result<MetObject> =
        withContext(dispatcher) {
            Log.d(TAG, "Fetch metObject: $id")
            return@withContext try {
                Log.d(TAG, "Fetch metObject success")
                Result.from(metNetworkClient.fetchMetObject(id))
            } catch (e: Throwable) {
                Log.e(TAG, "Fetch metObject failure: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun fetchMetObjects(ids: List<Int>): Result<List<MetObject>> =
        withContext(dispatcher) {
            Log.d(TAG, "Fetch metObjects: $ids")
            return@withContext try {
                // Unfortunately, the API doesn't have a batch collection with one API call that
                // sends a ids: List<Int> as a query parameter, so instead we make sequential
                // one-shot API MetObject calls for each id.
                // The Dispatcher used by the internal OkHTTPClient is set to a default of 64 concurrent
                // requests, which should fall under the 80 requests per second rate limit of the Met API.
                // https://stackoverflow.com/questions/52881862/throttle-or-limit-kotlin-coroutine-count
                val metObjects: List<MetObject> = ids.map { objectID ->
                    fetchMetObject(objectID).getOrThrow()
                }
                    // API hasImages=true sometimes returns MetObject's with empty primary images
                    .filter { it.primaryImageSmall.isNotEmpty() }
                metDatabase.metObjectDAO().insertList(metObjects)
                Log.d(TAG, "Fetch metObjects success")
                Result.success(metObjects)
            } catch (e: Throwable) {
                Log.e(TAG, "Fetch metObjects failure: ${e.message}")
                Result.failure(e)
            }
        }

    override fun getMetObject(id: Int): Flow<Result<MetObject>> {
        Log.d(TAG, "Get metObject from local storage: $id")
        return metDatabase.metObjectDAO().loadByIdAsFlow(id)
            .map {
                Log.d(TAG, "Get metObject from local storage success")
                Result.success(it)
            }
            .flowOn(dispatcher)
            .catch {
                Log.e(TAG, "Get all metObject from local storage failure: ${it.message}")
                emit(Result.failure(it))
            }
    }

    override fun getMetObjects(ids: List<Int>): Flow<Result<List<MetObject>>> {
        Log.d(TAG, "Get metObjects from local storage: $ids")
        return metDatabase.metObjectDAO().loadByIdsAsFlow(ids)
            .map {
                Log.d(TAG, "Get metObjects from local storage success")
                Result.success(it)
            }
            .flowOn(dispatcher)
            .catch {
                Log.e(TAG, "Get all metObjects from local storage failure: ${it.message}")
                emit(Result.failure(it))
            }
    }

    override fun getAllMetObjects(): Flow<Result<List<MetObject>>> {
        Log.d(TAG, "Getting all metObjects from local storage")
        return metDatabase.metObjectDAO().loadAllAsFlow()
            .map {
                Log.d(TAG, "Get all metObjects from local storage success | size: ${it.size}")
                Result.success(it)
            }
            .flowOn(dispatcher)
            .catch {
                Log.e(TAG, "Get all metObjects from local storage: ${it.message}")
                emit(Result.failure(it))
            }
    }

    override suspend fun insertMetObject(metObject: MetObject): Result<Unit> =
        withContext(dispatcher) {
            Log.d(TAG, "Inserting metObject into into local storage: $metObject")
            return@withContext try {
                metDatabase.metObjectDAO().insert(metObject)
                Log.d(TAG, "Insert metObject into into local storage success")
                Result.success(Unit)
            } catch (e: Throwable) {
                Log.e(TAG, "Insert metObject into into local storage failure: ${e.message}")
                Result.failure(e)
            }
        }


    override suspend fun insertMetObjects(metObjects: List<MetObject>): Result<Unit> =
        withContext(dispatcher) {
            Log.d(TAG, "Inserting metObjects into local storage | size: ${metObjects.size}")
            return@withContext try {
                metDatabase.metObjectDAO().insertList(metObjects)
                Log.d(TAG, "Inserting metObjects into local storage success")
                Result.success(Unit)
            } catch (e: Throwable) {
                Log.e(TAG, "Inserting metObjects into local storage failure: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun deleteAllMetObjects(): Result<Unit> = withContext(dispatcher) {
        Log.d(TAG, "Delete all metObjects")
        return@withContext try {
            metDatabase.metObjectDAO().deleteAll()
            Log.d(TAG, "Delete all metObjects success")
            Result.success(Unit)
        } catch (e: Throwable) {
            Log.e(TAG, "Delete all metObjects failure: ${e.message}")
            Result.failure(e)
        }
    }

    // Note: In the future, this could be hooked up to the Met Collection API to sync local/remote
    // if there was ever a user-driven feature such as a list of their favorite works.
    override suspend fun deleteMetObjects(ids: List<Int>): Result<Unit> = withContext(dispatcher) {
        Log.d(TAG, "Deleting metObjects from local storage | ids: ${ids}")
        return@withContext try {
            metDatabase.metObjectDAO().deleteByIds(ids)
            Log.d(TAG, "Delete metObjects from local storage success")
            Result.success(Unit)
        } catch (e: Throwable) {
            Log.e(TAG, "Delete metObjects from local storage failure: ${e.message}")
            Result.failure(e)
        }
    }

    // Note: In the future, this could be hooked up to the Met Collection API to sync local/remote
    // if there was ever a user-driven feature such as a list of their favorite works.
    override suspend fun deleteMetObject(id: Int): Result<Unit> = withContext(dispatcher) {
        Log.d(TAG, "Deleting metObject from local storage: $id")
        return@withContext try {
            metDatabase.metObjectDAO().deleteById(id)
            Log.d(TAG, "Delete metObject from local storage success")
            Result.success(Unit)
        } catch (e: Throwable) {
            Log.e(TAG, "Delete metObject from local storage failure: ${e.message}")
            Result.failure(e)
        }
    }

    // See code comment in interface MetRepository
    override suspend fun getLocalThenRemoteMetObjects(query: String, tags: List<String>): Result<Unit> =
        withContext(dispatcher) {
            Log.d(TAG, "get local or remote data")
            return@withContext try {
                if (!metDatabase.metObjectDAO().isEmpty()) {
                    Log.d(TAG, "returning local data from DB")
                    Result.success(Unit)
                } else {
                    Log.e(TAG, "fetching all metObjects")
                    val metSearchResult = fetchMetSearchResult(query, true, tags)
                    val objectIDs = metSearchResult.getOrThrow().objectIDs
                    if (objectIDs.isNullOrEmpty()) {
                        Log.e(TAG, "fetching all metObjects | empty state")
                        metDatabase.metObjectDAO().insertList(emptyList())
                        Result.success(Unit)
                    } else {
                        val metObjects = fetchMetObjects(objectIDs)
                        metObjects.fold(
                            onSuccess = { Result.success(Unit) },
                            onFailure = { Result.failure(it) }
                        )
                    }
                }
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
}