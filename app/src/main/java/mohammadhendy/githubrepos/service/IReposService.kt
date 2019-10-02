package mohammadhendy.githubrepos.service

import io.reactivex.Completable
import io.reactivex.Observable
import mohammadhendy.githubrepos.service.model.BookmarkRepo

interface IReposService {
    /**
     * loads the Repos from Github for a certain organisation
     * @param organisation name of organisation to load its repos
     * @return an [Observable] of [BookmarkRepo]'s [List]
     */
    fun loadRepos(organisation: String) : Observable<List<BookmarkRepo>>

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

}