package mohammadhendy.githubrepos.dependency_injection.modules

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mohammadhendy.githubrepos.R
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.repository.ReposRepository
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.ReposService
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun providesCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun providesRepoService(githubApi: IGithubApi) : IReposService = ReposService(githubApi)

    @Provides
    @Named(ORGANISATION_KEY)
    fun providesOrganisationName(resources: Resources) : String = resources.getString(R.string.organisation)

    @Singleton
    @Provides
    fun providesReposRepository(
        @Named(ORGANISATION_KEY) organisation: String,
        reposService: IReposService
    ) : IReposRepository = ReposRepository(organisation, reposService)

    companion object {
        const val ORGANISATION_KEY = "ORGANISATION_KEY"
    }
}