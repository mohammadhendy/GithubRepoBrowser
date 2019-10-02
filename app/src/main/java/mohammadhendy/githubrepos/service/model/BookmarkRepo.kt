package mohammadhendy.githubrepos.service.model

import mohammadhendy.githubrepos.api.model.Repo

data class BookmarkRepo(
    val repo: Repo,
    var isBookmarked: Boolean
)