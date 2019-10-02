package mohammadhendy.githubrepos.api

import io.reactivex.Single
import mohammadhendy.githubrepos.api.model.Repo
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IGithubApi {
    @Headers("Content-Type: application/json")
    @GET("orgs/{organisationName}/repos")
    fun getRepos(
        @Path("organisationName") organisationName: String
    ): Single<List<Repo>>
}