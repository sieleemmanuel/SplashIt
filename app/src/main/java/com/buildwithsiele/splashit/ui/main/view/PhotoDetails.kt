package com.buildwithsiele.splashit.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.databinding.PhotoDetailsFragmentBinding
import com.buildwithsiele.splashit.ui.main.viewmodels.PhotoDetailsViewModel

class PhotoDetails : Fragment() {
    private lateinit var viewModel: PhotoDetailsViewModel
    private lateinit var binding: PhotoDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.photo_details_fragment, container, false)
        viewModel = ViewModelProvider(this)[PhotoDetailsViewModel::class.java]
       /* val adapter = ViewPagerAdapter(viewModel.imageList)
        binding.imageViewpager.adapter = adapter*/
        return binding.root
    }

}