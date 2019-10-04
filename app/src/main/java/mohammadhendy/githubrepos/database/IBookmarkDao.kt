package mohammadhendy.githubrepos.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import mohammadhendy.githubrepos.database.model.Bookmark

@Dao
interface IBookmarkDao {

    @Query("SELECT is_bookmarked FROM bookmark WHERE repo_id = (:repoId)")
    fun isBookmarked(repoId: Int): Single<Boolean>

    @Query("SELECT * FROM bookmark WHERE repo_id IN (:repoIds)")
    fun isBookmarked(repoIds: IntArray): Single<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark) : Completable
}
