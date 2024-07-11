package silmex.apps.airdropcryptopoints.ui.view.composables

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.AlmostTransparent
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.FarmingProgressBG
import silmex.apps.airdropcryptopoints.ui.theme.MainBG
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.utils.IntegerUtils
import silmex.apps.airdropcryptopoints.utils.StringUtils.getBalanceText

@Composable
fun BalanceBar(balance: Float,progress: Float, isFarming: Boolean, coins: List<Coin?>,currentMultiplier: Int, onRemove: (Int) -> Unit) {
    var progress = 1-progress
    var alphaText by remember { mutableStateOf(0f) }

    val animatedAlphaText by animateFloatAsState(
        targetValue = alphaText,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    var image by remember { mutableStateOf(R.drawable.empty) }
    LaunchedEffect(progress) {

        if(progress!=1f){

            when(progress){
                in 0f..(1f / 5f) -> image = R.drawable.full_1
                in (1f / 5f)..(1f / 5f)*2 -> image = R.drawable.full_2
                in (1f / 5f)*2..(1f / 5f)*3 -> image = R.drawable.full_3
                in (1f / 5f)*3 ..(1f / 5f)*4 -> image = R.drawable.full_4
                in (1f / 5f)*4..1f -> image = R.drawable.full_5
            }
        }
        else{
            image = R.drawable.empty
        }
        alphaText = if(alphaText== almostAlpha0){
            1f
        } else almostAlpha0
    }
    var finalColorPlus by remember { mutableStateOf(AlmostTransparent) }
    val animatedColorPlus by animateColorAsState(
        targetValue = finalColorPlus,
        animationSpec = tween(durationMillis = 400)
    )
    LaunchedEffect(balance) {
        if(balance.toInt() !=0){
            finalColorPlus = SideTextColor
            delay(300)
            finalColorPlus = AlmostTransparent
        }
    }
    BoxWithConstraints(
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
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Crypto Points", fontSize = medium_text_size, color = SideTextColor, fontFamily = itimStyle)
                }
                Spacer(Modifier.width(48.dp))
            }
            Spacer(Modifier.height(12.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("+", fontSize = 48.sp, color = animatedColorPlus, fontFamily = itimStyle, modifier = Modifier)
            Text(getBalanceText(balance), fontSize = 48.sp, color = SideTextColor, fontFamily = itimStyle, modifier = Modifier)
            Spacer(Modifier.height(1.dp))
        }
        FallingCoins(coins,maxHeight.value,maxWidth.value,1f+currentMultiplier*0.01f, onRemove)
        Text("Don't forget to claim points when the timer ends", textAlign = TextAlign.Center, fontSize = average_text_size, color = FarmingProgressBG, fontFamily = itimStyle, maxLines = 1, modifier = Modifier
            .align(
                Alignment.BottomCenter
            )
            .alpha(animatedAlphaText)
            .padding(bottom = 8.dp)
            .fillMaxWidth())
    }
}

fun formatTime(time: Int): String {
    val minutes = time / 60
    val seconds = time % 60
    return String.format("%02d:%02d", minutes, seconds)
}
@Composable
fun FallingCoins(coins: List<Coin?>,maxHeight: Float, maxWidth: Float,speedModifier: Float,onRemove: (Int) -> Unit) {
    val scope = rememberCoroutineScope()

/*    LaunchedEffect(Unit) {
        while (true) {
            for(i in 0..MULTIPLYER_ENUM.MULTYPLIER_5x.value){
                coins = coins + Coin(
                    id = System.currentTimeMillis(),
                    x = (0..300).random().toFloat()
                )
            }
            delay(1000)
        }
    }*/
    for(i in 0..<coins.size){
        if(coins[i]!=null){
            FallingCoin(
                modifier = Modifier,
                coin = coins[i]!!,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                speedModifier = speedModifier,
                onRemove = { onRemove(i) }
            )
        }
    }
/*    coins.forEach { coin ->
        FallingCoin(
            modifier = Modifier,
            coin = coin,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            onRemove = { onRemove(coin.key) }
        )
    }*/
}


@Composable
fun FallingCoin(modifier: Modifier, maxHeight: Float, maxWidth:Float, coin: Coin,speedModifier: Float, onRemove: (Integer) -> Unit) {
    val yOffset = remember { Animatable(coin.startY*maxHeight) }
    val xOffset = remember { Animatable(coin.startX*maxWidth) }
    val size = remember { Animatable(1f) }
    val alpha = remember { Animatable(almostAlpha0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(coin.id) {
        scope.launch {
            yOffset.animateTo(
                targetValue = coin.endY*maxHeight, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = (1000/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
        }
        scope.launch {
            xOffset.animateTo(
                targetValue = coin.endX*maxWidth, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = (1000/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
        }
        scope.launch {
            size.animateTo(
                targetValue = coin.endSize, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = (1000/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
        }
        scope.launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = (500/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
            delay((200/speedModifier).toLong())
            alpha.animateTo(
                targetValue = almostAlpha0,
                animationSpec = tween(durationMillis = (300/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
            Log.d("Worked","work + "+coin.id)
            onRemove(coin.id) // Remove the coin after the animation ends
        }
    }
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.anim_item),
        contentDescription = "",
        modifier = modifier
            .absoluteOffset(x = xOffset.value.dp, y = yOffset.value.dp)
            .size((32 * size.value).dp)
            .alpha(alpha.value)
    )
}

val almostAlpha0 = 0.000001f

data class Coin(
    var id: Integer,
    var endSize: Float,
    var startX: Float = (0..1).random().toFloat(),
    var startY: Float = (0..1).random().toFloat(),
    var endX: Float = (0..1).random().toFloat(),
    var endY: Float = (0..1).random().toFloat(),
)
