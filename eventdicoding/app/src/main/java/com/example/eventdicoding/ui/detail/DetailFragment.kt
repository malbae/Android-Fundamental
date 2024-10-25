package com.example.eventdicoding.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.eventdicoding.database.room.FavoriteUserRoomDatabase
import com.example.eventdicoding.database.room.FavoriteUserDao
import com.example.eventdicoding.database.local.entity.FavoriteUser
import com.example.eventdicoding.detail.DetailViewModel
import com.example.eventdicoding.R
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.databinding.FragmentDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private lateinit var favoriteUserDao: FavoriteUserDao
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi FavoriteUserDao dari database
        favoriteUserDao = FavoriteUserRoomDatabase.getDatabase(requireContext())!!.favoriteUserDao()

        // Inisialisasi ViewModel dengan factory
        val factory = DetailViewModelFactory(favoriteUserDao)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        // Mengatur listener untuk tombol back di toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Kembali ke fragment sebelumnya
        }

        // Mengambil data selectedEvent dari arguments
        val selectedEvent = arguments?.getParcelable<ListEventsItem>("selectedEvent")

        // Jika selectedEvent tidak null, tampilkan data di UI
        selectedEvent?.let { event ->
            displayEventDetails(event)

            // Cek apakah event ini sudah ditambahkan ke favorit
            checkIfFavorite(event.id)

            // Set listener untuk ikon favorit
            binding.ivFavorite.setOnClickListener {
                toggleFavorite(event)
            }
        }
    }

    private fun displayEventDetails(event: ListEventsItem) {
        binding.apply {
            // Mengatur data yang diterima pada elemen UI
            tvEventName.text = event.name
            tvEventDate.text = event.beginTime
            tvEventLocation.text = event.cityName
            tvOrganizerName.text = event.ownerName
            tvQuota.text = "${event.quota - event.registrants} sisa kuota"

            // Menampilkan deskripsi dengan format HTML agar lebih rapi
            tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

            // Menampilkan gambar menggunakan Glide
            Glide.with(this@DetailFragment)
                .load(event.mediaCover)
                .into(ivEventImage)

            // Tombol untuk membuka link acara
            btnOpenLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(intent)
            }
        }
    }

    private fun checkIfFavorite(eventId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = favoriteUserDao.checkUserFavorite(eventId)
            isFavorite = count > 0
            withContext(Dispatchers.Main) {
                setFavoriteIcon(isFavorite)
            }
        }
    }

    private fun toggleFavorite(event: ListEventsItem) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isFavorite) {
                favoriteUserDao.removeFromFavorite(event.id)
            } else {
                favoriteUserDao.addToFavorite(
                    FavoriteUser(
                        login = event.name,
                        avatar_url = event.mediaCover,
                        description = event.description,
                        eventLocation = event.cityName,
                        beginTime = event.beginTime,
                        endTime = event.endTime,
                        quota = event.quota,
                        registrants = event.registrants,
                        id = event.id
                    )
                )
            }
            isFavorite = !isFavorite
            withContext(Dispatchers.Main) {
                setFavoriteIcon(isFavorite)
            }
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        val favoriteIcon = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_off
        binding.ivFavorite.setImageResource(favoriteIcon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
