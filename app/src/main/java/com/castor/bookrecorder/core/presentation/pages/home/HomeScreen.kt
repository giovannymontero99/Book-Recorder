package com.castor.bookrecorder.core.presentation.pages.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.presentation.navigation.BooksListRoute
import com.castor.bookrecorder.core.presentation.navigation.HomeRoute
import com.castor.bookrecorder.core.presentation.navigation.LoginRoute
import com.castor.bookrecorder.core.presentation.navigation.TestRoute
import com.castor.bookrecorder.core.presentation.pages.bookslist.BooksListScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
)

data class ItemNavigationMenu(
    val label: String,
    val icon: ImageVector
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToBookDetail: (String, String) -> Unit,
    onNavigateToAddBook: () -> Unit,
    onNavigateToEditBook: (String) -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val booksList by viewModel.booksList.collectAsState()
    val onClick = viewModel::onClick
    val auth = Firebase.auth

    // App Bottom Bar handler
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf(
        NavItem(
            "Home",
            Icons.Filled.Home, Icons.Outlined.Home,
            BooksListRoute
        ),
        NavItem("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, TestRoute),
        NavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, TestRoute)
    )

    // Modal bottom Sheet handler
    val sheetState = rememberModalBottomSheetState()
    var showModal by remember { mutableStateOf(false) }

    // Delete alert handler
    var showDeleteAlert by remember { mutableStateOf(false) }
    var idItemSelected by remember { mutableStateOf<String?>(null) }




    // Navigation State
    val navController = rememberNavController()

    /*
    if(showDeleteAlert){
        AlertDialog(
            onDismissRequest = { showDeleteAlert = false },
            title = { Text(stringResource(R.string.delete_character)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_this_character)) },
            confirmButton = {
                Button(onClick = {
                    onClick(HomeEvent.DeleteBook(idItemSelected!!))
                    showDeleteAlert = false
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteAlert = false
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            textContentColor = MaterialTheme.colorScheme.onBackground,
        )
    }

     */

    /*
    if(showModal){
        ModalBottomSheet(
            onDismissRequest = { showModal = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            ListItem(
                modifier = Modifier
                    .clickable{
                        showDeleteAlert = true
                        showModal = false
                    },
                headlineContent = {
                    Text(stringResource(R.string.delete))
                },
                leadingContent = {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    headlineColor = MaterialTheme.colorScheme.onBackground,
                    leadingIconColor = MaterialTheme.colorScheme.onBackground
                )
            )

            ListItem(
                modifier = Modifier
                    .clickable{
                        onNavigateToEditBook(idItemSelected!!)
                        showModal = false
                    },
                headlineContent = {
                    Text(text = stringResource(R.string.edit))
                },
                leadingContent = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    headlineColor = MaterialTheme.colorScheme.onBackground,
                    leadingIconColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }

     */

    // Navigation state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val itemsNavigationMenu = listOf(
        ItemNavigationMenu(
            label = "Profile",
            icon = Icons.Default.Person
        ),
        ItemNavigationMenu(
            label = "Settings",
            icon = Icons.Default.Settings
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp)
            ) {
                itemsNavigationMenu.forEachIndexed { index, menu ->
                    NavigationDrawerItem(
                        label = { Text(menu.label) },
                        selected = false,
                        onClick = {
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
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
                        onMenuClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
                composable<TestRoute> {
                    Text("Test")
                }
            }

        }

        /*
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { onNavigateToAddBook() }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Book")
                }
            },
            topBar = {
                TopAppBar(
                    actions = {
                        if(auth.currentUser?.photoUrl == null){
                            Image(
                                painter = painterResource(R.drawable.userphoto_desnt_exist),
                                contentDescription = "User profile picture",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .clickable { onNavigateToAccount() }
                            )
                        }else{
                            AsyncImage(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .clickable { onNavigateToAccount() },
                                model =  auth.currentUser?.photoUrl,
                                contentDescription = "User profile picture"
                            )
                        }
                    },

                    title = {
                        Text(text = stringResource(R.string.library), color = MaterialTheme.colorScheme.background)
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
                    contentColor = MaterialTheme.colorScheme.onBackground
                ){
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
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
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(booksList){ item ->
                    BookTitleItem(
                        title = item.title,
                        author = item.author,
                        onClick = {
                            onNavigateToBookDetail(item.id, item.title)
                        },
                        onShowModalOptions = {
                            showModal = true
                            idItemSelected = item.id
                        }
                    )
                }
            }
        }

         */
    }
}

