package mohammadhendy.githubrepos.repo_details.view_model

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import mohammadhendy.githubrepos.TestRules
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.service.MockUtils
import org.junit.Assert.assertNotNull
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RepoDetailsViewModelTest {

    @Rule
    @JvmField val replaceSchedulers = TestRules.ReplaceSchedulers()
    @Mock
    lateinit var repository: IReposRepository

    private lateinit var viewModel: IRepoDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = createViewModel()
    }

    @Test
    fun getBookmarkState_RepoBookmarked_ReturnAdded() {
        val repoId = 2
        viewModel = createViewModel(repoId)
        val repo = MockUtils().mockBookmarkRepos()[1]
        whenever(repository.getRepo(repoId)).thenReturn(repo)
        val testObserver = viewModel.bookmarkState.test()
        assertNotNull(viewModel.bookmarkRepo)
        testObserver.assertNoErrors()
        testObserver.assertValue(BookmarkState.Added)
    }

    @Test
    fun getBookmarkState_RepoNotBookmarked_ReturnRemoved() {
        val repoId = 1
        viewModel = createViewModel(repoId)
        val repo = MockUtils().mockBookmarkRepos()[0]
        whenever(repository.getRepo(repoId)).thenReturn(repo)
        val testObserver = viewModel.bookmarkState.test()
        assertNotNull(viewModel.bookmarkRepo)
        testObserver.assertNoErrors()
        testObserver.assertValue(BookmarkState.Removed)
    }

    @Test
    fun getBookmarkState_AddBookmarkSuccess_ReturnLoadingThenAdded() {
        val repoId = 1
        viewModel = createViewModel(repoId)
        val repo = MockUtils().mockBookmarkRepos()[0]
        whenever(repository.getRepo(repoId)).thenReturn(repo)
        whenever(repository.addRepoToBookmarks(repoId)).thenReturn(Completable.complete())
        val testObserver = viewModel.bookmarkState.test()
        assertNotNull(viewModel.bookmarkRepo)
        viewModel.onAddRemoveBookmarkClicked()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, BookmarkState.Removed)
        testObserver.assertValueAt(1) { it is BookmarkState.Loading && !it.remove }
        testObserver.assertValueAt(2, BookmarkState.Added)
    }

    @Test
    fun getBookmarkState_AddBookmarkFailure_ReturnLoadingThenRemoved() {
        val repoId = 1
        viewModel = createViewModel(repoId)
        val repo = MockUtils().mockBookmarkRepos()[0]
        whenever(repository.getRepo(repoId)).thenReturn(repo)
        whenever(repository.addRepoToBookmarks(repoId)).thenReturn(Completable.error(Throwable()))
        val testObserver = viewModel.bookmarkState.test()
        assertNotNull(viewModel.bookmarkRepo)
        viewModel.onAddRemoveBookmarkClicked()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, BookmarkState.Removed)
        testObserver.assertValueAt(1) { it is BookmarkState.Loading && !it.remove }
        testObserver.assertValueAt(2, BookmarkState.Removed)
    }

    @Test
    fun getBookmarkState_RemoveBookmarkSuccess_ReturnLoadingThenRemoved() {
        val repoId = 2
        viewModel = createViewModel(repoId)
        val repo = MockUtils().mockBookmarkRepos()[1]
        whenever(repository.getRepo(repoId)).thenReturn(repo)
        whenever(repository.removeRepoFromBookmarks(repoId)).thenReturn(Completable.complete())
        val testObserver = viewModel.bookmarkState.test()
        assertNotNull(viewModel.bookmarkRepo)
        viewModel.onAddRemoveBookmarkClicked()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, BookmarkState.Added)
        testObserver.assertValueAt(1) { it is BookmarkState.Loading && it.remove }
        testObserver.assertValueAt(2, BookmarkState.Removed)
    }

    @Test
    fun getBookmarkState_RemoveBookmarkFailure_ReturnLoadingThenAdded() {
        val repoId = 2
        viewModel = createViewModel(repoId)
        val repo = MockUtils().mockBookmarkRepos()[1]
        whenever(repository.getRepo(repoId)).thenReturn(repo)
        whenever(repository.removeRepoFromBookmarks(repoId)).thenReturn(Completable.error(Throwable()))
        val testObserver = viewModel.bookmarkState.test()
        assertNotNull(viewModel.bookmarkRepo)
        viewModel.onAddRemoveBookmarkClicked()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, BookmarkState.Added)
        testObserver.assertValueAt(1) { it is BookmarkState.Loading && it.remove }
        testObserver.assertValueAt(2, BookmarkState.Added)
    }

    @Test
    fun onAddRemoveBookmarkClicked() {
    }

    private fun createViewModel(repoId: Int = 1) = RepoDetailsViewModel(repoId, repository)
}