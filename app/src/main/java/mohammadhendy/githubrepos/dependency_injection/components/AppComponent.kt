package mohammadhendy.githubrepos.dependency_injection.components

import dagger.Component
import mohammadhendy.githubrepos.App
import mohammadhendy.githubrepos.dependency_injection.modules.AndroidModule
import mohammadhendy.githubrepos.dependency_injection.modules.AppModule
import mohammadhendy.githubrepos.dependency_injection.modules.RepoListModule
import mohammadhendy.githubrepos.dependency_injection.modules.RestApiModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidModule::class, RestApiModule::class])
interface AppComponent {
    fun inject(target: App)

    fun repoListComponent(module: RepoListModule): RepoListComponent
}