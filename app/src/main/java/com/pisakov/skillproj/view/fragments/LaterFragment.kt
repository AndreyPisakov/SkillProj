package com.pisakov.skillproj.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentLaterBinding
import com.pisakov.skillproj.utils.AnimationHelper

class LaterFragment : Fragment() {

    private lateinit var binding: FragmentLaterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_later, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaterBinding.bind(view)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.laterFragmentRoot, requireActivity(), 3)
    }
}