package mohammadhendy.githubrepos.repos_list.view_model

import io.reactivex.Observable
import mohammadhendy.githubrepos.service.IReposService

class RepoListViewModel(
    private val supportsTwoPane: Boolean,
    private val reposService: IReposService
) : IRepoListViewModel {

    override val state: Observable<RepoListState>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val nextRoute: Observable<RepoRoute>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun onRepoItemClicked(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}