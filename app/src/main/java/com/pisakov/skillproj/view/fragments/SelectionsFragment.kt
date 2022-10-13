package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentSelectionsBinding
import com.pisakov.skillproj.utils.AnimationHelper
import com.pisakov.skillproj.utils.Selections
import com.pisakov.skillproj.utils.TopSpacingItemDecoration
import com.pisakov.skillproj.view.rv_adapters.SelectionListRecyclerAdapter

class SelectionsFragment : Fragment() {
    private lateinit var binding: FragmentSelectionsBinding
    private lateinit var selectionsAdapter: SelectionListRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_selections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectionsBinding.bind(view)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.selectionsFragmentRoot, requireActivity(), 4)
        initRV(view)
    }

    private fun initRV(view: View) {
        val list = listOf(
            Selections.popular,
            Selections.top_rated,
            Selections.latest,
            Selections.now_playing,
            Selections.upcoming
        )
        binding.mainRecycler.apply {
            selectionsAdapter = SelectionListRecyclerAdapter(object : SelectionListRecyclerAdapter.OnItemClickListener {
                override fun click(title: String) {
                    view.findNavController()
                        .navigate(SelectionsFragmentDirections.actionSelectionsFragmentToListFragment(title))
                }
            }, list)
            adapter = selectionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(TopSpacingItemDecoration(8))
        }
    }
}