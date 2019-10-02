package mohammadhendy.githubrepos.dependency_injection.components

import dagger.Subcomponent
import mohammadhendy.githubrepos.dependency_injection.modules.RepoListModule
import mohammadhendy.githubrepos.dependency_injection.scopes.ActivityScope
import mohammadhendy.githubrepos.repos_list.view.RepoListActivity

@ActivityScope
@Subcomponent(modules = [RepoListModule::class])
interface RepoListComponent {
    fun inject(target: RepoListActivity)
}