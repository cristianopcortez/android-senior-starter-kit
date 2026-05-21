package br.com.ccortez.seniorstarterkitapplication.domain.usecase

import br.com.ccortez.seniorstarterkitapplication.domain.model.Country
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.repository.CountriesRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: CountriesRepository,
) : UseCase<NoParams, List<Country>> {

    override suspend fun invoke(params: NoParams): Resource<List<Country>> =
        repository.getCountries()
}
