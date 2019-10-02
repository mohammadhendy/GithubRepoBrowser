package mohammadhendy.githubrepos.repos_list.view_model

sealed class RepoRoute {
    /**
     * Used to open the details activity when twoPane mode is not supported
     */
    data class OpenDetails(val repoId: Int) : RepoRoute()
    /**
     * Used to refresh the details fragment when twoPane mode is supported
     */
    data class RefreshDetails(val repoId: Int) : RepoRoute()
}
