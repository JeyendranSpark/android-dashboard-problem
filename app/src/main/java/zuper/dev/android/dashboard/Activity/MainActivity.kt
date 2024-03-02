package zuper.dev.android.dashboard.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.userInterface.theme.AppTheme
import zuper.dev.android.dashboard.userInterface.theme.screens.MainScreen
import zuper.dev.android.dashboard.userInterface.theme.screens.JobListShowScreen

@Preview
@Composable
fun PreviewMainActivity() {
    MainActivity()
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            AppTheme {

                NavHost(
                    navController = navHostController,
                    startDestination = getString(R.string.main)
                ) {
                    composable(getString(R.string.main)) {
                        MainScreen(navHostController)
                    }
                    composable(getString(R.string.job_list_show_screen)) {
                        JobListShowScreen(navHostController)
                    }
                }
            }
        }
    }

}
