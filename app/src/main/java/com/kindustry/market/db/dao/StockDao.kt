package com.kindustry.market.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

import com.kindustry.market.db.entity.Stock

@Dao
interface StockDao {

    @Query("""
    SELECT * 
    FROM company_statistics
    WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR 
      UPPER(:query) == symbol
    """)
    suspend fun getStockListings(query: String) : List<Stock>

    @Query("DELETE FROM company_statistics")
    suspend fun clearStockListings()

    // List Notes
    @Query("SELECT * FROM company_statistics ORDER BY symbol ASC Limit 10")
    fun getAllStock(): Flow<List<Stock>>

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