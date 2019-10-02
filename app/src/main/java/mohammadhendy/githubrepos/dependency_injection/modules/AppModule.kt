package mohammadhendy.githubrepos.dependency_injection.modules

import dagger.Module
import dagger.Provides
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.ReposService

@Module
class AppModule {

    @Provides
    fun providesRepoService(githubApi: IGithubApi) : IReposService = ReposService(githubApi)
}