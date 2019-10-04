package mohammadhendy.githubrepos.dependency_injection.modules

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import mohammadhendy.githubrepos.R
import mohammadhendy.githubrepos.api.IGithubApi
import mohammadhendy.githubrepos.database.BookmarksDatabase
import mohammadhendy.githubrepos.database.IBookmarkDao
import mohammadhendy.githubrepos.repository.IReposRepository
import mohammadhendy.githubrepos.repository.ReposRepository
import mohammadhendy.githubrepos.service.IReposService
import mohammadhendy.githubrepos.service.ReposService
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun providesCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun providesRepoService(githubApi: IGithubApi, bookmarkDao: IBookmarkDao) : IReposService =
        ReposService(githubApi, bookmarkDao)

    @Provides
    @Named(ORGANISATION_KEY)
    fun providesOrganisationName(resources: Resources) : String = resources.getString(R.string.organisation)

    @Singleton
    @Provides
    fun providesReposRepository(
        @Named(ORGANISATION_KEY) organisation: String,
        reposService: IReposService
    ) : IReposRepository = ReposRepository(organisation, reposService)

    @Singleton
    @Provides
    fun providesBookmarkDatabase(context: Context) : BookmarksDatabase = Room.databaseBuilder(
            context,
            BookmarksDatabase::class.java, "bookmark-database"
        ).build()

    @Provides
    fun providesBookmarkDao(bookmarksDatabase: BookmarksDatabase) : IBookmarkDao =
        bookmarksDatabase.bookmarkDao()

    companion object {
        const val ORGANISATION_KEY = "ORGANISATION_KEY"
    }
}