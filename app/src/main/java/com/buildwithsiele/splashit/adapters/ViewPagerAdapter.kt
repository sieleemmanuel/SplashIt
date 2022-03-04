package com.buildwithsiele.splashit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.data.model.Photo
import com.squareup.picasso.Picasso

class ViewPagerAdapter:ListAdapter<Photo,ViewPagerAdapter.ViewPagerHolder>(PhotosAdapter.DiffUtilCallback()) {
    class ViewPagerHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.imagePager)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
    return ViewPagerHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_pager,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val currentImage = getItem(position)

        Picasso.get()
            .load(currentImage?.urls?.urlSmall)
            .placeholder(R.drawable.progress_animation)
            .into(holder.imageView)

    }

}