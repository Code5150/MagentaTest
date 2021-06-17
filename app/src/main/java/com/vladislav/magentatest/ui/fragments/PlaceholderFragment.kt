package com.vladislav.magentatest.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vladislav.magentatest.Helpers
import com.vladislav.magentatest.adapters.ImagesListRecyclerAdapter
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.databinding.FragmentFavouritesBinding
import com.vladislav.magentatest.databinding.FragmentMainBinding
import com.vladislav.magentatest.ui.main.MainActivity.Companion.JPG
import com.vladislav.magentatest.viewmodels.PageViewModel
import java.io.File

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private val pageViewModel: PageViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        pageViewModel.initDatabase(requireActivity().applicationContext)

        val adapter = pageViewModel.pictures.value?.let {
            ImagesListRecyclerAdapter(it, this::likeImage)
        } ?: ImagesListRecyclerAdapter(emptyList(), this::likeImage)
        binding.recyclerView.adapter = adapter

        pageViewModel.pictures.observe(viewLifecycleOwner) {
            Log.d(TAG, "Updating list")
            (binding.recyclerView.adapter as ImagesListRecyclerAdapter).updateItems(it)
        }

        pageViewModel.likedPictures.observe(viewLifecycleOwner) {
            pageViewModel.pictures.value?.forEachIndexed{ pos, img ->
                if (img.liked && !(pageViewModel.likedPictures.value?.contains(img.id))!!)
                    (binding.recyclerView.adapter as ImagesListRecyclerAdapter).notifyItemChanged(pos)
            }
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

    private fun likeImage(img: Photo, d: Drawable) {
        if (img.liked) {
            activity?.filesDir?.let {
                pageViewModel.likeImage(d.toBitmap(), img, it)
            }
        }
        else {
            pageViewModel.removeLike(
                img.id,
                Helpers.getImage(requireActivity().filesDir, img.id),
                requireActivity().applicationContext
            )
        }
    }
}