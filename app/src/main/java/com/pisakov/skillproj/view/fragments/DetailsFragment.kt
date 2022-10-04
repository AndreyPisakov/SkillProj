package com.pisakov.skillproj.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.FragmentDetailsBinding
import com.pisakov.skillproj.data.ApiConstants
import com.pisakov.skillproj.domain.Film

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        film = DetailsFragmentArgs.fromBundle(requireArguments()).film
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
    }

    private fun addToFavorite() {
        binding.detailsFabFavorites.setOnClickListener{
            binding.detailsFabFavorites.setImageResource(
                if (!film.isInFavorites) R.drawable.ic_favorite
                else R.drawable.ic_favorite_border
            )
            film.isInFavorites = !film.isInFavorites
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