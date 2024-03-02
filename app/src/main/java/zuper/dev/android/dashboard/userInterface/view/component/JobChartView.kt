package zuper.dev.android.dashboard.userInterface.theme.view.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.AppUtils.Utility.calculateJobPercentagesWithColors
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.userInterface.theme.BgLightGrey
import zuper.dev.android.dashboard.userInterface.theme.TextDarkGrey


@Composable
fun JobChartView(jobs: List<JobApiModel>) {

    val percentagesState = remember { mutableStateOf<Map<Color,Double>>(emptyMap()) }

    LaunchedEffect(jobs) {
        val percentages = withContext(Dispatchers.Default) {
            calculateJobPercentagesWithColors(jobs)
        }
        percentagesState.value = percentages
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = BgLightGrey
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            val (jobCount, remainingCount, chart, divider) = createRefs()

            Text(
                text = stringResource(R.string.jobs, jobs.size),
                style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                modifier = Modifier
                    .constrainAs(jobCount) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = stringResource(
                    R.string.of_completed,
                    jobs.count { it.status == JobStatus.Completed } ?: 0, jobs.size ?: 0
                ),
                style = MaterialTheme.typography.bodySmall.copy(TextDarkGrey),
                modifier = Modifier
                    .constrainAs(remainingCount) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .constrainAs(chart) {
                        top.linkTo(jobCount.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (percentagesState.value.values.sum() > 0.0) {
                        percentagesState.value.forEach  { (color, percentage) ->
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

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(divider) {
                        top.linkTo(chart.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                thickness = 0.8.dp,
                color = LightGray
            )
        }
    }
}

@Composable
fun HorizontalDivider(modifier: Modifier = Modifier,
                      thickness: Dp = DividerDefaults.Thickness,
                      color: Color = DividerDefaults.color,
) = Canvas(modifier.fillMaxWidth().height(thickness)) {
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        start = Offset(0f, thickness.toPx() / 2),
        end = Offset(size.width, thickness.toPx() / 2),
    )
}

