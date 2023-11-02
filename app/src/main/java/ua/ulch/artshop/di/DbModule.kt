package ua.ulch.artshop.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.ulch.artshop.data.storage.LocaleBase
import ua.ulch.artshop.data.storage.dao.CategoriesDao
import ua.ulch.artshop.data.storage.dao.FavoritesDao
import javax.inject.Singleton

const val NAME_DAO = "locale_bd"

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): LocaleBase {
        return databaseBuilder(
            context.applicationContext,
            LocaleBase::class.java,
            NAME_DAO
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(dao: LocaleBase): FavoritesDao {
        return dao.favoritesDao
    }

    @Singleton
    @Provides
    fun provideLocaleDao(dao: LocaleBase): CategoriesDao {
        return dao.categoriesDao
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE favorites ADD COLUMN locale TEXT")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `categories`")
        db.execSQL("DROP TABLE IF EXISTS `holidays`")
        db.execSQL("DROP TABLE IF EXISTS `months`")
        db.execSQL("DROP TABLE IF EXISTS `postcards`")
        db.execSQL("DROP TABLE IF EXISTS `postcard_remote_keys`")

        db.execSQL(
            "CREATE TABLE `categories` (" +
                    "`id` INTEGER DEFAULT 0 NOT NULL, " +
                    "`name` TEXT, " +
                    "`title` TEXT, `image` TEXT, " +
                    "`order_number` INTEGER DEFAULT 0 NOT NULL, " +
                    "`parent` INTEGER DEFAULT 0 NOT NULL," +
                    "`checked` INTEGER DEFAULT 0 NOT NULL, " +
                    "PRIMARY KEY(`id`))"
        )
        db.execSQL("CREATE INDEX index_categories_checked ON `categories`(`checked`)")

        db.execSQL(
            "CREATE TABLE `holidays` (" +
                    "`id` INTEGER DEFAULT 0 NOT NULL, " +
                    "`title` TEXT, " +
                    "`image_url` TEXT, " +
                    "`media_link` INTEGER, " +
                    "`category` TEXT, " +
                    "`date` TEXT, " +
                    "`month_id` INTEGER, " +
                    "`description` TEXT, " +
                    "PRIMARY KEY(`id`))"
        )
        db.execSQL(
            "CREATE TABLE `months` (" +
                    "`id` INTEGER DEFAULT 0 NOT NULL, " +
                    "`name` TEXT, " +
                    "`image_url` TEXT," +
                    "`index_number` INTEGER DEFAULT 0 NOT NULL, " +
                    "PRIMARY KEY(`id`))"
        )
        db.execSQL(
            "CREATE TABLE `postcards` (" +
                    "`id` INTEGER DEFAULT 0 NOT NULL, " +
                    "`image_url` TEXT, " +
                    "`web_url` TEXT, " +
                    "`category_id` INTEGER, " +
                    "`subcategory_id` INTEGER, " +
                    "PRIMARY KEY(`id`))"
        )
        db.execSQL(
            "CREATE TABLE `postcard_remote_keys` (" +
                    "`id` INTEGER DEFAULT 0 NOT NULL, " +
                    "`prevKey` INTEGER, " +
                    "`nextKey` INTEGER, " +
                    "PRIMARY KEY(`id`))"
        )
    }
}
