package ua.ulch.artshop.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.data.storage.entities.HolidayEntity

@Dao
abstract class HolidaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun put(entity: HolidayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putAll(entity: List<HolidayEntity>)

    @Query("SELECT * FROM holidays")
    abstract fun observeAll(): Flow<List<HolidayEntity>>

    @Query("SELECT * FROM holidays WHERE month_id = :monthId ORDER BY date")
    abstract fun observeByMonth(monthId: Int): Flow<List<HolidayEntity>>

    @Query("SELECT * FROM holidays WHERE month_id = :monthId ORDER BY date")
    abstract fun readByMonthId(monthId: Int): List<HolidayEntity>

    @Query("SELECT * FROM holidays ORDER BY date")
    abstract suspend fun readAll(): List<HolidayEntity>

    @Query("SELECT * FROM holidays WHERE id = :id")
    abstract fun getHolidayById(id: Int): Flow<HolidayEntity>

    @Query("DELETE FROM holidays")
    abstract suspend fun deleteAll()

    @Query("DELETE FROM holidays WHERE month_id = :monthId")
    abstract suspend fun deleteByMonthId(monthId: Int)

    @Query("DELETE FROM holidays WHERE id = :id")
    abstract suspend fun deleteById(id: Int)
}