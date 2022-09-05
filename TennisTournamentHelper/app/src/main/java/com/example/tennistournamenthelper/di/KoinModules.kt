package com.example.tennistournamenthelper.di

import com.example.tennistournamenthelper.data.*
import com.example.tennistournamenthelper.presentation.*
import com.example.tennistournamenthelper.presentation.matches.*
import com.example.tennistournamenthelper.presentation.players.NewPlayerViewModel
import com.example.tennistournamenthelper.presentation.players.PlayerListViewModel
import com.example.tennistournamenthelper.presentation.tournaments.*
import com.example.tennistournamenthelper.DialogFactory
import com.example.tennistournamenthelper.ui.MapShowLocationHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuthManager() }
    single { Database() }
    factory { MapShowLocationHelper() }
    single { DialogFactory() }
}

val repositoryModule = module {
    single<TournamentRepository> { TournamentRepositoryImpl(get()) }
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    single<PlayerRepository> { PlayerRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { RegistrationViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { FinishedTournamentListViewModel(get(), get(), get()) }
    viewModel { NewTournamentViewModel(get()) }
    viewModel { CurrentTournamentListViewModel(get()) }
    viewModel { CurrentTournamentDetailsViewModel(get()) }
    viewModel { NewPlayerViewModel(get(), get()) }
    viewModel { TrailMatchListViewModel(get()) }
    viewModel { TrailViewModel(get()) }
    viewModel { PlayerListViewModel(get()) }
    viewModel { FinishedMatchesListViewModel(get()) }
    viewModel { FinishedMatchDetailsViewModel(get()) }
    viewModel { NewMatchTournamentNoRelatedViewModel(get()) }
    viewModel { NewMatchTournamentRelatedViewModel(get(), get(), get()) }
    viewModel { FinishedTournamentDetailsViewModel(get()) }
}
