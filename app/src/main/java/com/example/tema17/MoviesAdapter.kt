package com.example.tema17

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MoviesAdapter(private val data: ArrayList<MoviesResponse.Result>, val onClick:(MoviesResponse.Result) ->Unit) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int = data.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MoviesResponse.Result) {

            val ivMovie =  itemView.findViewById<ImageView>(R.id.ivMovie)
            itemView.findViewById<TextView>(R.id.txtTitulo).text = item.title
            val image = ApiRest.URL_IMAGES + item.posterPath
            Picasso.get().load(image).into(ivMovie)

            itemView.setOnClickListener {
                Log.v("Pulso sobre", item.id.toString())
                onClick(item)
            }

        }
    }
}