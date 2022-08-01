package com.pisakov.skillproj

import android.os.Bundle
import android.transition.Scene
import android.transition.Slide
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import com.pisakov.skillproj.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.merge_home_screen_content.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private val filmsDataBase = listOf(
        Film(1,"Star is born", R.drawable.poster_1, "This should be a description", true),
        Film(2,"Kill Bill", R.drawable.poster_2, "This should be a description", true),
        Film(3,"Bring him home", R.drawable.poster_3, "This should be a description", true),
        Film(4,"Hard candy", R.drawable.poster_4, "This should be a description"),
        Film(5,"John Wick", R.drawable.poster_5, "This should be a description"),
        Film(6,"Фото на память", R.drawable.poster_6, "This should be a description"),
        Film(7,"Color out of space", R.drawable.poster_7, "This should be a description"),
        Film(8,"Маша", R.drawable.poster_8, "This should be a description"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        animation()
        initRV(view)
        search()
    }

    private fun animation() {
        val scene = Scene.getSceneForLayout(binding.homeFragmentRoot, R.layout.merge_home_screen_content, requireContext())
        val searchSlide = Slide(Gravity.TOP).addTarget(R.id.search_view)
        val recyclerSlide = Slide(Gravity.BOTTOM).addTarget(R.id.main_recycler)
        val customTransition = TransitionSet().apply {
            duration = 500
            addTransition(recyclerSlide)
            addTransition(searchSlide)
        }
        TransitionManager.go(scene, customTransition)
    }

    private fun search() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
        main_recycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener{
                override fun click(film: Film) {
                    view.findNavController()
                        .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(film))
                }
            })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())

            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
        filmsAdapter.submitList(filmsDataBase)
    }
}