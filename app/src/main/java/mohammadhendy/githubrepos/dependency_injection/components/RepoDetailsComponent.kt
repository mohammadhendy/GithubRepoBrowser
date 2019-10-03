package mohammadhendy.githubrepos.dependency_injection.components

import dagger.Subcomponent
import mohammadhendy.githubrepos.dependency_injection.modules.RepoDetailsModule
import mohammadhendy.githubrepos.dependency_injection.scopes.FragmentScope
import mohammadhendy.githubrepos.repo_details.view.RepoDetailFragment

@FragmentScope
@Subcomponent(modules = [RepoDetailsModule::class])
interface RepoDetailsComponent {
    fun inject(target: RepoDetailFragment)
}