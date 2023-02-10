package com.example.tema17

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesFragment : Fragment() {


    private var swiperefresh: SwipeRefreshLayout? = null
    val TAG = "MainActivity"
    var data: ArrayList<MoviesResponse.Result> = ArrayList()

    private var adapter: MoviesAdapter? = null


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
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val movies =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable("genre", GenresResponse.Genre::class.java)


            } else {
                arguments?.getSerializable("genre") as? GenresResponse.Genre


            }



        ApiRest.initService()


        val mLayoutManager = GridLayoutManager(context, 2)
        val rvMovies = view.findViewById<RecyclerView>(R.id.rvMovies)

        rvMovies.layoutManager = mLayoutManager

        swiperefresh = view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)

        adapter = MoviesAdapter(data){ detail ->
            activity?.let {
                val fragment = MovieDetailFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putSerializable("id", detail)

                it.supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container, fragment)
                    .commit()

            }
        }

        rvMovies.adapter = adapter

        getMovies(movies)

        swiperefresh?.setOnRefreshListener{
            getMovies(movies)
        }
    }
    private fun getMovies(movies: GenresResponse.Genre?) {
        val call = ApiRest.service.getMovies(genreMovies = movies?.id.toString())
        call.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.results)
                    adapter?.notifyDataSetChanged()
// Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }

                swiperefresh?.isRefreshing = false
            }
             override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())

                swiperefresh?.isRefreshing = false
            }
        })
    }
}