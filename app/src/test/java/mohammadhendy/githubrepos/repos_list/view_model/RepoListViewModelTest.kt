package mohammadhendy.githubrepos.repos_list.view_model

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import mohammadhendy.githubrepos.TestRules
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.repository.ReposResult
import mohammadhendy.githubrepos.service.MockUtils
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RepoListViewModelTest {

    @Rule @JvmField val replaceSchedulers = TestRules.ReplaceSchedulers()
    @Mock lateinit var repository: IReposRepository

    private lateinit var repoListViewModel: IRepoListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repoListViewModel = createViewModel()
    }

    @Test
    fun getState_FirstIsLoadingState() {
        whenever(repository.repos).thenReturn(Observable.just(ReposResult.Success(emptyMap())))
        val testObserver = repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, RepoListState.Loading)
    }

    @Test
    fun getState_Failed_ReturnEmptyWithError() {
        whenever(repository.repos).thenReturn(Observable.error(Throwable()))
        val testObserver = repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, RepoListState.Loading)
        testObserver.assertValueAt(1) {
            it is RepoListState.Empty && it.error
        }
    }

    @Test
    fun getState_NoRepos_ReturnEmptyState() {
        whenever(repository.repos).thenReturn(Observable.just(ReposResult.Success(emptyMap())))
        val testObserver = repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, RepoListState.Loading)
        testObserver.assertValueAt(1) {
            it is RepoListState.Empty && !it.error
        }
    }

    @Test
    fun getState_Success_ReturnDataState() {
        val mockUtils = MockUtils()
        whenever(repository.repos).thenReturn(
            Observable.just(ReposResult.Success(mockUtils.mockBookmarkReposMap()))
        )
        val testObserver = repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, RepoListState.Loading)
        testObserver.assertValueAt(1) {
            it is RepoListState.Data && it.repoList == mockUtils.mockBookmarkRepos()
        }
    }

    @Test
    fun getState_SuccessAndSupportsTwoPane_NextRouteIsRefreshDetails() {
        repoListViewModel = createViewModel(supportsTwoPane = true)
        val mockUtils = MockUtils()
        whenever(repository.repos).thenReturn(
            Observable.just(ReposResult.Success(mockUtils.mockBookmarkReposMap()))
        )
        val testObserver = repoListViewModel.nextRoute.test()
        repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            it is RepoRoute.RefreshDetails && it.repoId == mockUtils.mockBookmarkRepos()[0].repo.id
        }
    }

    @Test
    fun onRepoItemClicked_SupportsTwoPane_NextRouteIsRefreshDetails() {
        repoListViewModel = createViewModel(supportsTwoPane = true)
        val testObserver = repoListViewModel.nextRoute.test()
        repoListViewModel.onRepoItemClicked(1)
        testObserver.assertNoErrors()
        testObserver.assertValue {
            it is RepoRoute.RefreshDetails && it.repoId == 1
        }
    }

    @Test
    fun onRepoItemClicked_NotSupportsTwoPane_NextRouteIsOpenDetails() {
        val testObserver = repoListViewModel.nextRoute.test()
        repoListViewModel.onRepoItemClicked(1)
        testObserver.assertNoErrors()
        testObserver.assertValue {
            it is RepoRoute.OpenDetails && it.repoId == 1
        }
    }

    private fun createViewModel(supportsTwoPane: Boolean = false) = RepoListViewModel(
        supportsTwoPane = supportsTwoPane,
        reposRepository = repository
    )
}