package com.monu.mobile.data.local.dao

import androidx.room.*
import com.monu.mobile.data.local.entity.OfflineCommandEntity

@Dao
interface OfflineCommandDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(
        command: OfflineCommandEntity
    )

    @Query(
        "SELECT * FROM offline_commands ORDER BY createdAt ASC"
    )
    suspend fun getAll():
        List<OfflineCommandEntity>

    @Query(
        """
        SELECT * FROM offline_commands
        WHERE syncState IN ('PENDING', 'RETRY_PENDING')
        ORDER BY createdAt ASC
        """
    )
    suspend fun getPending():
        List<OfflineCommandEntity>

    @Update
    suspend fun update(
        command: OfflineCommandEntity
    )

    @Query(
        "DELETE FROM offline_commands WHERE id = :id"
    )
    suspend fun deleteById(
        id: String
    )

    @Query(
        "SELECT COUNT(*) FROM offline_commands"
    )
    suspend fun count(): Int
}
