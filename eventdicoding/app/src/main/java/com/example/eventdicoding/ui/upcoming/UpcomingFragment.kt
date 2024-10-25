package com.example.eventdicoding.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicoding.R
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.databinding.FragmentUpcomingBinding
import com.example.eventdicoding.ui.EventAdapter


class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter
    private lateinit var upcomingViewModel: UpcomingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi ViewModel
        upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        // Inisialisasi View Binding
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        // Inisialisasi RecyclerView dan Adapter dengan onItemClick untuk navigasi
        eventAdapter = EventAdapter { selectedEvent: ListEventsItem -> // Tambahkan tipe ListEventsItem di sini
            val bundle = Bundle().apply {
                putParcelable("selectedEvent", selectedEvent)
            }
            findNavController().navigate(R.id.detailFragment, bundle)
        }

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        // Observasi perubahan data dari ViewModel
        upcomingViewModel.events.observe(viewLifecycleOwner) { eventList ->
            eventAdapter.submitList(eventList)
            binding.progressBar.visibility = View.GONE // Sembunyikan ProgressBar saat data telah dimuat
        }

        // Observasi status loading
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
