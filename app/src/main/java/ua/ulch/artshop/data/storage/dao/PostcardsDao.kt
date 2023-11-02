package ua.ulch.artshop.data.storage.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.data.storage.entities.PostcardEntity

@Dao
abstract class PostcardsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putAll(entity: List<PostcardEntity>)

    @Query("SELECT * FROM postcards")
    abstract fun observeAll(): Flow<List<PostcardEntity>>

    @Query("SELECT * FROM postcards WHERE category_id = :categoryId")
    abstract fun observeByCategoryId(categoryId: Int): Flow<List<PostcardEntity>>

    @Query("SELECT * FROM postcards WHERE category_id = :categoryId ORDER BY id DESC")
    abstract fun observePagedByCategoryId(categoryId: Int): PagingSource<Int, PostcardEntity>

    @Query("SELECT * FROM postcards")
    abstract suspend fun readAll(): List<PostcardEntity>

    @Query("SELECT * FROM postcards WHERE category_id = :categoryId")
    abstract fun readByCategoryId(categoryId: Int): List<PostcardEntity>

    @Query("DELETE FROM postcards")
    abstract suspend fun deleteAll()

    @Query("DELETE FROM postcards WHERE category_id = :categoryId")
    abstract suspend fun deleteByCategoryId(categoryId: Int)
}