package br.com.ccortez.seniorstarterkitapplication.domain.model

data class Country(
    val code: String,
    val name: String,
    val capital: String?,
    val emoji: String,
    val continent: String,
)
