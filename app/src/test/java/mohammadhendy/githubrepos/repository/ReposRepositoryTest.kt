package mohammadhendy.githubrepos.repository

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import mohammadhendy.githubrepos.TestRules
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.MockUtils
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ReposRepositoryTest {

    private val organisation = "square"
    @Rule
    @JvmField val replaceSchedulers = TestRules.ReplaceSchedulers()
    @Mock
    lateinit var reposService: IReposService

    private lateinit var reposRepository: IReposRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        reposRepository = ReposRepository(organisation, reposService)
    }

    @Test
    fun getRepos_NoRepos_ReturnEmptyList() {
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(emptyList()))
        val testObserver = reposRepository.repos.test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            it is ReposResult.Success && it.reposMap.isEmpty()
        }
    }

    @Test
    fun getRepos_Failed_ReturnError() {
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.error(Throwable("500")))
        val testObserver = reposRepository.repos.test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            it is ReposResult.Failure && it.error.message == "500"
        }
    }

    @Test
    fun getRepos_Success_ReturnBookmarkRepos() {
        val mockUtils = MockUtils()
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
        val testObserver = reposRepository.repos.test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            val repos = mockUtils.mockRepos()
            it is ReposResult.Success &&
            it.reposMap[1]?.repo == repos[0] && it.reposMap[1]?.isBookmarked == false &&
            it.reposMap[2]?.repo == repos[1] && it.reposMap[2]?.isBookmarked == true &&
            it.reposMap[3]?.repo == repos[2] && it.reposMap[3]?.isBookmarked == true
        }
    }

    @Test
    fun reloadRepos_NextTimeAfterSuccess_ReturnUpdatedBookmarkRepos() {
        val mockUtils = MockUtils()
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
        val testObserver = reposRepository.repos.test()

        val updatedRepos = mockUtils.mockBookmarkRepos()
        updatedRepos[0].isBookmarked = true
        updatedRepos[1].isBookmarked = false
        updatedRepos[2].isBookmarked = false
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(updatedRepos))
        reposRepository.reloadRepos()

        testObserver.assertNoErrors()
        testObserver.assertValueAt(0) {
            val repos = mockUtils.mockRepos()
            it is ReposResult.Success &&
                    it.reposMap[1]?.repo == repos[0] && it.reposMap[1]?.isBookmarked == false &&
                    it.reposMap[2]?.repo == repos[1] && it.reposMap[2]?.isBookmarked == true &&
                    it.reposMap[3]?.repo == repos[2] && it.reposMap[3]?.isBookmarked == true
        }
        testObserver.assertValueAt(1) {
            val repos = mockUtils.mockRepos()
            it is ReposResult.Success &&
                    it.reposMap[1]?.repo == repos[0] && it.reposMap[1]?.isBookmarked == true &&
                    it.reposMap[2]?.repo == repos[1] && it.reposMap[2]?.isBookmarked == false &&
                    it.reposMap[3]?.repo == repos[2] && it.reposMap[3]?.isBookmarked == false
        }
    }

    @Test
    fun bookmarkChanges_RepoBookmarkAddedSuccess_ReturnBookmarkRepo() {
        val mockUtils = MockUtils()
        val repoId = 1
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
        whenever(reposService.addRepoToBookmarks(repoId)).thenReturn(Completable.complete())
        reposRepository.repos.test()

        val testObserver = reposRepository.bookmarkChanges.test()
        reposRepository.addRepoToBookmarks(repoId).test()

        testObserver.assertNoErrors()
        testObserver.assertValue {
            it.isBookmarked && it.repo.id == repoId
        }
    }

    @Test
    fun bookmarkChanges_RepoBookmarkAddedFailure_ReturnNothing() {
        val mockUtils = MockUtils()
        val repoId = 1
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
        whenever(reposService.addRepoToBookmarks(repoId)).thenReturn(Completable.error(Throwable()))
        reposRepository.repos.test()

        val testObserver = reposRepository.bookmarkChanges.test()
        reposRepository.addRepoToBookmarks(repoId).test()

        testObserver.assertNoErrors()
        testObserver.assertNoValues()
    }

    @Test
    fun bookmarkChanges_RepoBookmarkRemovedSuccess_ReturnBookmarkRepo() {
        val mockUtils = MockUtils()
        val repoId = 2
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
        whenever(reposService.removeRepoFromBookmarks(repoId)).thenReturn(Completable.complete())
        reposRepository.repos.test()

        val testObserver = reposRepository.bookmarkChanges.test()
        reposRepository.removeRepoFromBookmarks(repoId).test()

        testObserver.assertNoErrors()
        testObserver.assertValue {
            !it.isBookmarked && it.repo.id == repoId
        }
    }

    @Test
    fun bookmarkChanges_RepoBookmarkRemovedFailure_ReturnNothing() {
        val mockUtils = MockUtils()
        val repoId = 2
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
        whenever(reposService.removeRepoFromBookmarks(repoId)).thenReturn(Completable.error(Throwable()))
        reposRepository.repos.test()

        val testObserver = reposRepository.bookmarkChanges.test()
        reposRepository.removeRepoFromBookmarks(repoId).test()

        testObserver.assertNoErrors()
        testObserver.assertNoValues()
    }
}