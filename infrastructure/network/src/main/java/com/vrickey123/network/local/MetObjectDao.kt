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
    fun getAll(): Flow<List<MetObject>>

    @Insert
    suspend fun insertMetObject(metObject: MetObject)

    @Insert
    suspend fun insertAll(metObject: MetObject)

    @Delete
    suspend fun deleteMetObject(metObject: MetObject)
}