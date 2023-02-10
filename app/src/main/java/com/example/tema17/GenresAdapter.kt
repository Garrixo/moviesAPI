package com.example.tema17

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class GenresAdapter (private val data: ArrayList<GenresResponse.Genre>, val onClick:(GenresResponse.Genre) ->Unit) : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_item, parent, false)
        Log.i("Test", itemCount.toString())
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun getItemCount(): Int = data.size



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: GenresResponse.Genre) {

            itemView.findViewById<TextView>(R.id.genero).text = item.name
            itemView.findViewById<CardView>(R.id.card).setBackgroundColor(generateColor().toInt())

            itemView.setOnClickListener {
                Log.v("Pulso sobre", item.id.toString())
                onClick(item)
            }
        }
        fun generateColor(): Long {
            val colors = arrayListOf(0xfff44336,0xffe91e63,0xff9c27b0,0xff673ab7,
                0xff3f51b5,0xff2196f3,0xff03a9f4,0xff00bcd4,
                0xff009688,0xff4caf50,0xff8bc34a,0xffcddc39,
                0xffffeb3b,0xffffc107,0xffff9800,0xffff5722,
                0xff795548,0xff9e9e9e,0xff607d8b,0xff333333)
            return colors.random()
        }
    }
}