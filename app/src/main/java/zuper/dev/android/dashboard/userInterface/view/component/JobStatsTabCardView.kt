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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.AppUtils.Utility.calculateJobPercentagesWithColors
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.userInterface.theme.LightBlue
import zuper.dev.android.dashboard.userInterface.theme.LightCyan
import zuper.dev.android.dashboard.userInterface.theme.LightGreen
import zuper.dev.android.dashboard.userInterface.theme.LightGrey
import zuper.dev.android.dashboard.userInterface.theme.LightRed
import zuper.dev.android.dashboard.userInterface.theme.TextDarkGrey
import zuper.dev.android.dashboard.userInterface.theme.TitleBlack
import zuper.dev.android.dashboard.userInterface.theme.LightYellow
import zuper.dev.android.dashboard.userInterface.theme.viewModels.MainScreenViewModel

@Composable
fun JobStatsCard(
    navHostController: NavHostController,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
) {
    val jobs by mainScreenViewModel.jobsState.observeAsState()
    val percentagesState = remember { mutableStateOf<Map<Color, Double>>(emptyMap()) }
    val context = LocalContext.current
    LaunchedEffect(jobs) {
        val percentages = withContext(Dispatchers.Default) {
            calculateJobPercentagesWithColors(jobs)
        }
        percentagesState.value = percentages
    }
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .border(0.8.dp, LightGrey, RoundedCornerShape(3)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {
            navHostController.navigate(ContextCompat.getString(context, R.string.job_list_show_screen))
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            val (
                title, divider, jobCount, remainingCount, chart, yetToStart,
                inProgress, cancelled, completed, inComplete,
            ) = createRefs()

            Text(
                text = stringResource(R.string.job_stats),
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
                color = Color.LightGray
            )

            Text(
                text = stringResource(R.string.jobs, jobs?.size ?: 0),
                style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                modifier = Modifier
                    .constrainAs(jobCount) {
                        start.linkTo(parent.start)
                        top.linkTo(divider.bottom)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = stringResource(
                    R.string.of_completed,
                    jobs?.count { it.status == JobStatus.Completed } ?: 0, jobs?.size ?: 0
                ),
                style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                modifier = Modifier
                    .constrainAs(remainingCount) {
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
                        top.linkTo(jobCount.bottom)
                    },
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (percentagesState.value.values.sum() > 0.0) {
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
            }

            createHorizontalChain(yetToStart, inProgress, chainStyle = ChainStyle.Packed)
            Row(
                modifier = Modifier
                    .constrainAs(yetToStart) {
                        top.linkTo(chart.bottom)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LightBlue, RoundedCornerShape(2.dp))
                )
                Text(
                    text = stringResource(
                        R.string.yet_to_start,
                        jobs?.count { it.status == JobStatus.YetToStart } ?: 0
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }


            Row(
                modifier = Modifier
                    .constrainAs(inProgress) {
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
                        R.string.in_progress_,
                        jobs?.count { it.status == JobStatus.InProgress } ?: 0
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            createHorizontalChain(cancelled, completed, chainStyle = ChainStyle.Packed)
            Row(
                modifier = Modifier
                    .constrainAs(cancelled) {
                        top.linkTo(yetToStart.bottom)
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
                        R.string.cancelled,
                        jobs?.count { it.status == JobStatus.Canceled } ?: 0
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .constrainAs(completed) {
                        top.linkTo(yetToStart.bottom)
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
                        R.string.completed,
                        jobs?.count { it.status == JobStatus.Completed } ?: 0
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .constrainAs(inComplete) {
                        top.linkTo(cancelled.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(LightRed, RoundedCornerShape(2.dp))
                )
                Text(
                    text = stringResource(
                        R.string.in_complete,
                        jobs?.count { it.status == JobStatus.Incomplete } ?: 0
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

