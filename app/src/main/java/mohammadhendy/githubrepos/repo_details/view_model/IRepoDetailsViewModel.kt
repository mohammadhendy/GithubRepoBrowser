package mohammadhendy.githubrepos.repo_details.view_model

import io.reactivex.Observable
import mohammadhendy.githubrepos.service.model.BookmarkRepo

interface IRepoDetailsViewModel {

    /**
     * Return the current bookmark repo
     */
    val bookmarkRepo: BookmarkRepo?

    /**
     * Emits the state of the add/remove bookmark action
     */
    val bookmarkState: Observable<BookmarkState>

    /**
     * called to add/remove the current repo to bookmarks
     */
    fun onAddRemoveBookmarkClicked()
}