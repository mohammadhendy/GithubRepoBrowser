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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeRepoFromBookmarks(repoId: Int): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun mapToBookmarkRepo(repos: List<Repo>) = repos.map {
        BookmarkRepo(it, false)
    }
}