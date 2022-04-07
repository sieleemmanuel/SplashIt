package com.buildwithsiele.splashit.ui.view

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.adapters.LoaderStateAdapter
import com.buildwithsiele.splashit.adapters.PhotosAdapter
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.PhotosApi
import com.buildwithsiele.splashit.data.network.monitor.NetworkConnection
import com.buildwithsiele.splashit.databinding.FragmentListPhotosBinding
import com.buildwithsiele.splashit.ui.viewmodels.ListImagesViewModel
import com.buildwithsiele.splashit.ui.viewmodels.ListImagesViewModelFactory
import kotlinx.coroutines.launch


private const val TAG = "ListPhotos"

@ExperimentalPagingApi
class ListPhotos : Fragment() {
    private lateinit var binding: FragmentListPhotosBinding
    private lateinit var viewModel: ListImagesViewModel
    private lateinit var adapter: PhotosAdapter
    private lateinit var loaderStateAdapter: LoaderStateAdapter
    private lateinit var itemClickListener: PhotosAdapter.ItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemClickListener = PhotosAdapter.ItemClickListener { photo, position ->
            findNavController().navigate(
                ListPhotosDirections.actionListImagesToImageDetails(
                    photo.id,
                    position
                )
            )
        }
        adapter = PhotosAdapter(itemClickListener)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_photos, container, false)

        val database = PhotosDatabase.getInstance(requireContext())
        val apiService = PhotosApi.apiService
        val viewModelFactory = ListImagesViewModelFactory(database, apiService)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListImagesViewModel::class.java]

        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
        binding.recyclerView.adapter = adapter.withLoadStateFooter(loaderStateAdapter)

        adapter.addLoadStateListener { combinedLoadStates ->
            val isRefreshing = combinedLoadStates.source.refresh is LoadState.Loading
            binding.loadingBar.isVisible = isRefreshing
        }
        setUpAdapterData(viewModel.query.value.toString()) {
            lifecycleScope.launch {
                adapter.submitData(it)
                adapter.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.actionSearch)
        val searchViewItem = searchItem.actionView as SearchView

        setUpSearchView(searchViewItem)
    }

    private fun setUpSearchView(searchViewItem: SearchView) {
        searchViewItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!NetworkConnection(requireContext()).isNetworkAvailable()) {
                    Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    searchPhotos(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchViewItem.isIconfiedByDefault
    }

    private fun searchPhotos(query: String?) {
        if (!query?.isEmpty()!!) {
            viewModel.updateSearchQuery(query)
            viewModel.fetchPhotosLiveData(query)
            viewModel.query.value?.let {
                setUpAdapterData(it) {
                    lifecycleScope.launch {
                        adapter.submitData(it)
                        adapter.stateRestorationPolicy =
                            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    }
                }
            }
        }else Toast.makeText(context, "oops! no match found", Toast.LENGTH_SHORT).show()
    }

    private fun setUpAdapterData(query: String="",completionHandler: (pagingData: PagingData<Photo>) -> Unit) {
        viewModel.fetchPhotosLiveData(query)
        viewModel.photosPagingData?.observe(viewLifecycleOwner, { pagingData ->
            completionHandler.invoke(pagingData)
            viewModel.updateSearchQuery(query)
        })
    }
}

