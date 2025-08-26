package com.castor.bookrecorder.core.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.castor.bookrecorder.core.presentation.pages.account.AccountScreen
import com.castor.bookrecorder.core.presentation.pages.add_book.AddBookScreen
import com.castor.bookrecorder.core.presentation.pages.book_detail.BookDetailScreen
import com.castor.bookrecorder.core.presentation.pages.home.HomeScreen
import com.castor.bookrecorder.core.presentation.pages.login.LoginScreen



@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    viewModel: NavigationWrapperViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isLoggedState by viewModel.isLoggedState.collectAsState()

    isLoggedState?.let { isLogged ->

        NavHost(navController = navController, startDestination = if(isLogged) HomeRoute else LoginRoute ){

            composable<HomeRoute> {
                HomeScreen(
                    onNavigateToAddBook = {
                        navController.navigate(AddBookRoute)
                    },
                    onNavigateToBookDetail = { id, title ->
                        /*
                        navController.navigate(BookDetailRoute(id, title))
                        
                         */
                    },
                    onNavigateToEditBook = { id ->
                        navController.navigate(EditBookRoute(id))
                    },
                    onNavigateToAccount = {
                        navController.navigate(AccountRoute)
                    }
                )
            }

            composable<LoginRoute> {
                LoginScreen(
                    navigateToHome = {
                        navController.navigate(HomeRoute){
                            launchSingleTop = true
                            popUpTo(LoginRoute){
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<AddBookRoute> {
                AddBookScreen(
                    onNavigateToHome = {
                        navController.navigate(HomeRoute){
                            launchSingleTop = true
                            popUpTo(HomeRoute){
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<EditBookRoute> { navBackStackEntry ->
                val homeArgs = navBackStackEntry.toRoute<EditBookRoute>()
                val id = homeArgs.id

                AddBookScreen(
                    id = id,
                    onNavigateToHome = {
                        navController.navigate(HomeRoute){
                            launchSingleTop = true
                            popUpTo(HomeRoute){
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<BookDetailRoute> { navBackStackEntry ->
                val homeArgs = navBackStackEntry.toRoute<BookDetailRoute>()
                val id = homeArgs.id
                val title = homeArgs.title

                BookDetailScreen(
                    id = id,
                    title = title
                )
            }

            composable<AccountRoute> {
                AccountScreen(
                    onNavigateToLogin = {
                        navController.navigate(LoginRoute){
                            launchSingleTop = true
                            popUpTo(HomeRoute){
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }

    }


}