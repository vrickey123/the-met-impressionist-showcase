package com.vrickey123.network.local

import com.vrickey123.met_api.MetObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMetObjectDao: MetObjectDAO {

    companion object {
        const val message = "Fake Exception"

        val MET_OBJECT = MetObject(
            objectID = 671456,
            isHighlight = false,
            isPublicDomain = true,
            primaryImage = "https://images.metmuseum.org/CRDImages/ep/original/DP341200.jpg",
            primaryImageSmall = "https://images.metmuseum.org/CRDImages/ep/web-large/DP341200.jpg",
            department = "European Paintings",
            objectName = "Painting",
            title = "Chrysanthemums in the Garden at Petit-Gennevilliers",
            artistDisplayName = "Gustave Caillebotte",
            artistDisplayBio = "French, Paris 1848–1894 Gennevilliers",
            artistNationality = "French",
            objectDate = "1893",
            medium = "Oil on canvas",
            dimensions = "39 1/8 × 24 1/4 in. (99.4 × 61.6 cm)",
            objectURL = "https://www.metmuseum.org/art/collection/search/671456",
            GalleryNumber = "824"
        )

        val ENTRIES = listOf(MET_OBJECT)
    }

    var isSuccess = true
    var hasEntries = true

    @Throws(Exception::class)
    override fun loadAllAsFlow(): Flow<List<MetObject>> = flow {
        if (isSuccess) emit(ENTRIES) else throw Exception(message)
    }

    override suspend fun loadAll(): List<MetObject> {
        if (isSuccess) return ENTRIES else throw Exception(message)
    }

    override fun loadByIdAsFlow(id: Int): Flow<MetObject> = flow {
        if (isSuccess) MET_OBJECT else throw Exception(message)
    }

    override fun loadByIdsAsFlow(id: List<Int>): Flow<List<MetObject>> = flow {
        if (isSuccess) ENTRIES else throw Exception(message)
    }

    override fun loadAsFlow(id: Int): Flow<MetObject> = flow {
        if (isSuccess) MET_OBJECT else throw Exception(message)
    }

    override suspend fun load(id: String): MetObject {
        if (isSuccess) return MET_OBJECT else throw Exception(message)
    }

    override suspend fun insertList(metObjects: List<MetObject>) {
        if (isSuccess) return Unit else throw Exception(message)
    }

    override suspend fun insert(metObject: MetObject) {
        if (isSuccess) return Unit else throw Exception(message)
    }

    override suspend fun deleteAll() {
        if (isSuccess) return Unit else throw Exception(message)
    }

    override suspend fun deleteById(id: Int) {
        if (isSuccess) return Unit else throw Exception(message)
    }

    override suspend fun deleteByIds(ids: List<Int>) {
        if (isSuccess) return Unit else throw Exception(message)
    }

    override suspend fun isEmpty(): Boolean = hasEntries
}