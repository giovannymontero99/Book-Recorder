package com.castor.bookrecorder.core.presentation.pages.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.core.presentation.state.NavigationState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {

    val loginResult by viewModel.loginResult.collectAsState()
    val context = LocalContext.current
    val navigationState by viewModel.navigationState.collectAsState()

    when(navigationState){
        is NavigationState.NavigateToHome -> {
            navigateToHome()
        }
        else -> {}
    }

    when(loginResult){
        AuthResult.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        is AuthResult.Success<Boolean> -> {

        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.signInWithGoogle(
                            context
                        )
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Continue with Google", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                }
            }
        }
    }





}