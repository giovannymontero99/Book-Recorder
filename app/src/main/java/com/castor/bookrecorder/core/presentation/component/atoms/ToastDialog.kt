package com.castor.bookrecorder.core.presentation.component.atoms

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource


@Composable
fun ToastDialog(text: Int, duration: Int) {
    val context = LocalContext.current
    Toast.makeText(
        context,
        stringResource(text),
        duration
    ).show()
}