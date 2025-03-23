package com.kindustry.market.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kindustry.market.db.dao.EquityDao
import com.kindustry.market.db.entity.Equity

// The @Database annotation requires arguments so that Room can create the database
// After list the database entities and the version number
@Database(
    // list App entities
    entities = [
        Equity::class
    ],
    // database version
    version = 1,
    // export DB declare with false to not keep DB
    exportSchema = false
)
abstract class MarketDatabase : RoomDatabase() {
    abstract val equityDao : EquityDao
//    abstract val equityItemDao : EquityItemDao
}