package com.kindustry.market.db.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

import com.kindustry.market.db.entity.Equity

@Dao
interface EquityDao {

    @RawQuery(observedEntities = [Equity::class])
    fun getEquityList(query: SupportSQLiteQuery): Flow<List<Equity>>

    // Select Annotation
    @Query("""
    SELECT * 
    FROM equity_statistics
    WHERE exchange =:exchange and sector =:sector  Limit 40
    """)
    fun getEquityListings(exchange: String?, sector: String?) : Flow<List<Equity>>

    // 市場区分
    @Query("SELECT '' UNION ALL SELECT distinct exchange FROM equity_statistics ORDER BY 1 ASC")
    fun getAllExchange(): Flow<List<String>>

    // 業種　東証業種名
    @Query("SELECT '' UNION ALL SELECT distinct sector FROM equity_statistics ORDER BY 1 ASC")
    fun getAllSector(): Flow<List<String>>

    @Query("SELECT * FROM equity_statistics WHERE delisting_date is NULL ORDER BY RANDOM() Limit 50")
    fun getRandomEquity(): Flow<List<Equity>>

    @Query("SELECT * FROM equity_statistics WHERE symbol=:symbol")
    fun getEquityBySymbol(symbol: String): Flow<Equity>

    @Query("SELECT * FROM equity_statistics WHERE symbol LIKE '%' || :codeOrName || '%' OR name LIKE '%' || :codeOrName || '%'")
    fun findEquityBySymbolOrName(codeOrName: String) : Flow<List<Equity>>

    // Insert Notes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: Equity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquityListings(equityListingEntities: List<Equity>)

    // Update Annotation
    @Update
    suspend fun updateNote(noteModel: Equity)

    // Delete Annotation
    @Delete
    suspend fun deleteNote(noteModel: Equity)
}