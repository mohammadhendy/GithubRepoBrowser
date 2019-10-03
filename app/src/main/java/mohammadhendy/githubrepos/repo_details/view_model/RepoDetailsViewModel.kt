package mohammadhendy.githubrepos.repo_details.view_model

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.service.model.BookmarkRepo

class RepoDetailsViewModel(
    private val repoId: Int,
    private val reposRepository: IReposRepository
) : IRepoDetailsViewModel {

    private val bookmarkStateRelay = BehaviorRelay.create<BookmarkState>()

    override val bookmarkRepo: BookmarkRepo? by lazy {
        val repo = reposRepository.getRepo(repoId)
        repo?.apply {
            bookmarkStateRelay.accept(mapToState(this.isBookmarked))
        }
        repo
    }

    override val bookmarkState: Observable<BookmarkState> = bookmarkStateRelay.flatMap {
        if (it is BookmarkState.Loading) {
            addOrRemoveBookmark(it)
        } else {
            Observable.just(it)
        }
    }

    override fun onAddRemoveBookmarkClicked() {
        bookmarkRepo?.let {
            bookmarkStateRelay.accept(BookmarkState.Loading(it.isBookmarked))
        }
    }

    private fun addOrRemoveBookmark(loading: BookmarkState.Loading): Observable<BookmarkState> = if (loading.remove) {
        reposRepository
            .removeRepoFromBookmarks(repoId)
            .andThen(Observable.just<BookmarkState>(BookmarkState.Removed))
            .onErrorReturnItem(BookmarkState.Added)
            .startWith(loading)
    } else {
        reposRepository
            .addRepoToBookmarks(repoId)
            .andThen(Observable.just<BookmarkState>(BookmarkState.Added))
            .onErrorReturnItem(BookmarkState.Removed)
            .startWith(loading)
    }

    private fun mapToState(isBookmarked: Boolean) = if (isBookmarked) BookmarkState.Added else BookmarkState.Removed
}