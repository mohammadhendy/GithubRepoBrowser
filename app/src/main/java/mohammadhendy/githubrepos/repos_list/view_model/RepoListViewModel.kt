package mohammadhendy.githubrepos.repos_list.view_model

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.service.model.BookmarkRepo

class RepoListViewModel(
    private val supportsTwoPane: Boolean,
    private val reposRepository: IReposRepository
) : IRepoListViewModel {

    private val nextRouteRelay = PublishRelay.create<RepoRoute>()

    override val state: Observable<RepoListState>
        get() = reposRepository.repos
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

    override val repoChanged: Observable<BookmarkRepo> = reposRepository.bookmarkChanges

    override fun onRepoItemClicked(repoId: Int) {
        if (supportsTwoPane) {
            nextRouteRelay.accept(RepoRoute.RefreshDetails(repoId))
        } else {
            nextRouteRelay.accept(RepoRoute.OpenDetails(repoId))
        }
    }
}