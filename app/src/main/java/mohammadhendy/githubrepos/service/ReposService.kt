package mohammadhendy.githubrepos.service

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.api.model.Repo
import mohammadhendy.githubrepos.service.model.BookmarkRepo

class ReposService(private val githubApi: IGithubApi) : IReposService {

    override fun loadRepos(organisation: String): Observable<List<BookmarkRepo>> = githubApi
        .getRepos(organisation)
        .map {
            mapToBookmarkRepo(it)
        }
        .toObservable()
        .subscribeOn(Schedulers.io())

    override fun addRepoToBookmarks(repoId: Int): Completable {
        return Completable.complete()
    }

    override fun removeRepoFromBookmarks(repoId: Int): Completable {
        return Completable.complete()
    }

    private fun mapToBookmarkRepo(repos: List<Repo>) = repos.map {
        BookmarkRepo(it, false)
    }
}