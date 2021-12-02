package com.buildwithsiele.splashit.ui.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.data.model.Photo
import com.squareup.picasso.Picasso

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ImagesViewHolder>() {

    var photosList = listOf<Photo>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageName: TextView = itemView.findViewById(R.id.image_name)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_item,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = photosList[position]
        holder.imageName.text = image.id
        Picasso.get().load(image.urls.imageUrl).into(holder.imageView)
    }
    override fun getItemCount(): Int = photosList.size
}