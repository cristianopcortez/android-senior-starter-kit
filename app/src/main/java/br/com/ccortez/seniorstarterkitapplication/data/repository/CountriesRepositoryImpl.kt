package br.com.ccortez.seniorstarterkitapplication.data.repository

import br.com.ccortez.seniorstarterkitapplication.data.ErrorMessages
import br.com.ccortez.seniorstarterkitapplication.data.mapper.toDomain
import br.com.ccortez.seniorstarterkitapplication.domain.model.Country
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.repository.CountriesRepository
import br.com.ccortez.seniorstarterkitapplication.graphql.GetCountriesQuery
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : CountriesRepository {

    override suspend fun getCountries(): Resource<List<Country>> = try {
        val response = apolloClient.query(GetCountriesQuery()).execute()
        if (response.hasErrors()) {
            Resource.Error(
                response.errors?.firstOrNull()?.message ?: ErrorMessages.UNKNOWN_ERROR,
            )
        } else {
            val countries = response.data
                ?.countries
                ?.map { it.toDomain() }
                ?.sortedBy { it.name }
                .orEmpty()
            Resource.Success(countries)
        }
    } catch (e: ApolloException) {
        Resource.Error(
            e.message ?: ErrorMessages.UNKNOWN_ERROR,
            e,
        )
    } catch (t: Throwable) {
        Resource.Error(
            t.message ?: t::class.simpleName ?: ErrorMessages.UNKNOWN_ERROR,
            t,
        )
    }
}
