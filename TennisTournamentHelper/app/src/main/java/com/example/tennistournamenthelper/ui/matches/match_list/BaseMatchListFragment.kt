package com.example.tennistournamenthelper.ui.matches.match_list

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.example.tennistournamenthelper.model.Match
import com.example.tennistournamenthelper.presentation.BaseListViewModel
import com.example.tennistournamenthelper.DialogFactory
import com.example.tennistournamenthelper.ui.OnItemEventListener
import com.example.tennistournamenthelper.ui.matches.new_match.MatchAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject


abstract class BaseMatchListFragment : Fragment(), OnItemEventListener {
    protected lateinit var adapter: MatchAdapter
    protected abstract val viewModel: BaseListViewModel<Match>
    private val dialogFactory: DialogFactory by inject()

    protected open fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = MatchAdapter()
        adapter.onMatchEventListener = this
        recyclerView.adapter = adapter
    }

    protected open fun setOnClickListenerToAddBtn(floatingActionButton: FloatingActionButton) {
        floatingActionButton.setOnClickListener {
            dialogFactory.createChoseMatchCreatingTypeDialog(
                context!!,
                { navigateToNewMatchTournamentRelated() },
                { navigateToNewMatchTournamentNoRelated() }).show()
        }
    }

    protected open fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        viewModel.listUiState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultStatus.Success -> onSuccessfulDataArrival(result.data)
                is ResultStatus.Failure -> Toast.makeText(
                    context,
                    result.message,
                    Toast.LENGTH_LONG
                ).show()
                is ResultStatus.Loading -> {}
            }
        }
    }

    override fun onItemSelected(id: String) {
        navigateToDetails(id)
    }

    override fun onItemLongPress(id: String): Boolean {
        dialogFactory.createDeleteDialog(context!!) {
            viewModel.delete(id)
        }.show()
        return true
    }

    protected fun observeDeleteState() {
        viewModel.deleteState.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.resetDeleteState()
                viewModel.getContent()
            } else if (it == false) {
                Toast.makeText(context, getString(R.string.deletation_faild), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    protected abstract fun onSuccessfulDataArrival(matches: List<Match>?)
    protected abstract fun navigateToDetails(matchId: String)
    protected abstract fun navigateToNewMatchTournamentNoRelated()
    protected abstract fun navigateToNewMatchTournamentRelated()
}