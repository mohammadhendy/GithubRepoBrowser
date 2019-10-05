package mohammadhendy.githubrepos.repos_list.view_model

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.repository.ReposResult
import mohammadhendy.githubrepos.service.model.BookmarkRepo

class RepoListViewModel(
    private val supportsTwoPane: Boolean,
    private val reposRepository: IReposRepository
) : IRepoListViewModel {

    private val nextRouteRelay = PublishRelay.create<RepoRoute>()
    private val selectedRepoIdRelay = BehaviorRelay.create<Int>()

    override val state: Observable<RepoListState>
        get() = reposRepository.repos
        .map { result ->
            when(result) {
                is ReposResult.Success -> {
                    if (result.reposMap.isNullOrEmpty()) {
                        emptyState()
                    } else {
                        val reposList = result.reposMap.values.toList()
                        if (supportsTwoPane) {
                            selectedRepoIdRelay.accept(reposList[0].repo.id)
                            nextRouteRelay.accept(RepoRoute.RefreshDetails(reposList[0].repo.id))
                        }
                        RepoListState.Data(reposList)
                    }
                }
                is ReposResult.Failure -> {
                    emptyState(true)
                }
            }
        }
        .startWith(RepoListState.Loading)

    override val nextRoute: Observable<RepoRoute> = nextRouteRelay.hide()

    override val repoChanged: Observable<BookmarkRepo> = reposRepository.bookmarkChanges

    override val selectedRepoId: Observable<Int> = selectedRepoIdRelay.hide().distinctUntilChanged()

    override fun onRepoItemClicked(repoId: Int) {
        if (supportsTwoPane) {
            if (repoId != selectedRepoIdRelay.value) {
                selectedRepoIdRelay.accept(repoId)
                nextRouteRelay.accept(RepoRoute.RefreshDetails(repoId))
            }

        } else {
            nextRouteRelay.accept(RepoRoute.OpenDetails(repoId))
        }
    }

    override fun onRefresh() {
        reposRepository.reloadRepos()
    }

    private fun emptyState(error: Boolean = false) : RepoListState {
        if (supportsTwoPane) {
            nextRouteRelay.accept(RepoRoute.HideDetails)
        }
        return RepoListState.Empty(error = error)
    }
}