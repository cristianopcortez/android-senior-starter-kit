package br.com.ccortez.seniorstarterkitapplication.data.mapper

import br.com.ccortez.seniorstarterkitapplication.data.HackerNewsWeb
import br.com.ccortez.seniorstarterkitapplication.data.dto.HnHitDto
import br.com.ccortez.seniorstarterkitapplication.domain.model.Story

fun HnHitDto.toDomain(): Story? {
    val trimmedTitle = title?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val id = objectId.trim().ifEmpty { return null }
    val resolvedUrl =
        url?.trim()?.takeIf { it.isNotEmpty() } ?: HackerNewsWeb.itemUrl(id)

    return Story(
        id = id,
        title = trimmedTitle,
        url = resolvedUrl,
        points = points ?: 0,
        commentsCount = numComments ?: 0,
        author = author?.trim()?.takeIf { it.isNotEmpty() } ?: "—",
    )
}
