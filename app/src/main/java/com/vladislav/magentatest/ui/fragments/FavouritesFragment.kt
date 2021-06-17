package com.vladislav.magentatest.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vladislav.magentatest.adapters.LikedImagesRecyclerAdapter
import com.vladislav.magentatest.databinding.FragmentFavouritesBinding
import com.vladislav.magentatest.other.Helpers
import com.vladislav.magentatest.viewmodels.PageViewModel


class FavouritesFragment : Fragment() {

    private val pageViewModel: PageViewModel by activityViewModels()

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root = binding.root

        val adapter =
            LikedImagesRecyclerAdapter(
                pageViewModel.likedPictures.value,
                this::dislikeImage,
                requireActivity().filesDir
            )
        binding.recyclerViewFavourites.adapter = adapter

        pageViewModel.likedPictures.observe(viewLifecycleOwner) {
            (binding.recyclerViewFavourites.adapter as LikedImagesRecyclerAdapter).updateItems(it)
        }

        return root
    }

    companion object {
        private const val TAG = "Favourites Fragment"

        @JvmStatic
        fun newInstance(): FavouritesFragment {
            return FavouritesFragment()
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