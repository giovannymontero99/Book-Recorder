package com.castor.bookrecorder.core.presentation.pages.home

import com.castor.bookrecorder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.castor.bookrecorder.core.presentation.navigation.BooksListRoute
import com.castor.bookrecorder.core.presentation.navigation.FavoritesRoute
import com.castor.bookrecorder.core.presentation.navigation.MemoryBoxRoute
import com.castor.bookrecorder.core.presentation.pages.bookslist.BooksListScreen
import com.castor.bookrecorder.core.presentation.pages.favorites.FavoritesScreen
import kotlinx.coroutines.launch

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBookDetail: (String, String) -> Unit,
    onNavigateToAddBook: () -> Unit,
    onNavigateToEditBook: (String) -> Unit,
    onNavigateToAccount: () -> Unit
) {

    // App Bottom Bar handler
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf(
        NavItem(
            stringResource(R.string.home),
            Icons.Filled.Home, Icons.Outlined.Home,
            BooksListRoute
        ),
        NavItem(stringResource(R.string.favorites), Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, FavoritesRoute),
        NavItem(stringResource(R.string.memory_box), Icons.Filled.Search, Icons.Outlined.Search,
            MemoryBoxRoute
        )
    )

    // Navigation State
    val navController = rememberNavController()

    // Navigation state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color(0xFFE8E8E8),
            ) {
                Box(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(150.dp)
                ){
                }
                Box(
                   modifier = Modifier
                       .fillMaxSize()
                       .background(Color.White)
                       .weight(1f)

                ){
                    Column(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        NavigationDrawerItem(
                            label = { Text("Profile") },
                            selected = false,
                            onClick = {
                                onNavigateToAccount()
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedTextColor = Color.Black,
                                unselectedTextColor = Color.Black,
                                unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            icon = { Icon(Icons.Default.Person, contentDescription = null) }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Developed by Giovanny Montero", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                if(selectedItemIndex == 0){
                    FloatingActionButton(onClick = { onNavigateToAddBook() }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Book")
                    }
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(items[selectedItemIndex].label, color = MaterialTheme.colorScheme.background)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = MaterialTheme.colorScheme.background)
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFFDFDFE3)
                        ),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ){
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.route)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            }
                        )
                    }
                }
            }
        ){ innerPadding ->

            NavHost(modifier = Modifier.padding(innerPadding),navController = navController, startDestination = BooksListRoute){
                composable<BooksListRoute>{
                    BooksListScreen(
                        onNavigateToEditBook = onNavigateToEditBook,
                        onNavigateToBookDetail = onNavigateToBookDetail
                    )
                }
                composable<FavoritesRoute> {
                    FavoritesScreen()
                }

                composable<MemoryBoxRoute> {

                }
            }
        }
    }
}

