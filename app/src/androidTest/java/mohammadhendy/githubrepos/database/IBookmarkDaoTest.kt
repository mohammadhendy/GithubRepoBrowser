package mohammadhendy.githubrepos.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import mohammadhendy.githubrepos.TestRules
import mohammadhendy.githubrepos.database.model.Bookmark
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class IBookmarkDaoTest {

    @Rule
    @JvmField val replaceSchedulers = TestRules.ReplaceSchedulers()

    private lateinit var db: BookmarksDatabase
    private lateinit var bookmarkDao: IBookmarkDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BookmarksDatabase::class.java).build()
        bookmarkDao = db.bookmarkDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun isBookmarked_NoBookmarksExists_ReturnEmptyList() {
        val testObserver = bookmarkDao.isBookmarked(intArrayOf(1,2,3)).test()
        testObserver.assertNoErrors()
        testObserver.assertValue(emptyList())
    }

    @Test
    fun isBookmarked_SomeBookmarksExist_ReturnSmallerBookmarkList() {
        mockBookmarks().forEach {
            bookmarkDao.insertBookmark(it).test()
        }
        val testObserver = bookmarkDao.isBookmarked(intArrayOf(1,2,3,4)).test()
        testObserver.assertNoErrors()
        testObserver.assertValue(mockBookmarks())
    }

    @Test
    fun insertBookmark_ExistingBookmarkIsReplaced() {
        bookmarkDao.insertBookmark(Bookmark(1, true)).test()
        bookmarkDao.insertBookmark(Bookmark(1, false)).test()
        val testObserver = bookmarkDao.isBookmarked(intArrayOf(1)).test()
        testObserver.assertNoErrors()
        testObserver.assertValue(listOf(Bookmark(1, false)))
    }

    private fun mockBookmarks() : List<Bookmark> = listOf(
        Bookmark(1, false),
        Bookmark(2, true),
        Bookmark(3, true)
    )
}