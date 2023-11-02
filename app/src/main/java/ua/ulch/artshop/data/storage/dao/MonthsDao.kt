package ua.ulch.artshop.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.data.storage.entities.MonthEntity

@Dao
abstract class MonthsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putAll(entity: List<MonthEntity>)

    @Query("SELECT * FROM months")
    abstract fun observeAll(): Flow<List<MonthEntity>>

    @Query("SELECT * FROM months")
    abstract suspend fun readAll(): List<MonthEntity>

    @Query("SELECT * FROM months WHERE index_number = :index")
    abstract suspend fun getMonthByIndex(index: Int): MonthEntity

    @Query("DELETE FROM months")
    abstract suspend fun deleteAll()
}