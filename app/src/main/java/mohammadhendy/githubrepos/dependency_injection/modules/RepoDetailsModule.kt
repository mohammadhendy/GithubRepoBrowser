package mohammadhendy.githubrepos.dependency_injection.modules

import dagger.Module
import dagger.Provides
import mohammadhendy.githubrepos.repo_details.view_model.IRepoDetailsViewModel
import mohammadhendy.githubrepos.repo_details.view_model.RepoDetailsViewModel
import mohammadhendy.githubrepos.repository.IReposRepository

@Module
class RepoDetailsModule(private val repoId: Int) {

    @Provides
    fun providesRepoDetailsViewModel(
        reposRepository: IReposRepository
    ) : IRepoDetailsViewModel = RepoDetailsViewModel(repoId, reposRepository)
}