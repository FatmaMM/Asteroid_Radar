package com.example.asteroidrader.main

import com.example.asteroidrader.model.Asteroid

class AsteroidClickListener (val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}