package com.example.tennistournamenthelper.ui.tournaments.tournament_list

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Tournament
import com.example.tennistournamenthelper.presentation.BaseListViewModel
import com.example.tennistournamenthelper.DialogFactory
import com.example.tennistournamenthelper.ui.OnItemEventListener
import org.koin.android.ext.android.inject

abstract class BaseTournamentListFragment : Fragment(), OnItemEventListener {
    protected lateinit var adapter: TournamentAdapter
    protected abstract val viewModel: BaseListViewModel<Tournament>
    protected val dialogFactory: DialogFactory by inject()

    protected open fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = TournamentAdapter()
        adapter.onTournamentSelectedListener = this
        recyclerView.adapter = adapter
    }

    protected open fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!isLayoutRefreshing()) {
                    adapter.filter(newText)
                }
                return false
            }

        })
    }

    protected abstract fun isLayoutRefreshing(): Boolean
    protected fun observeListUiState() {
        viewModel.listUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultStatus.Success -> {
                    onSuccessfulDataArrival(it.data)
                }
                is ResultStatus.Failure -> Toast.makeText(context, it.message, Toast.LENGTH_LONG)
                    .show()
                is ResultStatus.Loading -> {}
            }
        }
    }

    protected fun observeDeleteState() {
        viewModel.deleteState.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.resetDeleteState()
                viewModel.getContent()
            } else if (it == false) {
                viewModel.resetDeleteState()
                Toast.makeText(context, getString(R.string.deletation_faild), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    protected abstract fun navigateToDetails(tournamentId: String)
    protected abstract fun onSuccessfulDataArrival(tournaments: List<Tournament>?)

    override fun onItemSelected(id: String) {
        navigateToDetails(id)
    }

    override fun onItemLongPress(id: String): Boolean {
        dialogFactory.createDeleteDialog(context!!) {
            viewModel.delete(id)
        }.show()
        return true
    }
}