package com.example.tennistournamenthelper

sealed class ResultStatus<out T>(val data: T? = null, val message: String? = null) {
    class Success<out T>(data: T?) : ResultStatus<T>(data)
    class Loading : ResultStatus<Nothing>()
    class Failure<out T>(message: String?, data: T? = null) : ResultStatus<T>(data, message)
}
