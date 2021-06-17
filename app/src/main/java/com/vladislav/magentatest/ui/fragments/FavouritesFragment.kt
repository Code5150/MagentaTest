package com.vladislav.magentatest.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.vladislav.magentatest.Helpers
import com.vladislav.magentatest.adapters.ImagesListRecyclerAdapter
import com.vladislav.magentatest.adapters.LikedImagesRecyclerAdapter
import com.vladislav.magentatest.databinding.FragmentFavouritesBinding
import com.vladislav.magentatest.databinding.FragmentMainBinding
import com.vladislav.magentatest.viewmodels.PageViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {

    private val pageViewModel: PageViewModel by activityViewModels()

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root = binding.root
        // Inflate the layout for this fragment

        val adapter =
            LikedImagesRecyclerAdapter(
                pageViewModel.likedPictures.value!!,
                this::dislikeImage,
                requireActivity().filesDir
            )
        binding.recyclerViewFavourites.adapter = adapter

        pageViewModel.likedPictures.observe(viewLifecycleOwner) {
            Log.d(TAG, "Updating list")
            (binding.recyclerViewFavourites.adapter as LikedImagesRecyclerAdapter).updateItems(it)
        }

        return root
    }

    companion object {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        private const val TAG = "Favourites Fragment"
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): FavouritesFragment {
            return FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private fun dislikeImage(id: Int) {
        pageViewModel.removeLike(
            id,
            Helpers.getImage(requireActivity().filesDir, id),
            requireActivity().applicationContext
        )
    }
}