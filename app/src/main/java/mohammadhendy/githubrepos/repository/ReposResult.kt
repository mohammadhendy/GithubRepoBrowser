package mohammadhendy.githubrepos.repository

import mohammadhendy.githubrepos.service.model.BookmarkRepo

sealed class ReposResult {
    data class Success(val reposMap: Map<Int, BookmarkRepo>) : ReposResult()
    data class Failure(val error: Throwable) : ReposResult()
}
