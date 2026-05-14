package br.com.ccortez.seniorstarterkitapplication.di

import br.com.ccortez.seniorstarterkitapplication.BuildConfig
import br.com.ccortez.seniorstarterkitapplication.data.api.HackerNewsAlgoliaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val RETROFIT_APP = "retrofit_app"

const val RETROFIT_HN = "retrofit_hn"

/**
 * Hilt module that provides Retrofit and OkHttp dependencies.
 *
 * `@Named([RETROFIT_APP])`: base URL de [BuildConfig.API_BASE_URL] (`local.properties` → API_BASE_URL).
 *
 * `@Named([RETROFIT_HN])`: [Algolia HN API](https://hn.algolia.com/api) (HTTPS, sem chave).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONNECT_TIMEOUT_SEC = 30L

    private const val READ_TIMEOUT_SEC = 30L

    private const val WRITE_TIMEOUT_SEC = 30L

    /** Base Algolia endpoint; deve terminar com `/` para o Retrofit resolver paths relativos. */
    private const val HN_ALGOLIA_BASE_URL = "https://hn.algolia.com/api/v1/"

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    @Named(RETROFIT_APP)
    fun provideAppRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named(RETROFIT_HN)
    fun provideHackerNewsRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(HN_ALGOLIA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideHackerNewsAlgoliaApi(
        @Named(RETROFIT_HN) retrofit: Retrofit,
    ): HackerNewsAlgoliaApi = retrofit.create(HackerNewsAlgoliaApi::class.java)
}
