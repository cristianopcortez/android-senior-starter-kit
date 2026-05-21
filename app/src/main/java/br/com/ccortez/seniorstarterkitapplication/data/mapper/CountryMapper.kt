package br.com.ccortez.seniorstarterkitapplication.data.mapper

import br.com.ccortez.seniorstarterkitapplication.domain.model.Country
import br.com.ccortez.seniorstarterkitapplication.graphql.GetCountriesQuery

fun GetCountriesQuery.Country.toDomain(): Country =
    Country(
        code = code,
        name = name,
        capital = capital,
        emoji = emoji,
        continent = continent.name,
    )
