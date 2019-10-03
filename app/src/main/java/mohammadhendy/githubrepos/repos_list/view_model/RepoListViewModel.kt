package mohammadhendy.githubrepos.repos_list.view_model

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import mohammadhendy.githubrepos.service.IReposService

class RepoListViewModel(
    private val supportsTwoPane: Boolean,
    private val organisation: String,
    private val reposService: IReposService
) : IRepoListViewModel {

    private val nextRouteRelay = PublishRelay.create<RepoRoute>()

    override val state: Observable<RepoListState>
        get() = reposService.loadRepos(organisation)
        .map {
            if (it.isNullOrEmpty()) {
                RepoListState.Empty()
            } else {
                if (supportsTwoPane) {
                    nextRouteRelay.accept(RepoRoute.RefreshDetails(it[0].repo.id))
                }
                RepoListState.Data(it)
            }
        }
        .onErrorReturnItem(RepoListState.Empty(error = true))
        .startWith(RepoListState.Loading)
        .distinctUntilChanged()

    override val nextRoute: Observable<RepoRoute> = nextRouteRelay.hide()

    override fun onRepoItemClicked(repoId: Int) {
        if (supportsTwoPane) {
            nextRouteRelay.accept(RepoRoute.RefreshDetails(repoId))
        } else {
            nextRouteRelay.accept(RepoRoute.OpenDetails(repoId))
        }
    }
}