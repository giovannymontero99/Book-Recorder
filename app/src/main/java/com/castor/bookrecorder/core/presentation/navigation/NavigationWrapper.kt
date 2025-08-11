package com.castor.bookrecorder.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.castor.bookrecorder.core.presentation.pages.add_book.AddBookScreen
import com.castor.bookrecorder.core.presentation.pages.book_detail.BookDetailScreen
import com.castor.bookrecorder.core.presentation.pages.home.HomeScreen

@Composable
fun NavigationWrapper(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute ){
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToAddBook = {
                    navController.navigate(AddBookRoute)
                },
                onNavigateToBookDetail = { id, title ->
                    navController.navigate(BookDetailRoute(id, title))
                },
                onNavigateToEditBook = { id ->
                    navController.navigate(EditBookRoute(id))
                }
            )
        }

        composable<AddBookRoute> {
            AddBookScreen(
                onNavigateToHome = {
                    navController.navigate(HomeRoute)
                }
            )
        }

        composable<EditBookRoute> { navBackStackEntry ->
            val homeArgs = navBackStackEntry.toRoute<EditBookRoute>()
            val id = homeArgs.id

            AddBookScreen(
                id = id,
                onNavigateToHome = {
                    navController.navigate(HomeRoute)
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
    }
}