package com.pisakov.skillproj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private val filmsDataBase = listOf(
        Film("Star is born", R.drawable.poster_1, "This should be a description", true),
        Film("Kill Bill", R.drawable.poster_2, "This should be a description", true),
        Film("Bring him home", R.drawable.poster_3, "This should be a description", true),
        Film("Hard candy", R.drawable.poster_4, "This should be a description"),
        Film("John Wick", R.drawable.poster_5, "This should be a description"),
        Film("Фото на память", R.drawable.poster_6, "This should be a description"),
        Film("Color out of space", R.drawable.poster_7, "This should be a description"),
        Film("Маша", R.drawable.poster_8, "This should be a description"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initRV()
    }

    private fun initRV() {
        binding.mainRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener{
                override fun click(film: Film) {
                    (requireActivity() as MainActivity).launchDetailsFragment(film)
                }
            })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())

            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
        filmsAdapter.addItems(filmsDataBase)
    }
}