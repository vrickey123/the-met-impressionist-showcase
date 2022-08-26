package com.vrickey123.network

import com.vrickey123.model.api.MetObject
import com.vrickey123.model.api.MetSearchResult
import kotlinx.coroutines.flow.Flow

class MetRepositoryImpl(override val metNetworkClient: MetNetworkClient) : MetRepository {
    override fun search(
        query: String,
        hasImages: Boolean,
        tags: List<String>
    ): Flow<MetSearchResult> {
        TODO("Not yet implemented")
    }

    override fun fetchMetObject(objectID: Int): Flow<MetObject> {
        TODO("Not yet implemented")
    }
}