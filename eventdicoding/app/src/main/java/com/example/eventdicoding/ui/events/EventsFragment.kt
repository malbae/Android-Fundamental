package com.example.eventdicoding.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.databinding.FragmentEventsBinding
import com.example.eventdicoding.ui.EventAdapter

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView dengan LinearLayoutManager
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Buat adapter dengan LayoutType yang benar
        val adapter = EventAdapter(EventAdapter.LayoutType.Vertical) { event ->
            // Handler saat item di-klik
            openEventDetail(event)
        }

        // Set adapter ke RecyclerView
        binding.recyclerView.adapter = adapter

        // Simulasikan data atau cek apakah data kosong
        val events = listOf<ListEventsItem>() // Ganti dengan data asli
        if (events.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            adapter.submitList(events)
        }
    }

    private fun openEventDetail(event: ListEventsItem) {
        // Implementasi untuk membuka detail event
        // Misalnya, menggunakan Navigation Component untuk pindah ke FragmentDetail
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
