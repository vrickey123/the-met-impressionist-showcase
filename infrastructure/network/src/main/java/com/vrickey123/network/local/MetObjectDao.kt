package com.vrickey123.network.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vrickey123.model.api.MetObject
import kotlinx.coroutines.flow.Flow

@Dao
interface MetObjectDAO {
    @Query("SELECT * FROM metobject")
    fun getAllAsFlow(): Flow<List<MetObject>>

    @Query("SELECT * FROM metobject")
    suspend fun getAll(): List<MetObject>

    @Query("SELECT * FROM metobject WHERE objectID = :objectID")
    fun getAsFlow(objectID: Int): Flow<MetObject>

    @Insert
    suspend fun insert(metObject: MetObject)

    @Insert
    suspend fun insertAll(metObjects: List<MetObject>)

    @Delete
    suspend fun delete(metObject: MetObject)
}