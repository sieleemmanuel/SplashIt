package com.buildwithsiele.splashit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R

class LoaderStateAdapter(private val retry: ()->Unit):
LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>(){

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder.getInstance(parent, retry)
    }

    class LoaderViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view){

        companion object{
            // instance of loaderViewHolder
            fun getInstance(parent: ViewGroup,retry: () -> Unit):LoaderViewHolder{
                val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_loader, parent, false)
                return LoaderViewHolder(inflater,retry)

            }
        }
        private val motionLayout:MotionLayout = view.findViewById(R.id.mlLoader)
        init {
            view.findViewById<Button>(R.id.btnRetry).setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading){
                motionLayout.transitionToEnd()
            }else{
                motionLayout.transitionToStart()
            }

        }

    }


}