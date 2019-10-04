package mohammadhendy.githubrepos.repository

import io.reactivex.Completable
import io.reactivex.Observable
import mohammadhendy.githubrepos.service.model.BookmarkRepo

interface IReposRepository {

    /**
     * subscribe to this to get list of repos or error
     * @return [Observable] of [ReposResult]
     */
    val repos: Observable<ReposResult>

    /**
     * Subscribe to this to get notified with changes in Repos one by one
     * Mainly report the add/remove from bookmark changes
     * @return [Observable] of changed repo [BookmarkRepo]
     */
    val bookmarkChanges: Observable<BookmarkRepo>

    /**
     * adds a certain repo to bookmarks by id
     * @param repoId id of the repo
     * @return [Completable] of the add operation status
     */
    fun addRepoToBookmarks(repoId: Int) : Completable

    /**
     * remove a certain repo from bookmarks by id
     * @param repoId id of the repo
     * @return [Completable] of the remove operation status
     */
    fun removeRepoFromBookmarks(repoId: Int) : Completable

    /**
     * get a repo by its id
     * @param repoId id of the repo
     * @return the required [BookmarkRepo] or null
     */
    fun getRepo(repoId: Int) : BookmarkRepo?

    /**
     * Force fetch updated repos from sources. i.e update the cached copy
     */
    fun reloadRepos()
}