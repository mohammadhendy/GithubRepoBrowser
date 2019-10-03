package mohammadhendy.githubrepos.service

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import mohammadhendy.githubrepos.TestRules
import mohammadhendy.githubrepos.api.IGithubApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class ReposServiceTest {

    private val organisation = "square"
    @Rule
    @JvmField val replaceSchedulers = TestRules.ReplaceSchedulers()
    @Mock lateinit var githubApi: IGithubApi

    private lateinit var reposService: IReposService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        reposService = ReposService(githubApi)
    }

    @Test
    fun loadRepos_NoRepos_ReturnEmptyList() {
        whenever(githubApi.getRepos(organisation)).thenReturn(Single.just(emptyList()))
        val testObserver = reposService.loadRepos(organisation).test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun loadRepos_Failed_ReturnError() {
        whenever(githubApi.getRepos(organisation)).thenReturn(Single.error(Throwable("500")))
        val testObserver = reposService.loadRepos(organisation).test()
        testObserver.assertNoValues()
        testObserver.assertError {
            it.message == "500"
        }
    }

    @Test
    fun loadRepos_Success_ReturnBookmarkRepos() {
        val mockUtils = MockUtils()
        whenever(githubApi.getRepos(organisation)).thenReturn(Single.just(mockUtils.mockRepos()))
        val testObserver = reposService.loadRepos(organisation).test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            val repos = mockUtils.mockRepos()
            it[0].repo == repos[0] && !it[0].isBookmarked &&
            it[1].repo == repos[1] && !it[1].isBookmarked &&
            it[2].repo == repos[2] && !it[2].isBookmarked
        }
    }
}