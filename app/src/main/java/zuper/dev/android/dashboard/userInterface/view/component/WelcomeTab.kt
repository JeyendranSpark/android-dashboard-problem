package zuper.dev.android.dashboard.userInterface.theme.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import zuper.dev.android.dashboard.R
import zuper.dev.android.dashboard.AppUtils.Utility.getCurrentDate
import zuper.dev.android.dashboard.AppUtils.Utility.getWelcomeMessage
import zuper.dev.android.dashboard.userInterface.theme.LightGrey
import zuper.dev.android.dashboard.userInterface.theme.LightLowBlue
import zuper.dev.android.dashboard.userInterface.theme.TextLightGrey
import zuper.dev.android.dashboard.userInterface.theme.TitleBlack

@Preview
@Composable
fun PreviewMainActivity() {
    WelcomeTab()
}

@Composable
fun WelcomeTab() {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .border(0.8.dp, LightGrey, RoundedCornerShape(8)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(start =  16.dp, end = 16.dp, top =  12.dp, bottom = 12.dp   )
                .fillMaxWidth(),
        ) {
            val (nameRow, image) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(nameRow) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .padding(end = 58.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = getWelcomeMessage(context, stringResource(R.string.profile_name)),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(TitleBlack),
                )
                Text(
                    text = getCurrentDate(context),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall.copy(TextLightGrey),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Image(
                painter = painterResource(id = R.drawable.user_profile_img),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .constrainAs(image) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .border(1.dp, LightLowBlue, RoundedCornerShape(8))
                    .shadow(1.dp, shape = RoundedCornerShape(8))
                    .clip(RoundedCornerShape(8)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

