package com.vrickey123.network.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vrickey123.model.api.MetObject
import kotlinx.coroutines.flow.Flow

@Dao
interface MetObjectDAO {
    @Query("SELECT * FROM metobject")
    fun loadAllAsFlow(): Flow<List<MetObject>>

    @Query("SELECT * FROM metobject")
    suspend fun loadAll(): List<MetObject>

    @Query("SELECT * FROM metobject WHERE objectID = :id")
    fun loadByIdAsFlow(id: Int): Flow<MetObject>

    @Query("SELECT * FROM metobject WHERE objectID = :id")
    fun loadByIdsAsFlow(id: List<Int>): Flow<List<MetObject>>

    @Query("SELECT * FROM metobject WHERE objectID = :id")
    fun loadAsFlow(id: Int): Flow<MetObject>

    @Query("SELECT * FROM metobject WHERE objectID = :id")
    suspend fun load(id: String): MetObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(metObjects: List<MetObject>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metObject: MetObject)

    @Query("DELETE FROM metObject")
    suspend fun deleteAll()

    @Query("DELETE FROM metObject WHERE objectID = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM metObject WHERE objectID = :ids")
    suspend fun deleteByIds(ids: List<Int>)

    @Query("SELECT (SELECT COUNT(*) FROM metObject) == 0")
    suspend fun isEmpty(): Boolean
}