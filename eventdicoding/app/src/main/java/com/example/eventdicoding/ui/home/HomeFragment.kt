package com.example.eventdicoding.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicoding.R
import com.example.eventdicoding.databinding.FragmentHomeBinding
import com.example.eventdicoding.ui.EventAdapter
import com.example.eventdicoding.data.response.ListEventsItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize ViewModel
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Initialize view binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerViews and Adapters
        setupRecyclerView()

        // Observe ViewModel for data changes
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }

        // Observe Error Messages
        homeViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            binding.tvErrorMessage.visibility = if (message.isNotEmpty()) View.VISIBLE else View.GONE
            binding.tvErrorMessage.text = message
        }

        // Observe Loading State
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        // Initialize Adapter for Upcoming Events (Horizontal Layout)
        upcomingAdapter = EventAdapter(EventAdapter.LayoutType.Horizontal) { event ->
            navigateToDetail(event)
        }

        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        // Initialize Adapter for Finished Events (Vertical Layout)
        finishedAdapter = EventAdapter(EventAdapter.LayoutType.Vertical) { event ->
            navigateToDetail(event)
        }

        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }
    }

    private fun navigateToDetail(event: ListEventsItem) {
        val bundle = Bundle().apply {
            putParcelable("selectedEvent", event)
        }
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
