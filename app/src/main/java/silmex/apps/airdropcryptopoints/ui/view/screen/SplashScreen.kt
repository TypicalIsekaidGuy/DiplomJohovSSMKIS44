package silmex.apps.airdropcryptopoints.ui.view.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.FarmingProgressBG
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size

@Composable
fun SplashScreen(afterDelayFun: ()->Unit){
    Box(Modifier.fillMaxSize().background(color = AltBG)){
        Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(0.8f)
            .align(Alignment.Center)) {
            Image(painter = painterResource(id = R.drawable.airdrop_icon), contentDescription = "", modifier = Modifier.size(256.dp).align(Alignment.CenterHorizontally))
            Text("AirDrop Crypto Points", fontFamily = itimStyle, fontSize = big_text_size,color = MainTextColor)
            InfiniteLoadingAnimation()
            LaunchedEffect(key1 = true) {
                delay(3000L)
                afterDelayFun()
            }
        }
    }
}


@Composable
fun InfiniteLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val angle2 by infiniteTransition.animateFloat(
        initialValue = -120f,
        targetValue = 240f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val strokeWidth = 8.dp
    val outerRadius = 50.dp - strokeWidth / 2
    val innerRadius = 40.dp - strokeWidth / 2

    Box(
        modifier = Modifier
            .size(128.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .size(100.dp)
            .graphicsLayer { rotationZ = angle }) {
            drawArc(
                color = SideTextColor,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Canvas(modifier = Modifier
            .size(64.dp)
            .graphicsLayer { rotationZ = angle2 }) {
            drawArc(
                color = FarmingProgressBG,
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}