package br.com.ccortez.seniorstarterkitapplication.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * GraphQL client for the public [Countries API](https://countries.trevorblades.com/).
 */
@Module
@InstallIn(SingletonComponent::class)
object GraphqlModule {

    private const val COUNTRIES_GRAPHQL_URL = "https://countries.trevorblades.com/graphql"

    @Provides
    @Singleton
    fun provideCountriesApolloClient(okHttpClient: OkHttpClient): ApolloClient =
        ApolloClient.Builder()
            .serverUrl(COUNTRIES_GRAPHQL_URL)
            .okHttpClient(okHttpClient)
            .build()
}
