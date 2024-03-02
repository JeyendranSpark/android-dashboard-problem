package zuper.dev.android.dashboard.userInterface.theme.view.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.AppUtils.Utility
import zuper.dev.android.dashboard.data.model.JobApiModel
import zuper.dev.android.dashboard.userInterface.theme.TextDarkGrey
import zuper.dev.android.dashboard.userInterface.theme.TitleBlack


@Composable
fun JobItemCardView(job: JobApiModel) {
    val context= LocalContext.current
    Card(

        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(6)),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            Text(
                text = stringResource(R.string.job_number, job.jobNumber),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextDarkGrey
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Text(
                text = job.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TitleBlack
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 5.dp)
            )
            Text(
                text = Utility.formatDateTime(context,job.startTime, job.endTime),
                style = MaterialTheme.typography.bodyMedium.copy(TextDarkGrey),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }

}


