package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.R
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.databinding.FragmentHomeBinding
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.utils.AnimationHelper
import com.pisakov.skillproj.utils.AutoDisposable
import com.pisakov.skillproj.utils.addTo
import com.pisakov.skillproj.viewmodel.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        autoDisposable.bindTo(lifecycle)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(), 1)
        initRV(view)
        eventHandling()
        binding.settingsFab.setOnClickListener {
            view.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
    }

    override fun onStart() {
        super.onStart()
        filmsAdapter.submitList(viewModel.filmsDataBase)
    }

    private fun eventHandling() {
        viewModel.progressBarState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.progressBar.isVisible = it
            }.addTo(autoDisposable)

        viewModel.updatingUIState
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {
                    filmsAdapter.submitList(viewModel.filmsDataBase)
                    viewModel.resetUpdatingState()
                }
            }.addTo(autoDisposable)
    }

    private fun initRV(view: View) {
        binding.mainRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(
                click = { film: Film ->
                    view.findNavController()
                        .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(film)) },
                loadNewPage = { viewModel.loadNewPage() }
            )
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
    }
}
