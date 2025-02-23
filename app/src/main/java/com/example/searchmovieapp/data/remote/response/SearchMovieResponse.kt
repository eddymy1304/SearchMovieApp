package com.example.searchmovieapp.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchMovieResponse(
    @SerialName("Response") var response: String? = null,
    @SerialName("Search") var search: List<Search>? = null,
    var totalResults: String? = null
) {

    @Serializable
    data class Search(
        @SerialName("Poster") var poster: String? = null,
        @SerialName("Title") var title: String? = null,
        @SerialName("Type") var type: String? = null,
        @SerialName("Year") var year: String? = null,
        var imdbID: String? = null
    )
}