package com.buildwithsiele.splashit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.monitor.NetworkConnection
import com.squareup.picasso.Picasso

class ViewPagerAdapter(private val context: Context) :ListAdapter<Photo,ViewPagerAdapter.ViewPagerHolder>(PhotosAdapter.DiffUtilCallback()) {
    inner class ViewPagerHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.imagePager)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
    return ViewPagerHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_pager,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val currentImage = getItem(position)
        var photoUrl = ""
        photoUrl = if (NetworkConnection(context).isNetworkAvailable()) {
            currentImage?.urls?.urlRegular!!
        }else{
            currentImage?.urls?.urlSmall!!
        }
        Picasso.get()
            .load(photoUrl)
            .placeholder(R.drawable.progress_animation)
            .into(holder.imageView)
        holder.imageView.tag = position

    }

}
