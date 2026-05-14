package br.com.ccortez.seniorstarterkitapplication.domain.model

/**
 * Hacker News story item (mapped from Algolia HN hits).
 *
 * @param url Canonical link: article URL when present, otherwise HN discussion page.
 */
data class Story(
    val id: String,
    val title: String,
    val url: String,
    val points: Int,
    val commentsCount: Int,
    val author: String,
)
