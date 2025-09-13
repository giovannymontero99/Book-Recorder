package com.castor.bookrecorder.core.presentation.pages.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.presentation.state.NavigationState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {

    // Handle navigation
    val navigationState by viewModel.navigationState.collectAsState()
    val auth = Firebase.auth

    val accountHandler = viewModel::accountHandler


    LaunchedEffect(navigationState) {
        when(navigationState){
            NavigationState.NavigateToHome -> {}
            NavigationState.NavigateToProfile -> {}
            null -> {}
            NavigationState.NavigateToLogin -> {
                onNavigateToLogin()
            }
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(auth.currentUser?.photoUrl == null){
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    painter = painterResource(R.drawable.userphoto_desnt_exist),
                    contentDescription = "User profile picture",
                    contentScale = ContentScale.Crop
                )
            }else{
                AsyncImage(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    model = auth.currentUser?.photoUrl,
                    contentDescription = "User profile picture",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            TextButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    accountHandler(AccountEvent.SyncData)
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = stringResource(R.string.sync_data),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(stringResource(R.string.sync_data))
                }

            }



            Spacer(modifier = Modifier.height(8.dp))

            auth.currentUser?.displayName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            auth.currentUser?.email?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                onClick = {
                    accountHandler(AccountEvent.SignOut)
                }
            ) {
                Text(text = "Sign out", color = MaterialTheme.colorScheme.error, fontSize = 16.sp)
            }
        }
    }
}