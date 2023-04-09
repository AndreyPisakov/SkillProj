package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentSettingsBinding
import com.pisakov.skillproj.utils.Selections
import com.pisakov.skillproj.viewmodel.SettingsFragmentViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SettingsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categoryPropertyLifeData.observe(viewLifecycleOwner) {
            when (it) {
                Selections.POPULAR_CATEGORY -> binding.radioGroup.check(R.id.radio_popular)
                Selections.TOP_RATED_CATEGORY -> binding.radioGroup.check(R.id.radio_top_rated)
                Selections.UPCOMING_CATEGORY -> binding.radioGroup.check(R.id.radio_upcoming)
                Selections.NOW_PLAYING_CATEGORY -> binding.radioGroup.check(R.id.radio_now_playing)
            }
        }
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_popular -> viewModel.putCategoryProperty(Selections.POPULAR_CATEGORY)
                R.id.radio_top_rated -> viewModel.putCategoryProperty(Selections.TOP_RATED_CATEGORY)
                R.id.radio_upcoming -> viewModel.putCategoryProperty(Selections.UPCOMING_CATEGORY)
                R.id.radio_now_playing -> viewModel.putCategoryProperty(Selections.NOW_PLAYING_CATEGORY)
            }
        }
    }
}