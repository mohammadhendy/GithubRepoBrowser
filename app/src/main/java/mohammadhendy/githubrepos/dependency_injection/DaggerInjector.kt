package mohammadhendy.githubrepos.dependency_injection

import android.app.Application
import mohammadhendy.githubrepos.dependency_injection.components.AppComponent
import mohammadhendy.githubrepos.dependency_injection.components.DaggerAppComponent
import mohammadhendy.githubrepos.dependency_injection.components.RepoDetailsComponent
import mohammadhendy.githubrepos.dependency_injection.components.RepoListComponent
import mohammadhendy.githubrepos.dependency_injection.modules.AndroidModule
import mohammadhendy.githubrepos.dependency_injection.modules.RepoDetailsModule
import mohammadhendy.githubrepos.dependency_injection.modules.RepoListModule
import mohammadhendy.githubrepos.dependency_injection.modules.RestApiModule
import javax.inject.Singleton

@Singleton
class DaggerInjector private constructor() {

    private object Holder {
        val INSTANCE = DaggerInjector()
    }

    companion object {
        private val instance: DaggerInjector by lazy { Holder.INSTANCE }
        @JvmStatic
        fun get(): DaggerInjector = instance
    }

    lateinit var appComponent: AppComponent
    private var repoListComponent: RepoListComponent? = null
    private var repoDetailsComponent: RepoDetailsComponent? = null

    fun createDependencyInjectionRoot(application: Application): AppComponent {
        appComponent = DaggerAppComponent.builder()
            .androidModule(AndroidModule(application))
            .restApiModule(RestApiModule())
            .build()
        return appComponent
    }

    fun createRepoListComponent(supportsTwoPane: Boolean) = appComponent
        .repoListComponent(RepoListModule(supportsTwoPane))
        .apply { repoListComponent = this }

    fun releaseRepoListComponent() {
        repoListComponent = null
    }

    fun createRepoDetailsComponent(repoId: Int) = appComponent
        .repoDetailsComponent(RepoDetailsModule(repoId))
        .apply { repoDetailsComponent = this }

    fun releaseRepoDetailsComponent() {
        repoDetailsComponent = null
    }
}