package com.vladislav.magentatest.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vladislav.magentatest.adapters.ImagesPagingAdapter
import com.vladislav.magentatest.data.Photo
import com.vladislav.magentatest.databinding.FragmentMainBinding
import com.vladislav.magentatest.other.Helpers
import com.vladislav.magentatest.other.PhotoComparator
import com.vladislav.magentatest.viewmodels.PageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A placeholder fragment containing a simple view.
 */
class ImagesFragment : Fragment() {

    private val pageViewModel: PageViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        val adapter = ImagesPagingAdapter(PhotoComparator, this::likeImage)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { states ->
                when (states.refresh) {
                    is LoadState.Error -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.lostText.isVisible = true
                        binding.recyclerView.isVisible = false
                    }
                    is LoadState.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    is LoadState.NotLoading -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.lostText.isVisible = false
                        binding.recyclerView.isVisible = true
                    }
                }
                /*if (states.refresh is LoadState.Error) lifecycleScope.launch(Dispatchers.Default) {
                    delay(1500L)
                    adapter.retry()
                }*/
            }
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lm = recyclerView.layoutManager as LinearLayoutManager
                binding.swipeRefreshLayout.isEnabled = lm.findFirstVisibleItemPosition() == 0
            }
        })
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        pageViewModel.likedPictures.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        pageViewModel.pld.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
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
        fun newInstance(sectionNumber: Int): ImagesFragment {
            return ImagesFragment().apply {
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
        } else {
            pageViewModel.removeLike(
                img.id,
                Helpers.getImage(requireActivity().filesDir, img.id),
                requireActivity().applicationContext
            )
        }
    }
}