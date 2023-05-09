package com.vrickey123.network.local

import com.vrickey123.met_api.MetObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMetObjectDao: MetObjectDAO {

    companion object {
        val metObject = MetObject(
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

        val entries = listOf(metObject)
    }

    override fun loadAllAsFlow(): Flow<List<MetObject>> = flowOf(entries)

    override suspend fun loadAll(): List<MetObject> = entries

    override fun loadByIdAsFlow(id: Int): Flow<MetObject> = flowOf(metObject)

    override fun loadByIdsAsFlow(id: List<Int>): Flow<List<MetObject>> = flowOf(entries)

    override fun loadAsFlow(id: Int): Flow<MetObject> = flowOf(metObject)

    override suspend fun load(id: String): MetObject = metObject

    override suspend fun insertList(metObjects: List<MetObject>) = Unit

    override suspend fun insert(metObject: MetObject) = Unit

    override suspend fun deleteAll() = Unit

    override suspend fun deleteById(id: Int) = Unit

    override suspend fun deleteByIds(ids: List<Int>) = Unit

    override suspend fun isEmpty(): Boolean = false
}