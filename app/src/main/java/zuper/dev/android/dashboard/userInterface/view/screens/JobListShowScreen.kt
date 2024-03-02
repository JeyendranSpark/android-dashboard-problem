package zuper.dev.android.dashboard.userInterface.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.data.model.JobStatus
import zuper.dev.android.dashboard.userInterface.theme.BgLightGrey
import zuper.dev.android.dashboard.userInterface.theme.LightLowBlue
import zuper.dev.android.dashboard.userInterface.theme.TitleBlack
import zuper.dev.android.dashboard.userInterface.theme.view.component.JobChartView
import zuper.dev.android.dashboard.userInterface.theme.view.component.JobItemCardView
import zuper.dev.android.dashboard.userInterface.theme.viewModels.JobListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListShowScreen(navHostController: NavHostController, jobListViewModel: JobListViewModel = hiltViewModel()) {
    val jobsState by jobListViewModel.jobsState.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    Column() {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            title = {
                Text(
                    text = stringResource(R.string.jobs_count, jobsState?.size ?: 0),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(TitleBlack),
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    val back = R.string.jobs_count.toString();
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, back)
                }
            }
        )
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = {
                refreshing = true
                coroutineScope.launch {
                    jobListViewModel.refreshJobs()
                    refreshing = false
                }
            },
        ) {
            Column {

                jobsState?.let { jobList ->
                    if (jobList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_record_found),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    } else {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = LightLowBlue
                        )
                        JobChartView(jobs = jobList)
                        JobTabs(jobs = jobList)
                    }
                }
            }
        }
    }
}

@Composable
fun JobTabs(jobs: List<JobApiModel>) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val titles = listOf(
        stringResource(R.string.yet_to_start, jobs.count { it.status == JobStatus.YetToStart }),
        stringResource(R.string.in_progress_, jobs.count { it.status == JobStatus.InProgress }),
        stringResource(R.string.cancelled, jobs.count { it.status == JobStatus.Canceled }),
        stringResource(R.string.completed, jobs.count { it.status == JobStatus.Completed }),
        stringResource(R.string.in_complete, jobs.count { it.status == JobStatus.Incomplete })
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            containerColor = BgLightGrey,
            contentColor = TitleBlack,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[tabIndex])
                        .height(2.dp)
                        .background(color = MaterialTheme.colorScheme.primary),
                    color = Color.Transparent
                )
            }

        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(
                            text = title,
                        )
                    }
                )
            }
        }
        when (tabIndex) {
            in titles.indices -> JobList(jobs.filter { it.status == JobStatus.values()[tabIndex] })
        }
    }
}

@Composable
fun JobList(jobs: List<JobApiModel>) {
    if (jobs.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.no_record_found),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    } else {

        LazyColumn {
            items(jobs.size) { index ->
                val job = jobs[index]
                JobItemCardView(job)
                Box(
                    modifier = if (index == jobs.size - 1) {
                        Modifier.padding(bottom = 16.dp) // Add bottom padding to the last item
                    } else {
                        Modifier.fillMaxSize()
                    }
                )
            }
        }
    }

}

