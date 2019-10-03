package mohammadhendy.githubrepos.repo_details.view_model

sealed class BookmarkState {
    data class Loading(val remove: Boolean) : BookmarkState()
    object Added : BookmarkState()
    object Removed : BookmarkState()
}
