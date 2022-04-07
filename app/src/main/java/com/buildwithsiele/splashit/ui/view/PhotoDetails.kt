package com.buildwithsiele.splashit.ui.view

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.buildwithsiele.splashit.R
import com.buildwithsiele.splashit.adapters.ViewPagerAdapter
import com.buildwithsiele.splashit.data.database.PhotosDatabase
import com.buildwithsiele.splashit.data.model.Photo
import com.buildwithsiele.splashit.data.network.monitor.NetworkConnection
import com.buildwithsiele.splashit.databinding.PhotoDetailsFragmentBinding
import com.buildwithsiele.splashit.ui.viewmodels.PhotoDetailsViewModel
import com.buildwithsiele.splashit.ui.viewmodels.PhotoDetailsViewModelFactory
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.properties.Delegates

private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

@ExperimentalPagingApi
class PhotoDetails : Fragment() {
    private lateinit var viewModel: PhotoDetailsViewModel
    private lateinit var binding: PhotoDetailsFragmentBinding
    private lateinit var argument: Bundle
    private lateinit var adapter: ViewPagerAdapter
    private var currentPhotoPosition by Delegates.notNull<Int>()
    private lateinit var currentPhoto: Photo
    private lateinit var imageToShare: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.photo_details_fragment, container, false)
        val dataSource = PhotosDatabase.getInstance(requireContext())
        val viewModelProviderFactory = PhotoDetailsViewModelFactory(dataSource, requireContext())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[PhotoDetailsViewModel::class.java]

        argument = requireArguments()
        currentPhotoPosition = argument.getInt("current_photo_position")
        adapter = ViewPagerAdapter(requireContext())
        binding.photoViewpager.adapter = adapter

        adaptData {
            lifecycleScope.launch {
                adapter.submitList(it)
                binding.photoViewpager.setCurrentItem(currentPhotoPosition, false)
            }
            Log.d("CurrentList", "${adapter.currentList.size} ")
        }
        Log.d("OnCreateViewCurrentList", "${adapter.currentList.size} ")
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressed()

            R.id.actionDownload -> askPermission()

            R.id.actionShare -> share()

            R.id.actionSetAs -> useImageAs()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun adaptData(completionHandler: (photos: List<Photo>) -> Unit) {
        viewModel.photosList.observe(viewLifecycleOwner, { photos ->
            completionHandler.invoke(photos)
        })
    }

    private fun useImageAs() {
        val imageUri = saveImageToCacheAndGetUri()
        val setAsIntent = Intent(Intent.ACTION_ATTACH_DATA).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            setDataAndType(imageUri, "image/*")
            putExtra("mimeType", "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(setAsIntent, "Set as:"))
    }

    private fun share() {
      val bitmapCacheUrl:Uri =  saveImageToCacheAndGetUri()!!
        val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, bitmapCacheUrl)
            }
            startActivity(Intent.createChooser(shareIntent, "Share with"))

    }

    private fun saveImageToCacheAndGetUri(): Uri? {
        val imgView =
            binding.photoViewpager.findViewWithTag<ImageView>(binding.photoViewpager.currentItem)
        imageToShare = imgView.drawable.toBitmap()
        currentPhoto = adapter.currentList[binding.photoViewpager.currentItem]

        val filename = "${currentPhoto.id}}.png"
        val cacheDir = requireContext().cacheDir
        val bitmapFile = File(cacheDir, filename)
        bitmapFile.delete()
        bitmapFile.createNewFile()

        val fos = bitmapFile.outputStream()
        val byteArrayInputStream = ByteArrayOutputStream()
        imageToShare.compress(Bitmap.CompressFormat.PNG, 100, byteArrayInputStream)
        val byteArray = byteArrayInputStream.toByteArray()
        fos.apply {
            write(byteArray)
            flush()
            close()
        }
        byteArrayInputStream.close()

        if (bitmapFile.exists()) {
            return FileProvider.getUriForFile(
                requireContext(),
                "com.buildwithsiele.splashit.fileprovider",
                bitmapFile
            )
        }
        return null
    }

    private fun downloadPhoto() {
        val directory = File(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) directory.mkdir()
        val currentPhoto = adapter.currentList[currentPhotoPosition]
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(currentPhoto.download.download)

        val request = DownloadManager.Request(uri)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setDestinationInExternalPublicDir(
                directory.toString(),
                "/${getString(R.string.app_name)}/${currentPhoto.id}.png"
            )
        val downloadId = downloadManager.enqueue(request)
        if (!NetworkConnection(requireContext()).isNetworkAvailable()) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            downloadManager.remove(downloadId)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("Permission required to save image")
                    .setPositiveButton("Allow") { _, _ ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        activity?.onBackPressed()
                    }
                    .setNegativeButton("Deny") { dialog, _ -> dialog.cancel() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        } else {
            downloadPhoto()
        }
    }


}