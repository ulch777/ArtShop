package ua.ulch.artshop.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.ulch.artshop.data.storage.entities.CategoryEntity

@Dao
abstract class CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putAll(entity: List<CategoryEntity>)

    @Query("SELECT * FROM categories")
    abstract fun observeAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE checked = 1 ORDER BY order_number")
    abstract fun observeChecked(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE parent = :parent")
    abstract fun observeByParent(parent: Int): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories")
    abstract suspend fun readAll(): List<CategoryEntity>

    @Query("DELETE FROM categories")
    abstract suspend fun deleteAll()
}