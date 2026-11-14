package com.example.papb5

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.papb5.data.local.DatabaseProvider
import com.example.papb5.data.network.RetrofitClient
import com.example.papb5.data.preferences.UserPreferencesRepository
import com.example.papb5.data.repository.DefaultStudentRepository
import com.example.papb5.data.repository.FavoriteRepository
import com.example.papb5.data.repository.StudentRepository
import com.example.papb5.data.source.StudentRemoteDataSource
import com.example.papb5.ui.detail.DetailViewModel
import com.example.papb5.ui.favorites.FavoritesViewModel
import com.example.papb5.ui.home.HomeViewModel
import com.example.papb5.ui.screen.AboutScreen
import com.example.papb5.ui.screen.DetailScreen
import com.example.papb5.ui.screen.FavoritesScreen
import com.example.papb5.ui.screen.HomeScreen
import com.example.papb5.ui.screen.ProfileScreen

@Composable
fun NavigationLabApp() {
    val context = LocalContext.current.applicationContext

    val remoteDataSource = remember { StudentRemoteDataSource(RetrofitClient.apiService) }
    val repository: StudentRepository = remember { DefaultStudentRepository(remoteDataSource) }

    val preferencesRepository = remember {
        UserPreferencesRepository(context)
    }

    val favoriteRepository = remember {
        FavoriteRepository(
            DatabaseProvider.getDatabase(context).favoriteStudentDao()
        )
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.factory(repository, preferencesRepository)
            )
            val homeUiState by homeViewModel.uiState.collectAsState()

            HomeScreen(
                uiState = homeUiState,
                onSearchQueryChange = homeViewModel::onSearchQueryChange,
                onSearchSubmit = homeViewModel::searchStudents,
                onRefresh = homeViewModel::refresh,
                onToggleSortOrder = homeViewModel::toggleSortOrder,
                onOpenFavorites = {
                    navController.navigate(Routes.FAVORITES)
                },
                onOpenDetail = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onOpenAbout = {
                    navController.navigate(Routes.ABOUT)
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1

            val detailViewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )
            val detailUiState by detailViewModel.uiState.collectAsState()

            LaunchedEffect(studentId) {
                detailViewModel.loadStudent(studentId)
            }

            DetailScreen(
                uiState = detailUiState,
                onToggleFavorite = detailViewModel::toggleFavorite,
                onBack = { navController.popBackStack() },
                onOpenProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(favoriteRepository)
            )
            val favoritesUiState by favoritesViewModel.uiState.collectAsState()

            FavoritesScreen(
                uiState = favoritesUiState,
                onOpenDetail = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
