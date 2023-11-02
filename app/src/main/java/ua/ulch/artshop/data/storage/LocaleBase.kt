package ua.ulch.artshop.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.ulch.artshop.data.storage.dao.CategoriesDao
import ua.ulch.artshop.data.storage.dao.FavoritesDao
import ua.ulch.artshop.data.storage.dao.HolidaysDao
import ua.ulch.artshop.data.storage.dao.MonthsDao
import ua.ulch.artshop.data.storage.dao.PostcardRemoteKeysDao
import ua.ulch.artshop.data.storage.dao.PostcardsDao
import ua.ulch.artshop.data.storage.entities.CategoryEntity
import ua.ulch.artshop.data.storage.entities.FavoriteEntity
import ua.ulch.artshop.data.storage.entities.HolidayEntity
import ua.ulch.artshop.data.storage.entities.MonthEntity
import ua.ulch.artshop.data.storage.entities.PostcardEntity
import ua.ulch.artshop.data.storage.entities.PostcardRemoteKeys

@Database(
    entities = [
        FavoriteEntity::class,
        CategoryEntity::class,
        PostcardEntity::class,
        MonthEntity::class,
        HolidayEntity::class,
        PostcardRemoteKeys::class
    ],
    version = 3,
    exportSchema = true,
)
abstract class LocaleBase : RoomDatabase() {
    abstract val favoritesDao: FavoritesDao
    abstract val categoriesDao: CategoriesDao
    abstract val postcardsDao: PostcardsDao
    abstract val monthsDao: MonthsDao
    abstract val holidaysDao: HolidaysDao
    abstract val postcardRemoteKeysDao: PostcardRemoteKeysDao
}