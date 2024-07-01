package silmex.apps.airdropcryptopoints.ui.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import silmex.apps.airdropcryptopoints.ui.view.composables.MultiStyleText
import silmex.apps.airdropcryptopoints.ui.view.composables.NavigateToButton

@Composable
fun LearningScreen3(navigateTo:()->Unit){

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp), verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ProgressIndicator(3)
        Column(
            Modifier
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Withdraw Crypto Points to wallet",
                color = MainTextColor,
                fontFamily = itimStyle,
                fontSize = big_text_size,
                textAlign = TextAlign.Start
            )

            Image(painter = painterResource(id = R.drawable.withdraw_illustration), contentDescription = "", modifier = Modifier.align(
                Alignment.CenterHorizontally).fillMaxWidth(), contentScale = ContentScale.FillWidth)
            NavigateToButton("START",Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),{navigateTo()})
        }
    }
}