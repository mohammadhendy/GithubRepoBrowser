package mohammadhendy.githubrepos.repository

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.model.BookmarkRepo
import javax.inject.Inject

class ReposRepository(
    private val organisation: String,
    private val reposService: IReposService
) : IReposRepository {

    var disposable: Disposable? = null

    private val reposRelay = BehaviorRelay.create<ReposResult>()
    private val bookmarkChangeRelay by lazy { PublishRelay.create<BookmarkRepo>() }

    override val repos: Observable<ReposResult>
        get() = reposRelay
        .doOnSubscribe {
            if (!reposRelay.hasValue() || reposRelay.value is ReposResult.Failure) {
                reloadRepos()
            }
        }

    override val bookmarkChanges: Observable<BookmarkRepo> = bookmarkChangeRelay

    override fun addRepoToBookmarks(repoId: Int): Completable =
        reposService
            .addRepoToBookmarks(repoId)
            .doOnComplete {
                getRepo(repoId)?.let {
                    it.isBookmarked = true
                    bookmarkChangeRelay.accept(it)
                }
            }

    override fun removeRepoFromBookmarks(repoId: Int): Completable =
        reposService
            .removeRepoFromBookmarks(repoId)
            .doOnComplete {
                getRepo(repoId)?.let {
                    it.isBookmarked = false
                    bookmarkChangeRelay.accept(it)
                }
            }

    override fun getRepo(repoId: Int): BookmarkRepo? = reposRelay.value?.let {
        if (it is ReposResult.Success) {
            it.reposMap[repoId]
        } else {
            null
        }
    }

    override fun reloadRepos() {
        disposable?.dispose()
        disposable = reposService.loadRepos(organisation)
            .map { reposList ->
                reposList.associateBy({ it.repo.id }, { it })
            }
            .subscribe({
                reposRelay.accept(ReposResult.Success(it))
            }, {
                reposRelay.accept(ReposResult.Failure(it))
                Log.e(LOG_TAG, "error subscribing to loadRepos", it)
            })
    }

    companion object {
        private const val LOG_TAG = "ReposRepository"
    }
}