package mohammadhendy.githubrepos.dependency_injection.modules

import dagger.Module
import dagger.Provides
import mohammadhendy.githubrepos.repos_list.view_model.IRepoListViewModel
import mohammadhendy.githubrepos.repos_list.view_model.RepoListViewModel
import mohammadhendy.githubrepos.service.IReposService

@Module
class RepoListModule(private val supportsTwoPane: Boolean) {

    @Provides
    fun providesRepoListViewModel(reposService: IReposService) : IRepoListViewModel =
        RepoListViewModel(supportsTwoPane, reposService)
}