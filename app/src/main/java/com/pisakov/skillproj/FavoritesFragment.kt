package com.pisakov.skillproj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.databinding.FragmentFavoritesBinding
import com.pisakov.skillproj.databinding.FragmentHomeBinding

class FavoritesFragment : Fragment() {

    private lateinit var binding:FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        initRV()
    }

    private fun initRV() {
        val favoritesList: List<Film> = listOf(
            Film("Star is born", R.drawable.poster_1, "This should be a description", true),
            Film("Kill Bill", R.drawable.poster_2, "This should be a description", true),
            Film("Bring him home", R.drawable.poster_3, "This should be a description", true))
        binding.favoritesRecycler.apply {
            val filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            filmsAdapter.addItems(favoritesList)
        }
    }
}