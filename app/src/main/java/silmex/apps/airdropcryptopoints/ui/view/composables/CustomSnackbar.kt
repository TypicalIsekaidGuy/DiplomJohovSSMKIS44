package silmex.apps.airdropcryptopoints.ui.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import silmex.apps.airdropcryptopoints.MainActivity
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.ToastNotSuceded
import silmex.apps.airdropcryptopoints.ui.theme.ToastSucceded
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle

@Composable
fun CustomSnackbar(data: SnackbarData){
    Box(modifier = Modifier
        .background(
            if (MainActivity.hasSucceded == null || MainActivity.hasSucceded == false) ToastNotSuceded else ToastSucceded,
            shape = RoundedCornerShape(16.dp)
        )
        .fillMaxHeight(0.08f)
        .fillMaxWidth()){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Center).padding(horizontal = 8.dp)){
            Text(data.visuals.message, fontSize = 20.sp, color = MainTextColor, fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier.align(
                Alignment.CenterVertically).fillMaxWidth(0.9f))
        }
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.align(Alignment.CenterEnd)){
            Image(painterResource(id = R.drawable.dismiss_snackbar),"",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        data.dismiss()
                    })
            Spacer(Modifier.width(16.dp))
        }
    }
}