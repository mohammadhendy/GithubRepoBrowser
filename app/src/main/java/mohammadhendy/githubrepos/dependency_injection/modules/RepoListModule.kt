package mohammadhendy.githubrepos.dependency_injection.modules

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import mohammadhendy.githubrepos.R
import mohammadhendy.githubrepos.repos_list.view_model.IRepoListViewModel
import mohammadhendy.githubrepos.repos_list.view_model.RepoListViewModel
import mohammadhendy.githubrepos.service.IReposService
import javax.inject.Named

@Module
class RepoListModule(private val supportsTwoPane: Boolean) {

    @Provides
    @Named(ORGANISATION_KEY)
    fun providesOrganisationName(resources: Resources) : String = resources.getString(R.string.organisation)

    @Provides
    fun providesRepoListViewModel(
        @Named(ORGANISATION_KEY) organisation: String,
        reposService: IReposService
    ) : IRepoListViewModel = RepoListViewModel(supportsTwoPane, organisation, reposService)

    companion object {
        private const val ORGANISATION_KEY = "ORGANISATION_KEY"
    }
}