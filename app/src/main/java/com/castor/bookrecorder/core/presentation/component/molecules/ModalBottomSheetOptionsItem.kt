package com.castor.bookrecorder.core.presentation.component.molecules

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ModalBottomSheetOptionsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    icon: @Composable () -> Unit
) {
    ListItem(
        modifier = modifier
            .clickable{ onClick() },
        headlineContent = {
            Text(title)
        },
        leadingContent = { icon() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            leadingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}