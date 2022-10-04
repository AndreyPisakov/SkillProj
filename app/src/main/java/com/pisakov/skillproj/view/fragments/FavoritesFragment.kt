package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.R
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.databinding.FragmentFavoritesBinding
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.utils.AnimationHelper

class FavoritesFragment : Fragment() {

    private lateinit var binding:FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.favoritesFragmentRoot, requireActivity(), 2)
        initRV(view)
    }

    private fun initRV(view: View) {
//        val favoritesList: List<Film> = listOf(
//            Film(1,"Star is born", R.drawable.poster_1, 2.2f, "This should be a description", true),
//            Film(2,"Kill Bill", R.drawable.poster_2, 9.9f,"This should be a description", true),
//            Film(3,"Bring him home", R.drawable.poster_3, 4.7f,"This should be a description", true)
//        )
//        binding.favoritesRecycler.apply {
//            val filmsAdapter =
//                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
//                    override fun click(film: Film) {
//                        view.findNavController().navigate(FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(film))
//                    }
//                })
//            adapter = filmsAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//            addItemDecoration(TopSpacingItemDecoration(8))
//            filmsAdapter.submitList(favoritesList)
//        }
    }
}