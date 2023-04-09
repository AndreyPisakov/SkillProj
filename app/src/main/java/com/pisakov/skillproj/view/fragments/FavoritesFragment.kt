package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.R
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.databinding.FragmentFavoritesBinding
import com.pisakov.skillproj.utils.AnimationHelper
import com.pisakov.skillproj.utils.AutoDisposable
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.utils.addTo
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.viewmodel.FavoriteFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoriteFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        autoDisposable.bindTo(lifecycle)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.favoritesFragmentRoot, requireActivity(), 2)
        initRV(view)
    }

    private fun initRV(view: View) {
        binding.favoritesRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(
                click = { film: Film ->
                    view.findNavController()
                        .navigate(FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(film)) },
                loadNewPage = {}
                )
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
        viewModel.getFilmList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                filmsAdapter.submitList(it)
            }.addTo(autoDisposable)
    }
}