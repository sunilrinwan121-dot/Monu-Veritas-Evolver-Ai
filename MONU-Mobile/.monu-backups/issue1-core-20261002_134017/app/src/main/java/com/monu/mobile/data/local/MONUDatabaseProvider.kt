package com.monu.mobile.data.local

import android.content.Context
import androidx.room.Room

object MONUDatabaseProvider {

    @Volatile
    private var instance:
        MONULocalDatabase? = null

    fun get(
        context: Context
    ): MONULocalDatabase {

        return instance ?: synchronized(this) {

            instance ?: Room.databaseBuilder(
                context.applicationContext,
                MONULocalDatabase::class.java,
                "monu_mobile.db"
            ).build().also {
                instance = it
            }
        }
    }
}
