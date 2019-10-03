package mohammadhendy.githubrepos.repos_list.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.activity_repo_list.*
import kotlinx.android.synthetic.main.empty_loading_recyclerview.*
import kotlinx.android.synthetic.main.item_repo.view.*
import kotlinx.android.synthetic.main.repo_list.*
import mohammadhendy.githubrepos.R
import mohammadhendy.githubrepos.RepoDetailActivity
import mohammadhendy.githubrepos.RepoDetailFragment
import mohammadhendy.githubrepos.dependency_injection.DaggerInjector
import mohammadhendy.githubrepos.repos_list.view_model.IRepoListViewModel
import mohammadhendy.githubrepos.repos_list.view_model.RepoListState
import mohammadhendy.githubrepos.repos_list.view_model.RepoRoute
import mohammadhendy.githubrepos.service.model.BookmarkRepo
import javax.inject.Inject

/**
 * An activity representing a list of Repos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of Repos, which when touched,
 * lead to a [RepoDetailActivity] representing
 * Repo details. On tablets, the activity presents the list of Repos and
 * Repo details side-by-side using two vertical panes.
 */
class RepoListActivity : AppCompatActivity() {

    @Inject
    lateinit var disposables: CompositeDisposable
    @Inject
    lateinit var viewModel: IRepoListViewModel

    private lateinit var repoListAdapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        var twoPane = false
        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        DaggerInjector.get().createRepoListComponent(twoPane).inject(this)

        setupRecyclerView(repo_list)
    }

    override fun onStart() {
        super.onStart()
        bindViewModel()
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    override fun onDestroy() {
        DaggerInjector.get().releaseRepoListComponent()
        super.onDestroy()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val onClickListener = View.OnClickListener {
            val repo = it.tag as BookmarkRepo
            viewModel.onRepoItemClicked(repo.repo.id)
        }
        recyclerView.adapter =
            RepoListAdapter(onClickListener).apply { repoListAdapter = this }
    }

    private fun bindViewModel() {
        disposables.add(viewModel.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleRepoState(it)
            }, {
                Log.e(LOG_TAG, "Error subscribing to repoListState", it)
            })
        )

        disposables.add(viewModel.nextRoute
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleNextRoute(it)
            }, {
                Log.e(LOG_TAG, "Error subscribing to nextRoute", it)
            })
        )
    }

    private fun handleRepoState(repoListState: RepoListState) {
        when(repoListState) {
            is RepoListState.Data -> {
                repoListAdapter.populateList(repoListState.repoList)
                applyVisibility()
            }
            RepoListState.Loading -> {
                applyVisibility(showRecycler = false, showEmpty = false, showProgress = true)
            }
            is RepoListState.Empty -> {
                empty_text_view.text = getString(if (repoListState.error) R.string.empty_text_error else R.string.empty_text_no_data)
                applyVisibility(showRecycler = false, showEmpty = true, showProgress = false)
            }
        }
    }

    private fun applyVisibility(
        showRecycler: Boolean = true,
        showProgress: Boolean = false,
        showEmpty: Boolean = false
    ) {
        repo_list.visibility = if (showRecycler) View.VISIBLE else View.GONE
        empty_text_view.visibility = if (showEmpty) View.VISIBLE else View.GONE
        loading_progress.visibility = if (showProgress) View.VISIBLE else View.GONE
    }

    private fun handleNextRoute(repoRoute: RepoRoute) {
        when(repoRoute) {
            is RepoRoute.OpenDetails -> {
                val intent = Intent(this, RepoDetailActivity::class.java).apply {
                    putExtra(RepoDetailFragment.ARG_ITEM_ID, repoRoute.repoId)
                }
                startActivity(intent)
            }
            is RepoRoute.RefreshDetails -> {
                val fragment = RepoDetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt(RepoDetailFragment.ARG_ITEM_ID, repoRoute.repoId)
                    }
                }
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            }
        }
    }

    class RepoListAdapter(private val onClickListener: View.OnClickListener) :
        RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

        private val repoList = mutableListOf<BookmarkRepo>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val repo = repoList[position]
            holder.bindViewHolder(repo)

            with(holder.itemView) {
                tag = repo
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = repoList.size

        fun populateList(newList: List<BookmarkRepo>) {
            repoList.clear()
            repoList.addAll(newList)
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindViewHolder(repo: BookmarkRepo) {
                itemView.repo_name_text_view.text = repo.repo.name
                itemView.stars_count_text_view.text = itemView.resources.getQuantityString(
                    R.plurals.stars_count,
                    repo.repo.starsCount,
                    repo.repo.starsCount
                )
                itemView.repo_bookmark_image_view.isSelected = repo.isBookmarked
            }
        }
    }

    companion object {
        private const val LOG_TAG = "RepoListActivity"
    }
}
