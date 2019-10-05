package mohammadhendy.githubrepos.service

import mohammadhendy.githubrepos.api.model.Repo
import mohammadhendy.githubrepos.database.model.Bookmark
import mohammadhendy.githubrepos.service.model.BookmarkRepo

class MockUtils {

    fun mockRepos() : List<Repo> {
        return listOf(
            Repo(id = 1, name = "repo 1", description = "repo desc 1", starsCount = 5),
            Repo(id = 2, name = "repo 2", description = "repo desc 2", starsCount = 10),
            Repo(id = 3, name = "repo 3", description = "repo desc 3", starsCount = 100)
        )
    }

    fun mockBookmarkRepos() : List<BookmarkRepo> {
        val repoList = mockRepos()
        return listOf(
            BookmarkRepo(repo = repoList[0], isBookmarked = false),
            BookmarkRepo(repo = repoList[1], isBookmarked = true),
            BookmarkRepo(repo = repoList[2], isBookmarked = true)
        )
    }

    fun mockBookmarks() : List<Bookmark> {
        return listOf(
            Bookmark(1, false),
            Bookmark(2, true),
            Bookmark(3, true)
        )
    }

    fun mockBookmarkReposMap() : Map<Int, BookmarkRepo> = mockBookmarkRepos().associateBy { it.repo.id }
}