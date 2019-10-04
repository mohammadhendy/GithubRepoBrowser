package mohammadhendy.githubrepos.service

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.api.model.Repo
import mohammadhendy.githubrepos.database.IBookmarkDao
import mohammadhendy.githubrepos.database.model.Bookmark
import mohammadhendy.githubrepos.service.model.BookmarkRepo

class ReposService(
    private val githubApi: IGithubApi,
    private val bookmarkDao: IBookmarkDao
) : IReposService {

    override fun loadRepos(organisation: String): Observable<List<BookmarkRepo>> = githubApi
        .getRepos(organisation)
        .flatMap { repos->
            Single.just(repos).zipWith(bookmarkDao.isBookmarked(repos.map { it.id }.toIntArray()),
                BiFunction { reposList: List<Repo>, bookmarksList: List<Bookmark> ->
                    mapToBookmarkRepo(reposList, bookmarksList)
                }
            )
        }
        .toObservable()
        .subscribeOn(Schedulers.io())

    override fun addRepoToBookmarks(repoId: Int): Completable =
        bookmarkDao.insertBookmark(Bookmark(repoId, true))
            .subscribeOn(Schedulers.io())

    override fun removeRepoFromBookmarks(repoId: Int): Completable =
        bookmarkDao.insertBookmark(Bookmark(repoId, false))
            .subscribeOn(Schedulers.io())

    private fun mapToBookmarkRepo(repos: List<Repo>, bookmarksList: List<Bookmark>) =
        repos.map { repo->
            val bookmark = bookmarksList.find { bookmark-> repo.id == bookmark.repoId }
            bookmark?.let { BookmarkRepo(repo, it.isBookmarked) } ?: BookmarkRepo(repo, false)
        }
}