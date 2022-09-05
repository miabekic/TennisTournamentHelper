package com.example.tennistournamenthelper.ui.tournaments.new_tournament

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.databinding.FragmentNewTournamentPlaceBinding
import com.example.tennistournamenthelper.model.Location
import com.example.tennistournamenthelper.presentation.tournaments.NewTournamentViewModel
import com.example.tennistournamenthelper.ui.MapShowLocationHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class NewTournamentPlaceFragment : Fragment() {
    private lateinit var binding: FragmentNewTournamentPlaceBinding
    private var map: GoogleMap? = null
    private val viewModel: NewTournamentViewModel by sharedViewModel()
    private var mapFragment: SupportMapFragment? = null
    private val showLocationHelper: MapShowLocationHelper by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val geocoder = Geocoder(context, Locale.getDefault())
        binding = FragmentNewTournamentPlaceBinding.inflate(layoutInflater)
        var loc = Location()
        var place: String? = null
        var marker: Marker? = null
        var address: String? = null
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map_new_tournament_place) as SupportMapFragment?
        mapFragment?.view?.visibility = View.GONE
        binding.apply {
            bPreviewLocation.setOnClickListener {
                if (etPlace.text.isNotEmpty()) {
                    place = etPlace.text.toString()
                    lifecycleScope.launch {
                        if (map == null) {
                            map = mapFragment?.awaitMap()
                        }
                        when (val result = viewModel.getLocation(geocoder, place!!)) {
                            is ResultStatus.Success -> {
                                showLocationHelper.marker?.remove()
                                address = result.data?.getAddressLine(0)
                                tvPlace.text = "Place: $address"
                                loc = Location(result.data!!.latitude, result.data.longitude)
                                showLocationHelper.showLocation(map, loc, address)
                                mapFragment?.view?.visibility = View.VISIBLE
                            }
                            is ResultStatus.Failure -> {
                                Toast.makeText(
                                    context,
                                    result.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                                place = ""
                            }
                            is ResultStatus.Loading -> {
                                if (marker != null) {
                                    marker.remove()
                                }
                            }
                        }
                    }
                } else Toast.makeText(context, R.string.missing_place_of_helding, Toast.LENGTH_LONG)
                    .show()
            }

            bCreateTournament.setOnClickListener {
                if (etPlace.text.isNotEmpty()) {
                    if (place == etPlace.text.toString()) {
                        viewModel.setPlace(address!!, loc)
                        viewModel.saveTournament()
                    } else {
                        viewModel.setPlace(etPlace.text.toString(), loc)
                        viewModel.saveTournament()
                    }
                } else Toast.makeText(
                    context,
                    getString(R.string.missing_place_of_helding),
                    Toast.LENGTH_LONG
                ).show()
            }
            viewModel.createNewTournamentUiState.observe(viewLifecycleOwner) {
                when (it) {
                    is ResultStatus.Loading -> enableButtons(false)
                    is ResultStatus.Success -> {
                        viewModel.reset()
                        val action =
                            NewTournamentPlaceFragmentDirections.actionNewTournamentMapsFragmentToNavCurrentTournaments()
                        findNavController().navigate(action)
                    }
                    is ResultStatus.Failure -> {
                        Toast.makeText(
                            context,
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        enableButtons(true)
                    }
                }
            }
        }
        return binding.root
    }

    private fun enableButtons(enable: Boolean) {
        binding.bCreateTournament.isEnabled = enable
        binding.bPreviewLocation.isEnabled = enable
    }

}