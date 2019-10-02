package mohammadhendy.githubrepos.dependency_injection.modules

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import mohammadhendy.githubrepos.BuildConfig
import mohammadhendy.githubrepos.R
import mohammadhendy.githubrepos.api.IGithubApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class RestApiModule {

    @Provides
    @Named(BASE_URL_KEY)
    fun providesBaseUrl(resources: Resources) : String = resources.getString(R.string.base_url)

    @Provides
    fun providesOkHttpClient(interceptor: HttpLoggingInterceptor) : OkHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    @Provides
    fun providesRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    fun providesGson(): Gson = GsonBuilder().create()

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )

    @Provides
    fun providesGithubApi(
        @Named(BASE_URL_KEY) baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ) : IGithubApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(rxJava2CallAdapterFactory)
        .build()
        .create(IGithubApi::class.java)

    companion object {
        private const val CONNECTION_TIMEOUT_IN_SECONDS = 30L
        private const val READ_TIMEOUT_IN_SECONDS = 5L
        private const val BASE_URL_KEY = "BASE_URL_KEY"
    }
}