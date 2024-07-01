package silmex.apps.airdropcryptopoints.ui.view.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle

@Composable
fun MultiStyleText(modifier: Modifier = Modifier, colors: List<Color>, fontSize: TextUnit = average_text_size, textAlign: TextAlign = TextAlign.Center, vararg texts: String) {
    if (texts.size!= colors.size) {
        throw IllegalArgumentException("texts and colors must have the same size")
    }

    Text(buildAnnotatedString {
        texts.indices.forEach { index ->
            withStyle(style = SpanStyle(color = colors[index], fontFamily = itimStyle, fontSize = fontSize)) {
                append(texts[index])
            }
        }
    }, textAlign = textAlign, modifier = modifier)
}