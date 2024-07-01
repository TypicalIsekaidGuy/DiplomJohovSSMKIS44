package silmex.apps.airdropcryptopoints.ui.view.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.FarmingProgressBG
import silmex.apps.airdropcryptopoints.ui.theme.MainBG
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.utils.StringUtils.getBalanceText
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM

@Composable
fun BalanceBar() {
    val isFarming = false
    val balance = 1
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3F)
            .background(if (isFarming) AltBG else MainBG, RoundedCornerShape(16.dp))
            .border(1.dp, FarmingProgressBG, RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Your balance", fontSize = big_text_size, color = MainTextColor, fontFamily = itimStyle)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("+", fontSize = 48.sp, color = SideTextColor, fontFamily = itimStyle)
                        Text(getBalanceText(balance), fontSize = 48.sp, color = SideTextColor, fontFamily = itimStyle)
                        Spacer(Modifier.height(1.dp))
                    }
                    Text("Crypto Points", fontSize = medium_text_size, color = SideTextColor, fontFamily = itimStyle)
                }
                Spacer(Modifier.width(48.dp))
            }
            Spacer(Modifier.height(12.dp))
        }
        FallingCoins()
    }
}

@Composable
fun FallingCoins() {
    var coins by remember { mutableStateOf(listOf<Coin>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            for(i in 0..MULTIPLYER_ENUM.MULTYPLIER_5x.value){
                coins = coins + Coin(
                    id = System.currentTimeMillis(),
                    x = (0..300).random().toFloat()
                )
            }
            delay(1000)
        }
    }

    coins.forEach { coin ->
        FallingCoin(
            modifier = Modifier,
            coin = coin,
            onRemove = { id -> coins = coins.filter { it.id != id } }
        )
    }
}

val almostAlpha0 = 0.000001f

@Composable
fun FallingCoin(modifier: Modifier, coin: Coin, onRemove: (Long) -> Unit) {
    val yOffsetRandom = (0..50).random().toFloat()
    val yOffset = remember { Animatable(yOffsetRandom) }
    val alpha = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(coin) {
        scope.launch {
            yOffset.animateTo(
                targetValue = 200f+yOffsetRandom, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
        scope.launch {
            Log.d("IdCheckUp",coin.x.toString() + " " + coin.id.toString())
            alpha.animateTo(
                targetValue = almostAlpha0,
                animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
            )
            onRemove(coin.id) // Remove the coin after the animation ends
        }
    }
    SideEffect {
        Log.d("UIDEBUG",(coin.x).dp.value.toString())
    }
    if (alpha.value > almostAlpha0) {
        Image(
            painter = rememberImagePainter(data = coin.image),
            contentDescription = "",
            modifier = modifier
                .absoluteOffset(x = (coin.x).dp, y = yOffset.value.dp)
                .size(32.dp)
                .alpha(alpha.value)
        )
    }
}

data class Coin(
    val id: Long,
    val x: Float,
    val image: Int = R.drawable.anim_item // default coin image, replace with your coin image
)
