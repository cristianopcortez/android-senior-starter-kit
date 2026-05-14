package br.com.ccortez.seniorstarterkitapplication.domain.usecase

import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.model.Story
import br.com.ccortez.seniorstarterkitapplication.domain.repository.HackerNewsRepository
import javax.inject.Inject

data class FrontPageStoriesParams(val hitsPerPage: Int = DEFAULT_HITS_PER_PAGE) {
    companion object {
        const val DEFAULT_HITS_PER_PAGE = 40
    }
}

class GetFrontPageStoriesUseCase @Inject constructor(
    private val repository: HackerNewsRepository,
) : UseCase<FrontPageStoriesParams, List<Story>> {

    override suspend fun invoke(params: FrontPageStoriesParams): Resource<List<Story>> =
        repository.getFrontPageStories(params.hitsPerPage)
}
