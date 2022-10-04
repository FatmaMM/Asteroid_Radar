package com.example.asteroidrader.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidrader.databinding.RowItemBinding
import com.example.asteroidrader.model.Asteroid

class AsteroidsAdapter(private val clickListener: AsteroidClickListener) :
    RecyclerView.Adapter<AsteroidsAdapter.AsteroidViewHolder>() {
    class AsteroidViewHolder(var binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val withDataBinding: RowItemBinding =
            RowItemBinding.inflate(LayoutInflater.from(parent.context))
        return AsteroidViewHolder(withDataBinding)
    }

    var asteroids: List<Asteroid>? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        asteroids?.let { list ->
            val asteroid = list[position]

            holder.also {
                it.itemView.setOnClickListener {
                    clickListener.onClick(asteroid)
                }
                it.bind(asteroid)
            }
        }
    }

    override fun getItemCount(): Int {
        asteroids?.let {
            return it.size
        }
        return 0
    }
}