package com.pisakov.skillproj

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pisakov.skillproj.databinding.FragmentDetailsBinding

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
            detailsPoster.setImageResource(film.poster)
            detailsDescription.text = film.description
        }
        isFavorite()
        addToFavorite()
        share()
    }

    private fun addToFavorite() {
        binding.detailsFabFavorites.setOnClickListener{
            if (!film.isInFavorites) {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_favorite)
                film.isInFavorites = true
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_favorite_border)
                film.isInFavorites = false
            }
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