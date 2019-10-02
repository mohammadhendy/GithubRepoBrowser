package mohammadhendy.githubrepos.repos_list.view_model

import mohammadhendy.githubrepos.service.model.BookmarkRepo

sealed class RepoListState {
    object Loading : RepoListState()
    data class Data(val repoList: List<BookmarkRepo>) : RepoListState()
    data class Empty(var error: Boolean = false) : RepoListState()
}
