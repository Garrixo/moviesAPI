package com.example.tema17

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tema17.R.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailFragment : Fragment() {


    val TAG = "MainActivity"
    var data: ArrayList<MovieDetailResponse.Genre> = ArrayList()

    private lateinit var chipGroup: ChipGroup




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable("id", MoviesResponse.Result::class.java)


            } else {
                arguments?.getSerializable("id") as? MoviesResponse.Result


            }

        ApiRest.initService()


        //val mLayoutManager = GridLayoutManager(context, 4)
        //val mLayoutManager = StaggeredGridLayoutManager(4, 1)
        //val mLayoutManager = WearableLinearLayoutManager(context)

       // val rvMovies = view.findViewById<RecyclerView>(R.id.rvGenresTag)

        //rvMovies.layoutManager = mLayoutManager



       // rvMovies.adapter = adapter

        getMovieDetailFunc(movie)




    }
    private fun getMovieDetailFunc(movies: MoviesResponse.Result?) {
        print(movies?.id)
        val call = ApiRest.service.getMovieDetail(movieId = movies?.id.toString())
        call.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(call: Call<MovieDetailResponse>, response: Response<MovieDetailResponse>) {
                val body = response.body()

                if (body != null) {
                    setText(body)
                }

                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.genres)

// Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }

            }
            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())

            }
        })
    }

    fun setText(datosPeli: MovieDetailResponse){

        chipGroup = requireView().findViewById(R.id.chipGroup)

        val imageC = ApiRest.URL_IMAGES + datosPeli.backdropPath
        val imageP = ApiRest.URL_IMAGES + datosPeli.posterPath
        var ivCabecera = view?.findViewById<ImageView>(R.id.ivCabecera)
        Picasso.get().load(imageC).into(ivCabecera)
        var ivPortada = view?.findViewById<ImageView>(R.id.ivPortada)
        Picasso.get().load(imageP).into(ivPortada)
        view?.findViewById<TextView>(R.id.tvAno)?.text = datosPeli.releaseDate.substring(0,4)
        view?.findViewById<TextView>(R.id.tvNota)?.text = datosPeli.voteAverage.toString().substring(0,3)
        view?.findViewById<ProgressBar>(R.id.notaProgress)?.progress = datosPeli.voteAverage.toInt()
        view?.findViewById<TextView>(R.id.tvTituloPeli)?.text = datosPeli.title
        view?.findViewById<TextView>(R.id.tvContenido)?.text = datosPeli.overview

        datosPeli.genres.forEach {
            addChip(it.name)
        }


    }

    private fun addChip(text: String){
        val chip = Chip(context)

        chip.text = text
        chip.setChipBackgroundColorResource(color.black)
        chip.setTextColor(getResources().getColor(color.lightGrey))
        chip.setChipStrokeWidthResource(dimen.chipStrokeWidth)
        chip.setChipStrokeColorResource(color.lightGrey)

        chipGroup.addView(chip)
    }




}


