package com.vladislav.magentatest.ui.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vladislav.magentatest.adapters.ImageListRecyclerAdapter
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.databinding.FragmentMainBinding
import com.vladislav.magentatest.ui.main.MainActivity.Companion.JPG
import com.vladislav.magentatest.viewmodels.PageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private val pageViewModel: PageViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        /*val textView: TextView = binding.sectionLabel
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        pageViewModel.initDatabase(requireActivity().applicationContext)

        val screenWidth = resources.configuration.screenWidthDp
        Log.d(TAG, screenWidth.toString())

        val adapter = pageViewModel.pictures.value?.let {
            ImageListRecyclerAdapter(it, screenWidth, this::likeImage)
        } ?: ImageListRecyclerAdapter(emptyList(), screenWidth, this::likeImage)
        binding.recyclerView.adapter = adapter

        pageViewModel.pictures.observe(viewLifecycleOwner) {
            Log.d(TAG, "Updating list")
            (binding.recyclerView.adapter as ImageListRecyclerAdapter).updateItems(it)
        }


        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val TAG = "All Fragment"
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inline fun getImage(id: Int): File = File(activity?.filesDir, "$id$JPG")

    private fun likeImage(img: Photo, d: Drawable) {
        if (img.liked) {
            activity?.filesDir?.let {
                pageViewModel.writeImage(d.toBitmap(), img.id, it)
                pageViewModel.insertIntoDB(img)
            }
        }
        else {
            activity?.filesDir?.let {
                pageViewModel.deleteFromDB(img.id)
                pageViewModel.deleteFile(getImage(img.id), requireActivity().applicationContext)
            }
        }
    }
}