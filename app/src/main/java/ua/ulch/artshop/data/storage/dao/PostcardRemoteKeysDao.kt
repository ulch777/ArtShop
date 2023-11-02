package ua.ulch.artshop.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.ulch.artshop.data.storage.entities.PostcardRemoteKeys

@Dao
abstract class PostcardRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(remoteKey: List<PostcardRemoteKeys>)

    @Query("SELECT * FROM postcard_remote_keys WHERE id = :id")
    abstract suspend fun remoteKeysPostcardId(id: Int): PostcardRemoteKeys?

    @Query("DELETE FROM postcard_remote_keys")
    abstract suspend fun clearRemoteKeys()
}