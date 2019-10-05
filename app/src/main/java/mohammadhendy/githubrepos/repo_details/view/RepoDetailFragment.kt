package mohammadhendy.githubrepos.repo_details.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import mohammadhendy.githubrepos.R
import mohammadhendy.githubrepos.dependency_injection.DaggerInjector
import mohammadhendy.githubrepos.repo_details.view_model.BookmarkState
import mohammadhendy.githubrepos.repo_details.view_model.IRepoDetailsViewModel
import javax.inject.Inject

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [RepoListActivity]
 * in two-pane mode (on tablets) or a [RepoDetailActivity]
 * on handsets.
 */
class RepoDetailFragment : Fragment() {

    @Inject
    lateinit var repoDetailsViewModel: IRepoDetailsViewModel
    @Inject
    lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                DaggerInjector.get().createRepoDetailsComponent(it.getInt(ARG_ITEM_ID)).inject(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_repo_detail, container, false)

    override fun onStart() {
        super.onStart()
        bindViewModel()
    }

    override fun onDestroy() {
        disposables.clear()
        DaggerInjector.get().releaseRepoDetailsComponent()
        super.onDestroy()
    }

    private fun bindViewModel() {
        repoDetailsViewModel.bookmarkRepo?.run {
            repo_name_text_view.text = repo.name
            repo_description_text_view.text = repo.description
            stars_count_text_view.text = resources.getQuantityString(
                R.plurals.stars_count,
                repo.starsCount,
                repo.starsCount
            )
        }

        repo_bookmark_button.setOnClickListener {
            repoDetailsViewModel.onAddRemoveBookmarkClicked()
        }

        disposables.add(repoDetailsViewModel.bookmarkState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleBookmarkState(it)
            }, {
                Log.e(LOG_TAG, "Error subscribing to bookmarkState", it)
            })
        )
    }

    private fun handleBookmarkState(bookmarkState: BookmarkState) {
        when(bookmarkState) {
            BookmarkState.Added -> {
                repo_bookmark_button.text = getString(R.string.remove_bookmark)
                repo_bookmark_button.isSelected = true
                repo_bookmark_button.isEnabled = true
                loading_progress.visibility = View.GONE
                repo_bookmark_image_view.visibility = View.VISIBLE
            }
            BookmarkState.Removed -> {
                repo_bookmark_button.text = getString(R.string.add_bookmark)
                repo_bookmark_button.isSelected = false
                repo_bookmark_button.isEnabled = true
                loading_progress.visibility = View.GONE
                repo_bookmark_image_view.visibility = View.INVISIBLE
            }
            is BookmarkState.Loading -> {
                repo_bookmark_button.isEnabled = false
                loading_progress.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
        private const val LOG_TAG = "RepoDetailFragment"
    }
}
