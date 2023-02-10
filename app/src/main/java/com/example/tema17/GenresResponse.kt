package com.example.tema17

import java.io.Serializable


data class GenresResponse(
    val genres: List<Genre>
):Serializable {
    data class Genre(
        val id: Int,
        val name: String
    ): Serializable
}