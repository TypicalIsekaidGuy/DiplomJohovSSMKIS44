package silmex.apps.airdropcryptopoints.ui.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.ButtonTextColor
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.ui.view.composables.DashedLine
import silmex.apps.airdropcryptopoints.ui.view.composables.MultiStyleText
import silmex.apps.airdropcryptopoints.ui.view.composables.NavigateToButton

@Composable
fun LearningScreen2(navigateTo:()->Unit){

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            ProgressIndicator(2)
            Column(
                Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(70.dp)
            ) {
                Text(
                    text = "Invite friends and get \n 10 000 Crypto Points",
                    color = MainTextColor,
                    fontFamily = itimStyle,
                    fontSize = big_text_size,
                    textAlign = TextAlign.Start
                )

                MultiStyleText(
                    modifier = Modifier,
                    colors = listOf(
                        MainTextColor, ButtonTextColor, MainTextColor, SideTextColor
                    ),
                    fontSize = average_text_size,
                    textAlign = TextAlign.Center,
                    "Every new user who entered ",
                    "your promo code",
                    " will ",
                    "receive"
                )
                Image(
                    painter = painterResource(id = R.drawable.get_bonus_illustration),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

        Column (Modifier.align(Alignment.BottomCenter)){
            Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp)){
                NavigateToButton("NEXT", Modifier, { navigateTo() })
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}