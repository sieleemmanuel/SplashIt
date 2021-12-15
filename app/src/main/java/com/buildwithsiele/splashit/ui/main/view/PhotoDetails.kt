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
import com.buildwithsiele.splashit.databinding.PhotoDetailsFragmentBinding
import com.buildwithsiele.splashit.adapters.ViewPagerAdapter
import com.buildwithsiele.splashit.ui.main.viewmodels.PhotoDetailsViewModel
import com.buildwithsiele.splashit.ui.main.viewmodels.PhotoDetailsViewModelFactory

class PhotoDetails : Fragment() {
    private lateinit var viewModel: PhotoDetailsViewModel
    private lateinit var binding: PhotoDetailsFragmentBinding
    private lateinit var argument: Bundle
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.photo_details_fragment, container, false)
        val dataSource = PhotosDatabase.getInstance(requireContext()).photosDao
        val viewModelProviderFactory = PhotoDetailsViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[PhotoDetailsViewModel::class.java]

        // val currentPhotoId = argument.getString("photo_id")!!
        argument = requireArguments()
        val currentPhotoPosition = argument.getInt("current_photo_position")
        adapter = ViewPagerAdapter()
        binding.photoViewpager.adapter = adapter


        viewModel.photoList.observe(viewLifecycleOwner, {
            adapter.photoList = it
            binding.photoViewpager.setCurrentItem(currentPhotoPosition,true)
        })
        return binding.root
    }

}