package br.com.ccortez.seniorstarterkitapplication.data.repository

import br.com.ccortez.seniorstarterkitapplication.data.ErrorMessages
import br.com.ccortez.seniorstarterkitapplication.data.api.HackerNewsAlgoliaApi
import br.com.ccortez.seniorstarterkitapplication.data.mapper.toDomain
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.model.Story
import br.com.ccortez.seniorstarterkitapplication.domain.repository.HackerNewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HackerNewsRepositoryImpl @Inject constructor(
    private val api: HackerNewsAlgoliaApi,
) : HackerNewsRepository {

    override suspend fun getFrontPageStories(hitsPerPage: Int): Resource<List<Story>> = try {
        val dto = api.search(tags = TAG_FRONT_PAGE, hitsPerPage = hitsPerPage)
        val stories = dto.hits.mapNotNull { it.toDomain() }
        Resource.Success(stories)
    } catch (t: Throwable) {
        Resource.Error(
            t.message ?: t::class.simpleName ?: ErrorMessages.UNKNOWN_ERROR,
            t,
        )
    }

    companion object {
        private const val TAG_FRONT_PAGE = "front_page"
    }
}
