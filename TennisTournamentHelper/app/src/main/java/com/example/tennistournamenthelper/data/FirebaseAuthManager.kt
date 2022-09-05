package com.example.tennistournamenthelper.data

import android.content.res.Resources
import com.example.tennistournamenthelper.R
import com.example.tennistournamenthelper.ResultStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser
    private var unverifiedUser: FirebaseUser? = null

    suspend fun login(email: String, password: String): ResultStatus<String> =
        withContext(Dispatchers.IO) {
            try {
                val data = auth.signInWithEmailAndPassword(email, password).await()
                currentUser = data.user
                if (currentUser?.isEmailVerified != true) {
                    unverifiedUser = currentUser
                    logOut()
                    ResultStatus.Failure(
                        "You did not verify your email. Did not get verification email? Click here to send verification email again."
                    )
                } else {
                    unverifiedUser = null
                    ResultStatus.Success(currentUser!!.uid)
                }
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    fun logOut() {
        auth.signOut()
        currentUser = null
    }

    suspend fun register(email: String, password: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val data = auth.createUserWithEmailAndPassword(email, password).await()
                data.user!!.sendEmailVerification().await()
                logOut()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    fun isCurrentUserLogged(): Boolean {
        return currentUser != null
    }


    suspend fun sendResetPasswordEmail(email: String): ResultStatus<Unit> =
        withContext(Dispatchers.IO) {
            try {
                auth.sendPasswordResetEmail(email).await()
                ResultStatus.Success(Unit)
            } catch (e: Exception) {
                ResultStatus.Failure(e.message.toString())
            }
        }

    suspend fun sendVerificationEmail() = withContext(Dispatchers.IO) {
        unverifiedUser?.sendEmailVerification()?.await()
    }

    fun getUserUid(): String? = currentUser?.uid
    fun getUserEmail(): String? = currentUser?.email
}
