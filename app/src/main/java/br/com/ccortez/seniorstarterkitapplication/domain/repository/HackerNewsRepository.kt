package br.com.ccortez.seniorstarterkitapplication.domain.repository

import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.model.Story

interface HackerNewsRepository : Repository {

    suspend fun getFrontPageStories(hitsPerPage: Int): Resource<List<Story>>
}
