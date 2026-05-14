package br.com.ccortez.seniorstarterkitapplication.data

import br.com.ccortez.seniorstarterkitapplication.BuildConfig

internal object HackerNewsWeb {
    val siteBaseUrl: String get() = BuildConfig.HN_SITE_BASE_URL

    fun itemUrl(id: String): String = "${siteBaseUrl.trimEnd('/')}/item?id=$id"
}
