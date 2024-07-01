package silmex.apps.airdropcryptopoints.ui.view.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle

@Composable
fun NavigateToButton(text: String = "NEXT",modifier: Modifier, navitgateTo: ()->Unit){
    InActiveBackground(true,{navitgateTo()}, null,text)
}
