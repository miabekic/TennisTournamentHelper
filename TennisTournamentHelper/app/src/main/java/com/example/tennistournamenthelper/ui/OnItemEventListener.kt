package com.example.tennistournamenthelper.ui

interface OnItemEventListener {
    fun onItemSelected(id: String)
    fun onItemLongPress(id: String): Boolean
}