package com.monu.mobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.monu.mobile.data.local.dao.OfflineCommandDao
import com.monu.mobile.data.local.entity.OfflineCommandEntity

@Database(
    entities = [
        OfflineCommandEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MONULocalDatabase :
    RoomDatabase() {

    abstract fun offlineCommandDao():
        OfflineCommandDao
}
