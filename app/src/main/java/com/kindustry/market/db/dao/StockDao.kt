package com.kindustry.market.db.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

import com.kindustry.market.db.entity.Stock

@Dao
interface StockDao {

    @RawQuery(observedEntities = [Stock::class])
    fun getStockList(query: SupportSQLiteQuery): Flow<List<Stock>>

    @Query("""
    SELECT * 
    FROM company_statistics
    WHERE exchange =:exchange and sector =:sector  Limit 40
    """)
//    suspend fun getStockListings(query: String) : List<Stock>
    fun getStockListings(exchange: String?, sector: String?) : Flow<List<Stock>>

    // 市場区分
    @Query("SELECT '' UNION ALL SELECT distinct exchange FROM company_statistics ORDER BY 1 ASC")
    fun getAllExchange(): Flow<List<String>>

    // 業種　東証業種名
    @Query("SELECT '' UNION ALL SELECT distinct sector FROM company_statistics ORDER BY 1 ASC")
    fun getAllSector(): Flow<List<String>>

    @Query("SELECT * FROM company_statistics ORDER BY RANDOM() Limit 50")
    fun getRandomStock(): Flow<List<Stock>>

    // Select Annotation
    @Query("SELECT * FROM company_statistics WHERE symbol=:noteID")
    fun selectNoteID(noteID: Int): Flow<Stock?>

    // Insert Notes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: Stock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockListings(stockListingEntities: List<Stock>)

    // Update Annotation
    @Update
    suspend fun updateNote(noteModel: Stock)

    // Delete Annotation
    @Delete
    suspend fun deleteNote(noteModel: Stock)
}