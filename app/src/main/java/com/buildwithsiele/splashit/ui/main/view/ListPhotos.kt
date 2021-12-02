package com.buildwithsiele.splashit.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.databinding.FragmentListPhotosBinding
import com.buildwithsiele.splashit.ui.main.viewmodels.ListImagesViewModel
import com.buildwithsiele.splashit.ui.main.viewmodels.ListImagesViewModelFactory
import com.buildwithsiele.splashit.ui.main.adapters.PhotosAdapter


class ListPhotos : Fragment() {
    private lateinit var binding: FragmentListPhotosBinding
    private lateinit var viewModel: ListImagesViewModel
    private lateinit var adapter: PhotosAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_photos, container, false)

        val database = PhotosDatabase.getInstance(requireContext()).photosDao
        val viewModelFactory = ListImagesViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListImagesViewModel::class.java]
        adapter = PhotosAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.photos.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.loadingBar.visibility = View.GONE
                adapter.photosList = it
            }

        })
        return binding.root
    }


}