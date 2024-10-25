package com.example.eventdicoding.ui.finished

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eventdicoding.R
import com.example.eventdicoding.databinding.FragmentFinishedBinding
import com.example.eventdicoding.ui.EventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter
    private lateinit var finishedViewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        finishedViewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        // Inisialisasi RecyclerView dan Adapter
        setupRecyclerView()

        // Observasi data dari ViewModel
        finishedViewModel.filteredEvents.observe(viewLifecycleOwner) { eventList ->
            Log.d("FinishedFragment", "Loaded ${eventList.size} Finished Events") // Debugging
            eventAdapter.submitList(eventList)
            binding.tvNoData.visibility = if (eventList.isEmpty()) View.VISIBLE else View.GONE
        }

        // Implementasi pencarian pada SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                finishedViewModel.filterEvents(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                finishedViewModel.filterEvents(newText ?: "")
                return true
            }
        })

        // Observasi pesan error
        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Log.e("FinishedFragment", message)
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.tvErrorMessage.text = message
            } else {
                binding.tvErrorMessage.visibility = View.GONE
            }
        }

        // Observasi status loading
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        // Gunakan LayoutType.Grid untuk tampilan grid
        eventAdapter = EventAdapter(EventAdapter.LayoutType.Grid) { selectedEvent ->
            // Implementasi navigasi ke halaman detail ketika item event diklik
            val bundle = Bundle().apply { putParcelable("selectedEvent", selectedEvent) }
            findNavController().navigate(R.id.action_finishedFragment_to_detailFragment, bundle)
        }

        binding.rvFinishedEvents.apply {
            layoutManager = GridLayoutManager(context, 2) // 2 adalah jumlah kolom dalam grid
            adapter = eventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
