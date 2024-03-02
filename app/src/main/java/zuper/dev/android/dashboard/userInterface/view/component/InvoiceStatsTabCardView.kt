package zuper.dev.android.dashboard.userInterface.theme.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.AppUtils.Utility.calculateInvoicePercentagesWithColors
import zuper.dev.android.dashboard.AppUtils.Utility.formatTotalCollected
import zuper.dev.android.dashboard.AppUtils.Utility.formatTotalValue
import zuper.dev.android.dashboard.data.model.InvoiceStatus
import zuper.dev.android.dashboard.userInterface.theme.LightCyan
import zuper.dev.android.dashboard.userInterface.theme.LightGreen
import zuper.dev.android.dashboard.userInterface.theme.LightGrey
import zuper.dev.android.dashboard.userInterface.theme.LightRed
import zuper.dev.android.dashboard.userInterface.theme.TextDarkGrey
import zuper.dev.android.dashboard.userInterface.theme.TitleBlack
import zuper.dev.android.dashboard.userInterface.theme.LightYellow
import zuper.dev.android.dashboard.userInterface.theme.viewModels.MainScreenViewModel

@Composable
fun InvoiceStatsCard(mainScreenViewModel: MainScreenViewModel = hiltViewModel()) {
    val invoices by mainScreenViewModel.invoicesState.observeAsState()

    val percentagesState = remember { mutableStateOf<Map<Color, Double>>(emptyMap()) }

    LaunchedEffect(key1 = invoices) {
        val calculatedPercentages = withContext(Dispatchers.Default) {
            calculateInvoicePercentagesWithColors(invoices)
        }
        percentagesState.value = calculatedPercentages
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(0.8.dp, Color.LightGray, RoundedCornerShape(3)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        ) {
            val (
                title, divider, total, collected, chart, draft,
                pending, paid, badDebt,
            ) = createRefs()

            Text(
                text = stringResource(R.string.invoice_stats),
                style = MaterialTheme.typography.titleMedium.copy(TitleBlack),
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .constrainAs(divider) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                thickness = 0.8.dp,
                color = LightGrey
            )

            Text(
                text = stringResource(R.string.total_value, formatTotalValue(invoices)),
                style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                modifier = Modifier
                    .constrainAs(total) {
                        start.linkTo(parent.start)
                        top.linkTo(divider.bottom)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(
                text = stringResource(R.string.collected, formatTotalCollected(invoices)),
                style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                modifier = Modifier
                    .constrainAs(collected) {
                        end.linkTo(parent.end)
                        top.linkTo(divider.bottom)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .constrainAs(chart) {
                        end.linkTo(parent.end)
                        top.linkTo(total.bottom)
                    },
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (percentagesState.value.values.sum() > 0.0)
                        percentagesState.value.forEach { (color, percentage) ->
                            Box(
                                modifier = Modifier
                                    .height(22.dp)
                                    .weight(maxOf(0.01f, percentage.toFloat())),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                )
                            }
                        }
                }
            }

            createHorizontalChain(draft, pending, chainStyle = ChainStyle.Packed)
            Row(
                modifier = Modifier
                    .constrainAs(draft) {
                        top.linkTo(chart.bottom)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LightYellow, RoundedCornerShape(2.dp))
                )
                Text(
                    text = stringResource(
                        R.string.draft,
                        invoices?.filter { it.status == InvoiceStatus.Draft }?.sumOf { it.total }
                            ?: 0),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .constrainAs(pending) {
                        top.linkTo(chart.bottom)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LightCyan, RoundedCornerShape(2.dp))
                )
                Text(
                    text = stringResource(
                        R.string.pending,
                        invoices?.filter { it.status == InvoiceStatus.Pending }?.sumOf { it.total }
                            ?: 0),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            createHorizontalChain(paid, badDebt, chainStyle = ChainStyle.Packed)
            Row(
                modifier = Modifier
                    .constrainAs(paid) {
                        top.linkTo(draft.bottom)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LightGreen, RoundedCornerShape(2.dp))
                )
                Text(
                    text = stringResource(
                        R.string.paid,
                        invoices?.filter { it.status == InvoiceStatus.Paid }?.sumOf { it.total }
                            ?: 0),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .constrainAs(badDebt) {
                        top.linkTo(draft.bottom)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LightRed, RoundedCornerShape(2.dp))
                )
                Text(
                    text = stringResource(
                        R.string.bad_debt,
                        invoices?.filter { it.status == InvoiceStatus.BadDebt }?.sumOf { it.total }
                            ?: 0),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}



