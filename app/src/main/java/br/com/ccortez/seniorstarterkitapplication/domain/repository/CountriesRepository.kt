package br.com.ccortez.seniorstarterkitapplication.domain.repository

import br.com.ccortez.seniorstarterkitapplication.domain.model.Country
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource

interface CountriesRepository : Repository {
    suspend fun getCountries(): Resource<List<Country>>
}
