package zuper.dev.android.dashboard.userInterface.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.userInterface.theme.LightLowBlue
import zuper.dev.android.dashboard.userInterface.theme.TitleBlack
import zuper.dev.android.dashboard.userInterface.theme.view.component.InvoiceStatsCard
import zuper.dev.android.dashboard.userInterface.theme.view.component.WelcomeTab
import zuper.dev.android.dashboard.userInterface.theme.view.component.JobStatsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navHostController: NavHostController) {
    Column() {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            title = {
                Text(
                    text = stringResource(R.string.dashboard),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(TitleBlack),

                )
            },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalDivider(
                thickness = 2.dp,
                color = LightLowBlue
            )

            WelcomeTab()
            JobStatsCard(navHostController)
            InvoiceStatsCard()
        }
    }
}
