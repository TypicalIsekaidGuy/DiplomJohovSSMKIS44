package silmex.apps.airdropcryptopoints.ui.view.composables

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.AltBorder
import silmex.apps.airdropcryptopoints.ui.theme.ButtonTextColor
import silmex.apps.airdropcryptopoints.ui.theme.FarmingProgressBG
import silmex.apps.airdropcryptopoints.ui.theme.FarmingProgressEndedBG
import silmex.apps.airdropcryptopoints.ui.theme.ShadeBackground
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size_f
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.utils.StringUtils.getBalanceText

@Composable
fun ButtonBackground(
    multipliyer: Int,
    balance: Float,
    progress: Float,
    timerText: String,
    isFarming: Boolean = false,
     isPressable: Boolean = false,
     onPress: (() -> Unit)? = null,
     content: (@Composable () -> Unit)?=null,
     text: String = ""){
    if(isFarming){
        ActiveBackground(multipliyer,balance,progress,timerText)
    }
    else{
        InActiveBackground(isPressable = isPressable, onPress,content,"Claim")
    }
}
@Composable
fun InActiveBackground(
    isPressable: Boolean = false,
    onPress: (() -> Unit)? = null,
    content: (@Composable () -> Unit)?=null,
    text: String = "") {


    var paddingValues by remember { mutableStateOf(0f) }
    var paddingFontValues by remember { mutableStateOf(0f) }

    val animatedPaddingValuesHor by animateFloatAsState(
        targetValue = paddingValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val animatedFontPaddingValues by animateFloatAsState(
        targetValue = paddingFontValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val modifier = if(isPressable)
        Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                MainScope().launch {
                        paddingValues = 12f
                        paddingFontValues = 4f
                        delay(300)
                        paddingValues = 0f
                        paddingFontValues = 0f
                        if (onPress != null) {
                            onPress()
                        }
                        else{

                            throw IllegalArgumentException("onPress should not be null when isPressable is true")
                        }
                }
            }
        else Modifier
    Box(
        modifier
            .fillMaxWidth()
            .height(66.dp)){

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = animatedPaddingValuesHor.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 6.dp)
                    .border(1.dp, AltBorder, RoundedCornerShape(16.dp))
                    .height((64 - animatedPaddingValuesHor).dp)
                    .background(
                        ShadeBackground, RoundedCornerShape(16.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp)
                    .border(1.dp, AltBG, RoundedCornerShape(16.dp))
                    .height((64 - animatedPaddingValuesHor).dp)
                    .background(
                        ButtonTextColor, RoundedCornerShape(16.dp)
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
                    if(text!=""){
                        Text(
                            text = text,
                            color = WhiteText,
                            fontFamily = itimStyle,
                            fontSize = (big_text_size_f-animatedFontPaddingValues).sp,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxSize()
                                .align(Alignment.CenterVertically)
                                .padding(top = 12.dp)
                                .padding()
                        )
                    }
                    else
                        if (content != null) {
                            content()
                        }
                }
            }
        }
    }
}
@Composable
fun ActiveBackground(
    multipliyer: Int,
    balance: Float,
    progress: Float,
    timerText: String){

    Box(
        Modifier
            .fillMaxWidth()
            .height(66.dp)){
        Box(Modifier.fillMaxWidth()) {
            val brush = Brush.horizontalGradient(
                0.0f to FarmingProgressBG,
                progress to FarmingProgressBG,
                progress+ 0.00001f to FarmingProgressEndedBG,
                1.0f to FarmingProgressEndedBG)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 6.dp)
                    .border(1.dp, AltBorder, RoundedCornerShape(16.dp))
                    .height(64.dp)
                    .background(
                        ShadeBackground, RoundedCornerShape(16.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp)
                    .border(1.dp, AltBG, RoundedCornerShape(16.dp))
                    .height(64.dp)
                    .background(
                        brush, RoundedCornerShape(16.dp)
                    )
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)){
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.align(Alignment.CenterVertically)){

                        Text(//usdt
                            text = "Mining",
                            color = WhiteText,
                            textAlign = TextAlign.Center,fontFamily = itimStyle,
                            fontSize = 20.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                            Image(painterResource(id = R.drawable.balance_small_icon),"",)

                            Text(//usdt
                                text = getBalanceText(balance.toFloat()),
                                color = WhiteText,
                                textAlign = TextAlign.Center,fontFamily = itimStyle,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){

                        Text(//usdt
                            text = timerText,
                            color = WhiteText,
                            textAlign = TextAlign.Center,fontFamily = itimStyle,
                            fontSize = 20.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                            Image(painterResource(id = R.drawable.boost_small_icon),"",)

                            Text(//usdt
                                text = "x$multipliyer",
                                color = WhiteText,
                                textAlign = TextAlign.Center,fontFamily = itimStyle,
                                fontSize = 20.sp
                            )
                        }
                    }

                }
            }
        }
    }
}
/*
fun dateToString(date: DateTime){

}*/
