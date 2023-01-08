package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.R
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.databinding.FragmentSearchBinding
import com.pisakov.skillproj.utils.AnimationHelper
import com.pisakov.skillproj.utils.AutoDisposable
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.utils.addTo
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.viewmodel.SearchFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SearchFragmentViewModel::class.java)
    }
    private val autoDisposable = AutoDisposable()

    private var searchQuery = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        autoDisposable.bindTo(lifecycle)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.searchFragmentRoot, requireActivity(), 3)
        initRV(view)
        search()
    }

    private fun initRV(view: View) {
        binding.mainRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(
                click = { film: Film ->
                    view.findNavController()
                        .navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(film)) },
                loadNewPage = {
                    viewModel.loadNewPage(searchQuery)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onError = {
                                Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                            },
                            onNext = {
                                viewModel.filmsDataBase = (viewModel.filmsDataBase + it) as MutableList<Film>
                                filmsAdapter.submitList(viewModel.filmsDataBase)
                            }
                        )
                }
            )
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
    }

    private fun search() {
        binding.searchView.setOnClickListener { binding.searchView.isIconified = false }
        Observable.create { subscriber ->
            binding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    newQuery(newText)
                    subscriber.onNext(newText)
                    return false
                }
                override fun onQueryTextSubmit(query: String): Boolean {
                    newQuery(query)
                    subscriber.onNext(query)
                    return false
                }
            })
        }
            .subscribeOn(Schedulers.io())
            .map {
                it.lowercase(Locale.getDefault()).trim()
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .filter {
                listOf<Film>()
                it.isNotBlank()
            }
            .flatMap {
                viewModel.pageQuery = 0
                viewModel.loadNewPage(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                },
                onNext = {
                    viewModel.filmsDataBase = (viewModel.filmsDataBase + it) as MutableList<Film>
                    filmsAdapter.submitList(viewModel.filmsDataBase)
                }
            )
            .addTo(autoDisposable)
    }

    private fun newQuery(newText: String) {
        searchQuery = newText
        viewModel.filmsDataBase = mutableListOf()
        filmsAdapter.submitList(viewModel.filmsDataBase)
    }
}