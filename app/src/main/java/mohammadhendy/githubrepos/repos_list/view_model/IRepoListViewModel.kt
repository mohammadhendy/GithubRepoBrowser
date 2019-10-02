package mohammadhendy.githubrepos.repos_list.view_model

import io.reactivex.Observable

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
     * called when a Repo item is clicked to open the detail
     * @param index of the clicked item in the list
     */
    fun onRepoItemClicked(index: Int)
}