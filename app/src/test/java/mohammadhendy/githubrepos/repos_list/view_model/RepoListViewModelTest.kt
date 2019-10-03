package mohammadhendy.githubrepos.repos_list.view_model

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import mohammadhendy.githubrepos.TestRules
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.MockUtils
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.reflect.jvm.internal.impl.resolve.scopes.MemberScope

class RepoListViewModelTest {

    private val organisation = "square"
    @Rule @JvmField val replaceSchedulers = TestRules.ReplaceSchedulers()
    @Mock lateinit var reposService: IReposService

    private lateinit var repoListViewModel: IRepoListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repoListViewModel = createViewModel()
    }

    @Test
    fun getState_FirstIsLoadingState() {
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(emptyList()))
        val testObserver = repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, RepoListState.Loading)
    }

    @Test
    fun getState_Failed_ReturnEmptyWithError() {
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.error(Throwable()))
        val testObserver = repoListViewModel.state.test()
        testObserver.assertNoErrors()
        testObserver.assertValueAt(0, RepoListState.Loading)
        testObserver.assertValueAt(1) {
            it is RepoListState.Empty && it.error
        }
    }

    @Test
    fun getState_NoRepos_ReturnEmptyState() {
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(emptyList()))
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
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
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
        whenever(reposService.loadRepos(organisation)).thenReturn(Observable.just(mockUtils.mockBookmarkRepos()))
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
        organisation = organisation,
        reposService = reposService
    )
}