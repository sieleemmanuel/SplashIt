package com.buildwithsiele.splashit.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R

class ViewPagerAdapter(private val imagesList: MutableList<Int>):RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder>() {
    class ViewPagerHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.imagePager)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
    return ViewPagerHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_pager,parent,false))
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        val currentImage = imagesList[position]
        holder.imageView.setImageResource(currentImage)

    }

    override fun getItemCount(): Int = imagesList.size

}