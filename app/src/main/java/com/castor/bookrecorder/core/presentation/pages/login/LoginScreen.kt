package com.castor.bookrecorder.core.presentation.pages.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.castor.bookrecorder.core.presentation.pages.login.components.loginForm.SignInForm
import com.castor.bookrecorder.core.presentation.pages.login.components.signupForm.SignUpForm

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit
) {

    var isSignUp by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            if (isSignUp) {
                SignUpForm(
                    onNavigateToSignUp = { isSignUp = it },
                    navigateToHome = {
                        navigateToHome()
                    }
                )
            } else {
                SignInForm(
                    onNavigateToSignUp = {
                        isSignUp = it
                    },
                    navigateToHome = {
                        navigateToHome()
                    }
                )
            }
        }
    }
}