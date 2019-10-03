package mohammadhendy.githubrepos.repos_list.view_model

import io.reactivex.Observable
import mohammadhendy.githubrepos.service.model.BookmarkRepo

interface IRepoListViewModel {

    /**
     * subscribe to the state of the screen, emits [Observable] of [RepoListState]
     */
    val state: Observable<RepoListState>

    /**
     * subscribe to route changes, emits [Observable] of [RepoRoute]
     */
    val nextRoute: Observable<RepoRoute>

    /**
     * subscribe to repo changes, emits [Observable] of repo id [BookmarkRepo]
     */
    val repoChanged: Observable<BookmarkRepo>

    /**
     * called when a Repo item is clicked to open/refresh the detail
     * @param repoId of the clicked item in the list
     */
    fun onRepoItemClicked(repoId: Int)
}