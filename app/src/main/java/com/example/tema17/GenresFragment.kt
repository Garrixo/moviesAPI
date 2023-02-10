package com.example.tema17

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

class GenresFragment : Fragment() {


    private var swiperefresh: SwipeRefreshLayout? = null
    val TAG = "MainActivity"
    var data: ArrayList<GenresResponse.Genre> = ArrayList()

    private var adapter: GenresAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApiRest.initService()

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Inicio"
        (activity as? AppCompatActivity)?.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        val mLayoutManager = GridLayoutManager(context, 2)
        val rvGeneros = view.findViewById<RecyclerView>(R.id.rvGeneros)

        rvGeneros.layoutManager = mLayoutManager

        swiperefresh = view.findViewById(R.id.swiperefresh)

        adapter = GenresAdapter(data) { genre ->
            activity?.let {
                (activity as? AppCompatActivity)?.supportActionBar?.title = genre.name
                val fragment = MoviesFragment()
                fragment.arguments = Bundle()
                fragment.arguments?.putSerializable("genre", genre)

                it.supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container, fragment)
                    .commit()

            }
        }
            rvGeneros.adapter = adapter
            getGenres()
            swiperefresh?.setOnRefreshListener {
                getGenres()
            }

    }
    private fun getGenres() {
        val call = ApiRest.service.getGenres()
        call.enqueue(object : Callback<GenresResponse> {
            override fun onResponse(call: Call<GenresResponse>, response: Response<GenresResponse>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i(TAG, body.toString())
                    data.clear()
                    data.addAll(body.genres)
                    Log.i(TAG, data.toString())
                    adapter?.notifyDataSetChanged()
// Imprimir aqui el listado con logs
                } else {
                    Log.e(TAG, response.errorBody()?.string() ?: "Error")
                }

                swiperefresh?.isRefreshing = false
            }
            override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())

                swiperefresh?.isRefreshing = false
            }
        })
    }

}