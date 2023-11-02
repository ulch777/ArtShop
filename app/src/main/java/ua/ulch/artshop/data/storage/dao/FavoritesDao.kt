package ua.ulch.artshop.data.storage.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.data.storage.entities.FavoriteEntity

@Dao
abstract class FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: FavoriteEntity)

    @Query("SELECT * FROM favorites WHERE locale = :locale")
    abstract fun observeByLocale(locale: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE locale = :locale")
    abstract fun observePagedByLocale(locale: String): PagingSource<Int, FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE locale = :locale")
    abstract fun readByLocale(locale: String): List<FavoriteEntity>

    @Query("DELETE FROM favorites WHERE id = :id")
    abstract suspend fun deleteById(id: Int?)
}