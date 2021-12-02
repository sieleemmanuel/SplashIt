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

class PhotosAdapter (private val itemClickListener: ItemClickListener): RecyclerView.Adapter<PhotosAdapter.ImagesViewHolder>() {

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
        val photo = photosList[position]
        holder.imageName.text = photo.id
        Picasso.get().load(photo.urls.urlSmall).into(holder.imageView)
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(photo,position)
        }
    }
    override fun getItemCount(): Int = photosList.size

    class ItemClickListener(val clickListener:(photo:Photo,position: Int)->Unit){
        fun onClick(photo: Photo, position: Int) = clickListener(photo,position)
    }
}