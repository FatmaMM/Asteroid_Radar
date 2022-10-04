package com.example.asteroidrader.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asteroidrader.R
import com.example.asteroidrader.databinding.FragmentMainBinding
import com.example.asteroidrader.model.Asteroid
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val asteroidAdapter = AsteroidsAdapter(AsteroidClickListener { asteroid ->
        viewModel.onAsteroidClicked(asteroid)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = asteroidAdapter
        setHasOptionsMenu(true)

        viewModel.navigateToDetailAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            if (asteroid != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidNavigated()
            }
        })
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            Picasso.with(this@MainFragment.context).load(it.url).fit().placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.asteroid_safe)
                .into(binding.activityMainImageOfTheDay)
            binding.textView.text = it.title
        })

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroid ->
            asteroid.apply {
                asteroidAdapter.asteroids = (this)
                asteroidAdapter.notifyDataSetChanged()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onChangeFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> {
                    SelectedAsteroid.TODAY
                }
                R.id.show_all_menu -> {
                    SelectedAsteroid.WEEK
                }
                else -> {
                    SelectedAsteroid.ALL
                }
            }
        )
        return true
    }
}
