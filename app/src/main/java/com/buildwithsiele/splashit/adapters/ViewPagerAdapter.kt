package com.buildwithsiele.splashit.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.data.model.Photo
import com.squareup.picasso.Picasso

class ViewPagerAdapter:RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>() {
    var photoList= listOf<Photo>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    class ViewPagerHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.imagePager)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
    return ViewPagerHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_pager,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val currentImage = photoList[position]
        Picasso.get().load(currentImage.urls.urlRegular).into(holder.imageView)
//        holder.imageView.setImageResource(currentImage)

    }

    override fun getItemCount(): Int = photoList.size

}