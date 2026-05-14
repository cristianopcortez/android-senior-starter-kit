package br.com.ccortez.seniorstarterkitapplication.data.api

import br.com.ccortez.seniorstarterkitapplication.data.dto.HnSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * [Algolia HN Search API](https://hn.algolia.com/api)
 */
interface HackerNewsAlgoliaApi {

    @GET("search")
    suspend fun search(
        @Query("tags") tags: String,
        @Query("hitsPerPage") hitsPerPage: Int,
    ): HnSearchResponseDto
}
