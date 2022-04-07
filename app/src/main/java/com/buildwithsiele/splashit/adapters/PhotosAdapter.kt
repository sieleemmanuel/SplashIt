package com.buildwithsiele.splashit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.data.model.Photo
import com.squareup.picasso.Picasso

class PhotosAdapter (private val itemClickListener: ItemClickListener)
    : PagingDataAdapter<Photo,PhotosAdapter.ImagesViewHolder>(DiffUtilCallback()) {

   inner class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val photo = getItem(position)
        holder.imageName.text = photo?.id

        Picasso.get()
            .load(photo?.urls?.urlSmall)
            .placeholder(R.drawable.progress_animation)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(photo!!,position)
        }
    }

    class ItemClickListener(val clickListener:(photo:Photo,position: Int)->Unit){
        fun onClick(photo: Photo, position: Int) = clickListener(photo,position)
    }

    class DiffUtilCallback:DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
           return oldItem == newItem
        }

    }
}