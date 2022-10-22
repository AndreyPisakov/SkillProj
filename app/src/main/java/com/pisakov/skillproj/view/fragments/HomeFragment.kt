package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.view.rv_adapters.FilmListRecyclerAdapter
import com.pisakov.skillproj.R
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.databinding.FragmentHomeBinding
import com.pisakov.skillproj.domain.Film
import com.pisakov.skillproj.utils.AnimationHelper
import com.pisakov.skillproj.utils.Selections
import com.pisakov.skillproj.viewmodel.HomeFragmentViewModel
import java.util.*

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private var filmsDataBase = listOf<Film>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.submitList(field)
            Log.d("MyLog", "db")
            Log.d("MyLog", "$value")
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("MyLog", "main")
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        viewModel.filmListLiveData.observe(viewLifecycleOwner){
            Log.d("MyLog", "observe")
            Log.d("MyLog", "$it")
            filmsDataBase = it
        }
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(), 1)
        initSpinner()
        Log.d("MyLog", "clean")
        //viewModel.cleanList()
        initRV(view)
        search()
        initPullToRefresh()
    }

    private fun initSpinner() {
        Log.d("MyLog", "initSpinner")
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.selections,
            android.R.layout.simple_spinner_item).also{ adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        binding.spinner.onItemSelectedListener = this
        binding.spinner.setSelection(viewModel.getMainCategoryInt())
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        Log.d("MyLog", "onItemSelected")
        Log.d("MyLog", "clean")
        viewModel.cleanList()
        viewModel.loadNewPage(viewModel.getChoosingCategory(pos))
    }
    override fun onNothingSelected(parent: AdapterView<*>) {}

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
        Log.d("MyLog", "initRV")
        binding.mainRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                override fun click(film: Film) {
                    view.findNavController()
                        .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(film))
                }
            }, object : FilmListRecyclerAdapter.Paging {
                override fun loadNewPage() {
                    Log.d("MyLog", "load from adapter")
                    viewModel.loadNewPage()
                }
            })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
        //filmsAdapter.submitList(filmsDataBase)
    }

    private fun initPullToRefresh() {
        Log.d("MyLog", "initPullRefresh")
        binding.pullToRefresh.setOnRefreshListener {
            Log.d("MyLog", "Refresh")
            Log.d("MyLog", "clean")
            viewModel.cleanList()
            viewModel.loadNewPage(viewModel.getMainCategory())
            binding.pullToRefresh.isRefreshing = false
            binding.spinner.setSelection(viewModel.getMainCategoryInt())
        }
    }
}