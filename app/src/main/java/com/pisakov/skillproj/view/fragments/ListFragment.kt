package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentListBinding
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.viewmodel.ListFragmentViewModel
import kotlinx.coroutines.launch
import java.util.*

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ListFragmentViewModel::class.java)
    }
    private var filmsDataBase = listOf<Film>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.submitList(field)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        val selectionName = ListFragmentArgs.fromBundle(requireArguments()).selectionName
        viewModel.page = 1
        viewModel.selectionName = selectionName
        eventHandling()
        viewModel.loadList()
        initRV(view)
        search()
    }

    private fun eventHandling() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.progressBarStateFlow.collect {
                    binding.progressBar.isVisible = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updatingUIStateFlow.collect {
                    if (it) {
                        filmsAdapter.submitList(viewModel.filmsDataBase)
                        viewModel.resetUpdatingState()
                    }
                }
            }
        }
    }

    private fun search() {
        binding.searchView.setOnClickListener { binding.searchView.isIconified = false }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    filmsAdapter.submitList(filmsDataBase)
                    return true
                }
                val result = filmsDataBase.filter {
                    it.title.lowercase(Locale.getDefault()).contains(newText.lowercase(Locale.getDefault()))
                }
                filmsAdapter.submitList(result)
                return true
            }
        })
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
        filmsAdapter.submitList(filmsDataBase)
    }
}