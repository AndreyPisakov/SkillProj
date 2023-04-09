package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentListBinding
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.utils.AutoDisposable
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.utils.addTo
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.viewmodel.ListFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ListFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        autoDisposable.bindTo(lifecycle)
        val selectionName = ListFragmentArgs.fromBundle(requireArguments()).selectionName
        viewModel.page = 1
        viewModel.selectionName = selectionName
        eventHandling()
        viewModel.loadList()
        initRV(view)
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
                        .navigate(ListFragmentDirections.actionListFragmentToDetailsFragment(film)) },
                loadNewPage = { viewModel.loadList() }
            )
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
    }
}