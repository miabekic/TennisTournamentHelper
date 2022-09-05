package com.example.tennistournamenthelper

import android.app.AlertDialog
import android.content.Context
import com.example.tennistournamenthelper.R

class DialogFactory {

    fun createNoSuchItemDialog(context: Context, executionFunction: () -> (Unit)): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("")
            .setCancelable(false)
            .setPositiveButton("Ok")
            { _, _ ->
                executionFunction()
            }
        return builder.create()
    }

    fun createDeleteDialog(context: Context, executionFunction: () -> Unit): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.confirm_delete))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.yes))
            { _, _ ->
                executionFunction()
            }
            .setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }

    fun createChoseMatchCreatingTypeDialog(
        context: Context,
        positive: () -> Unit,
        negative: () -> (Unit)
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.is_match_related_to_existing_tournament))
            .setCancelable(true)
            .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                positive()
            }
            .setNegativeButton(context.getString(R.string.no)) { _, _ ->
                negative()
            }
        return builder.create()
    }

}