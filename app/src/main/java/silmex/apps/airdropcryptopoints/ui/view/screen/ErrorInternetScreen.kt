package silmex.apps.airdropcryptopoints.ui.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.ButtonTextColor
import silmex.apps.airdropcryptopoints.ui.theme.FarmingProgressBG
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.view.composables.MultiStyleText
import silmex.apps.airdropcryptopoints.ui.view.composables.NavigateToButton

@Composable
fun ErrorInternetScreen(tryToReconnect: ()->Unit){
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(48.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            Column(
                Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Oops, error...",
                    color = FarmingProgressBG,
                    fontFamily = itimStyle,
                    fontSize = big_text_size,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Something went wrong",
                    color = FarmingProgressBG,
                    fontFamily = itimStyle,
                    fontSize = big_text_size,
                    textAlign = TextAlign.Center
                )
            }
            Image(
                painter = painterResource(id = R.drawable.internet_error),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .size(256.dp)
            )
            Text(
                text = "Check your internet connection",
                color = SideTextColor,
                fontFamily = itimStyle,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }

        Column (Modifier.align(Alignment.BottomCenter)){
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)){
                NavigateToButton("RECONNECT", Modifier, { tryToReconnect() })
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}