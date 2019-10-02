package mohammadhendy.githubrepos.dependency_injection.modules

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.ReposService

@Module
class AppModule {

    @Provides
    fun providesCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun providesRepoService(githubApi: IGithubApi) : IReposService = ReposService(githubApi)
}