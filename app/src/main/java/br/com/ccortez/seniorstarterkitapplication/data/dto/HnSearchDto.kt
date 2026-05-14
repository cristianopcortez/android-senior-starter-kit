package br.com.ccortez.seniorstarterkitapplication.data.dto

import com.google.gson.annotations.SerializedName

data class HnSearchResponseDto(
    val hits: List<HnHitDto> = emptyList(),
)

data class HnHitDto(
    @SerializedName("objectID")
    val objectId: String,
    val title: String?,
    val url: String?,
    val points: Int?,
    @SerializedName("num_comments")
    val numComments: Int?,
    val author: String?,
)
