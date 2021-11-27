package com.buildwithsiele.splashit.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.databinding.FragmentListImagesBinding
import com.buildwithsiele.splashit.ui.listimages.ListImagesViewModel
import com.buildwithsiele.splashit.ui.listimages.ListImagesViewModelFactory
import com.buildwithsiele.splashit.ui.main.adapter.ImagesAdapter


class ListImages : Fragment() {
    private lateinit var binding: FragmentListImagesBinding
    private lateinit var viewModel: ListImagesViewModel
    private lateinit var adapter: ImagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_images, container, false)

        val viewModelFactory = ListImagesViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[ListImagesViewModel::class.java]
        adapter = ImagesAdapter()
        binding.recyclerView.adapter = adapter

        /*val images = listOf(
            Image("01",100,100,R.drawable.social_media),
            Image("01",100,100,R.drawable.ic_launcher_background),
            Image("02",100,100,R.drawable.bluekeyboard),
            Image("03",100,100,R.drawable.ic_launcher_background)
        )
        adapter.ImagesList = images*/

        viewModel.photos.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.loadingBar.visibility = View.GONE
                adapter.ImagesList = it
            }

        })
        return binding.root
    }


}