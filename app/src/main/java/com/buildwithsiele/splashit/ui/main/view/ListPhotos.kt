package com.buildwithsiele.splashit.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.adapters.LoaderStateAdapter
import com.buildwithsiele.splashit.adapters.PhotosAdapter
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.network.PhotosApi
import com.buildwithsiele.splashit.databinding.FragmentListPhotosBinding
import com.buildwithsiele.splashit.ui.main.viewmodels.ListImagesViewModel
import com.buildwithsiele.splashit.ui.main.viewmodels.ListImagesViewModelFactory


@ExperimentalPagingApi
class ListPhotos : Fragment() {
    private lateinit var binding: FragmentListPhotosBinding
    private lateinit var viewModel: ListImagesViewModel
    private lateinit var adapter: PhotosAdapter
    private lateinit var loaderStateAdapter: LoaderStateAdapter
    private lateinit var itemClickListener: PhotosAdapter.ItemClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_photos, container, false)

        val database = PhotosDatabase.getInstance(requireContext())
        val apiService = PhotosApi.apiService

        val viewModelFactory = ListImagesViewModelFactory(database, apiService)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListImagesViewModel::class.java]

        itemClickListener = PhotosAdapter.ItemClickListener { photo, position ->
            findNavController().navigate(
                ListPhotosDirections.actionListImagesToImageDetails(
                    photo.id,
                    position
                )
            )
        }

        adapter = PhotosAdapter(itemClickListener)

        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
        binding.recyclerView.adapter = adapter.withLoadStateFooter(loaderStateAdapter)

        /*viewModel.photos.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.loadingBar.visibility = View.GONE
                adapter.photosList = it
            }
        })*/
        setUpAdapterData()
        return binding.root
    }

    private fun setUpAdapterData() {
        viewModel.fetchPhotosLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launchWhenCreated {
                if (it != null)
                    binding.loadingBar.visibility = View.GONE
                adapter.submitData(it)
            }
        })
    }


}

