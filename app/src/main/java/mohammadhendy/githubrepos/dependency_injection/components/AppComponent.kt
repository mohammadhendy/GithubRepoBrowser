package mohammadhendy.githubrepos.dependency_injection.components

import dagger.Component
import mohammadhendy.githubrepos.App
import mohammadhendy.githubrepos.dependency_injection.modules.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidModule::class, RestApiModule::class])
interface AppComponent {
    fun inject(target: App)

    fun repoListComponent(module: RepoListModule): RepoListComponent

    fun repoDetailsComponent(module: RepoDetailsModule): RepoDetailsComponent
}