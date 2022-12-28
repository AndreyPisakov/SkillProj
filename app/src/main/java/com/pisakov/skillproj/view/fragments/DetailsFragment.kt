package com.pisakov.skillproj.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pisakov.remote_module.entity.ApiConstants
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentDetailsBinding
import com.pisakov.skillproj.data.entity.Film
import com.pisakov.skillproj.notifications.NotificationConstants
import com.pisakov.skillproj.notifications.NotificationHelper
import com.pisakov.skillproj.viewmodel.DetailsFragmentViewModel

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var film: Film
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        film = arguments?.getParcelable(NotificationConstants.BUNDLE_KEY) ?: DetailsFragmentArgs.fromBundle(requireArguments()).film
        binding.apply {
            detailsToolbar.title = film.title
            Glide.with(view)
                .load(ApiConstants.IMAGES_URL + "w780" + film.poster)
                .centerCrop()
                .into(detailsPoster)
            detailsDescription.text = film.description
            ratingView.setProgress(film.rating.toFloat())
        }
        isFavorite()
        addToFavorite()
        share()

        binding.detailsFabWatchLater.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                NotificationHelper.createNotification(requireContext(), film)
            }
        }
    }

    private fun addToFavorite() {
        binding.detailsFabFavorites.setOnClickListener{
            binding.detailsFabFavorites.setImageResource(
                if (!film.isInFavorites) R.drawable.ic_favorite
                else R.drawable.ic_favorite_border
            )
            film.isInFavorites = !film.isInFavorites
            viewModel.updateFilm(film)
        }
    }

    private fun share() {
        binding.detailsFab.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    private fun isFavorite() {
        binding.detailsFabFavorites.setImageResource(
            if (film.isInFavorites) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
    }
}