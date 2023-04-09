package com.pisakov.skillproj.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pisakov.remote_module.entity.ApiConstants
import com.pisakov.skillproj.databinding.MergePromoBinding

class PromoView(context: Context, attributeSet: AttributeSet?) : FrameLayout(context, attributeSet) {
    val binding = MergePromoBinding.inflate(LayoutInflater.from(context), this)
    val watchButton = binding.watchButton

    fun setLinkForPoster(link: String) {
        Glide.with(binding.root)
            .load(ApiConstants.IMAGES_URL  + "w500" + link)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(ROUNDED_CORNERS)))
            .into(binding.poster)
    }

    companion object {
        const val ROUNDED_CORNERS = 55
    }
}