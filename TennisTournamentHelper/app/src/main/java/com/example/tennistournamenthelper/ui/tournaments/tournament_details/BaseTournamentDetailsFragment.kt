package com.example.tennistournamenthelper.ui.tournaments.tournament_details

import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.viewbinding.ViewBinding
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Location
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.BaseDetailsViewModel
import com.example.tennistournamenthelper.DialogFactory
import com.example.tennistournamenthelper.ui.MapShowLocationHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class BaseTournamentDetailsFragment<Binding : ViewBinding> : Fragment() {
    protected abstract var binding: Binding
    protected abstract val viewModel: BaseDetailsViewModel<Tournament>
    protected abstract val args: NavArgs
    private var map: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private val showLocationHelper: MapShowLocationHelper by inject()
    protected val dialogFactory: DialogFactory by inject()

    protected open fun buildDetails(tournament: Tournament?): String? {
        val build = StringBuilder()
        tournament?.let {
            build.append("Name: ${it.name}\nDate: ${it.startDate} - ${it.endDate}\nPlayers: ${it.allowedPlayersGender}\nSurface: ${it.surface}\nPlace of helding: ${it.place.name}\n\nMatches rules:")
            for (item in it.games) {
                build.append("\n${item.type}: ${item.sets} sets, ${item.tiebreak}")
            }
        } ?: return null
        return build.toString()
    }

    protected fun setMap(location: Location, place: String) {
        if (location.latitude != null) {
            lifecycleScope.launch(Dispatchers.Main) {
                map = mapFragment?.awaitMap()
                showLocationHelper.showLocation(map, location, place)
                setMapFragmentVisibility(View.VISIBLE)
            }
        }
    }

    protected fun setMapFragmentVisibility(visibility: Int) {
        mapFragment?.view?.visibility = visibility
    }

    protected fun addClickListenerOnDisplayPlayersButton(button: Button) {
        button.setOnClickListener { navigateToPlayersList() }
    }

    protected fun findMapFragmentView(id: Int) {
        mapFragment = childFragmentManager.findFragmentById(id) as SupportMapFragment?
    }

    protected fun observeDetailsUiState() {
        viewModel.detailsUiState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultStatus.Success -> {
                    updateUiAfterDataArrival(result.data)
                }
                is ResultStatus.Failure -> {
                    Toast.makeText(context, result.message.toString(), Toast.LENGTH_LONG).show()
                }
                is ResultStatus.Loading -> {
                    displayDetails("")
                    Toast.makeText(context, getString(R.string.loading_details), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    protected open fun updateUiAfterDataArrival(tournament: Tournament?) {
        buildDetails(tournament)?.let { details ->
            displayDetails(details)
            setMap(tournament!!.place.location, tournament.place.name)
        } ?: dialogFactory.createNoSuchItemDialog(context!!) {
            navigateBack()
        }.show()
    }

    abstract fun navigateToPlayersList()
    abstract fun navigateBack()
    abstract fun displayDetails(details: String)
}
