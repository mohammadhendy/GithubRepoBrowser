package mohammadhendy.githubrepos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import mohammadhendy.githubrepos.database.model.Bookmark

@Database(entities = arrayOf(Bookmark::class), version = 1)
abstract class BookmarksDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): IBookmarkDao
}