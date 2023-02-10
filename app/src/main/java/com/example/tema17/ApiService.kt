package com.example.tema17

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apikey: String = ApiRest.apiKey,
        @Query("language") language: String = ApiRest.language): Call<GenresResponse>
    @GET("discover/movie")
    fun getMovies(
        @Query("api_key") apikey: String = ApiRest.apiKey,
        @Query("language") language: String = ApiRest.language,
        @Query("with_genres") genreMovies: String): Call<MoviesResponse>
    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") apikey: String = ApiRest.apiKey,
        @Query("language") language: String = ApiRest.language): Call<MovieDetailResponse>
}