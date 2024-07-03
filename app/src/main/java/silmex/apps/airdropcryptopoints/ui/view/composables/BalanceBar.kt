package silmex.apps.airdropcryptopoints.ui.view.composables

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
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
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.utils.IntegerUtils
import silmex.apps.airdropcryptopoints.utils.StringUtils.getBalanceText

@Composable
fun BalanceBar(balance: Float,progress: Float, isFarming: Boolean, coins: List<Coin?>, onRemove: (Int) -> Unit) {
    var progress = 1-progress
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
    }
    var finalColorPlus by remember { mutableStateOf(AlmostTransparent) }
    val animatedColorPlus by animateColorAsState(
        targetValue = finalColorPlus,
        animationSpec = tween(durationMillis = 400)
    )
    LaunchedEffect(balance) {
        finalColorPlus = SideTextColor
        delay(200)
        finalColorPlus = AlmostTransparent
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text("+", fontSize = 48.sp, color = animatedColorPlus, fontFamily = itimStyle, modifier = Modifier)
                        Text(getBalanceText(balance), fontSize = 48.sp, color = SideTextColor, fontFamily = itimStyle, modifier = Modifier)
                        Spacer(Modifier.height(1.dp))
                    }
                    Text("Crypto Points", fontSize = medium_text_size, color = SideTextColor, fontFamily = itimStyle)
                }
                Spacer(Modifier.width(48.dp))
            }
            Spacer(Modifier.height(12.dp))
        }
        FallingCoins(coins,maxHeight.value,maxWidth.value, onRemove)
    }
}

@Composable
fun FallingCoins(coins: List<Coin?>,maxHeight: Float, maxWidth: Float,onRemove: (Int) -> Unit) {
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
fun FallingCoin(modifier: Modifier, maxHeight: Float, maxWidth:Float, coin: Coin, onRemove: (Integer) -> Unit) {
    val yOffset = remember { Animatable(coin.startY*maxHeight) }
    val xOffset = remember { Animatable(coin.startX*maxWidth) }
    val size = remember { Animatable(1f) }
    val alpha = remember { Animatable(almostAlpha0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(coin.id) {
        scope.launch {
            yOffset.animateTo(
                targetValue = coin.endY*maxHeight, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
        scope.launch {
            xOffset.animateTo(
                targetValue = coin.endX*maxWidth, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
        scope.launch {
            size.animateTo(
                targetValue = coin.endSize, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
        scope.launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
            delay(300L)
            alpha.animateTo(
                targetValue = almostAlpha0,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
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
