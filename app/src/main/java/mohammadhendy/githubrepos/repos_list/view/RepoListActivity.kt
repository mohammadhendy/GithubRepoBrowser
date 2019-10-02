package mohammadhendy.githubrepos.repos_list.view

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

import mohammadhendy.githubrepos.dummy.DummyContent
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
        recyclerView.adapter =
            RepoListAdapter().apply { repoListAdapter = this }
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

    class RepoListAdapter :
        RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

        private val repoList = mutableListOf<BookmarkRepo>()
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
//                val item = v.tag as DummyContent.DummyItem
//                if (twoPane) {
//                    val fragment = RepoDetailFragment().apply {
//                        arguments = Bundle().apply {
//                            putString(RepoDetailFragment.ARG_ITEM_ID, item.id)
//                        }
//                    }
//                    parentActivity.supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.item_detail_container, fragment)
//                        .commit()
//                } else {
//                    val intent = Intent(v.context, RepoDetailActivity::class.java).apply {
//                        putExtra(RepoDetailFragment.ARG_ITEM_ID, item.id)
//                    }
//                    v.context.startActivity(intent)
//                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = repoList[position]
            holder.idView.text = item.repo.name
            holder.contentView.text = item.repo.description

            with(holder.itemView) {
                tag = item
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
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }

    companion object {
        private const val LOG_TAG = "RepoListActivity"
    }
}
