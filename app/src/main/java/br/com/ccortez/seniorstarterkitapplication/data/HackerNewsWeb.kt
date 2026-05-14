package br.com.ccortez.seniorstarterkitapplication.data

internal object HackerNewsWeb {
    const val SITE_BASE_URL = "https://news.ycombinator.com"

    fun itemUrl(id: String): String = "$SITE_BASE_URL/item?id=$id"
}
