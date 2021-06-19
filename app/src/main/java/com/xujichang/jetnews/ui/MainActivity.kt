package com.xujichang.jetnews.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xujichang.jetnews.res.JetNewsTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JetNewsApp()
        }
    }

}

@Composable
private fun JetNewsApp() {
    JetNewsTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()

            val scaffoldState = rememberScaffoldState()

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE

            Scaffold(
                scaffoldState = scaffoldState,
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigationToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                        navigationToInterests = { navController.navigate(MainDestinations.INTERESTS_ROUTE) },
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } }
                    )
                }
            ) {
                JetNewsNavGraph()
            }

        }
    }
}
