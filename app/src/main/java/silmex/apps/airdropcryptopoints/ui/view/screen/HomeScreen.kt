package silmex.apps.airdropcryptopoints.ui.view.screen

import android.annotation.SuppressLint
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.fonts.FontStyle
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.MainBG
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.ui.view.composables.BalanceBar
import silmex.apps.airdropcryptopoints.ui.view.composables.ButtonBackground
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(){
    var currentBoost by remember { mutableStateOf(MULTIPLYER_ENUM.MULTYPLIER_1x) }
    var isFarming by remember {
        mutableStateOf(false)
    }
    val isPressable by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(true) {
        while(true){

            delay(1000L)
            when(currentBoost){
                MULTIPLYER_ENUM.MULTYPLIER_1x-> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_2x
                MULTIPLYER_ENUM.MULTYPLIER_2x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_3x
                MULTIPLYER_ENUM.MULTYPLIER_3x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_5x
                MULTIPLYER_ENUM.MULTYPLIER_5x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_8x
                MULTIPLYER_ENUM.MULTYPLIER_8x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_13x
                MULTIPLYER_ENUM.MULTYPLIER_13x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_21x
                MULTIPLYER_ENUM.MULTYPLIER_21x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_34x
                MULTIPLYER_ENUM.MULTYPLIER_34x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_55x
                MULTIPLYER_ENUM.MULTYPLIER_55x -> currentBoost = MULTIPLYER_ENUM.MULTYPLIER_1x
            }
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val isActive = remember {
            mutableStateOf(false)
        }
        BalanceBar()
        UpgradeWheel(isActive, currentBoost)
        ClaimButton(isFarming,isPressable, {isFarming = true})
    }
}

@Composable
fun UpgradeWheel(isActive: MutableState<Boolean>, currentBoost: MULTIPLYER_ENUM) {



    var paddingValues1 by remember { mutableStateOf(0f) }

    val animatedPaddingValues1 by animateFloatAsState(
        targetValue = paddingValues1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BoxWithConstraints {

            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .height(maxWidth)) {

                OuterCircle(currentBoost)

            }
        }
        LaunchedEffect(true) {
            if(!isActive.value){
                while(!isActive.value){
                    paddingValues1 = 16f
                    delay(600)
                    paddingValues1 = 0f
                    delay(600)
                }
            }
            else{
                while(isActive.value){
                    paddingValues1 = 4f
                    delay(600)
                    paddingValues1 = 0f
                    delay(600)
                }
            }
        }
        Box(
            Modifier
                .size(120.dp)
                .align(Alignment.Center)) {
            Image(
                painter = painterResource(id = if(isActive.value) R.drawable.upgrade_center_active else R.drawable.upgrade_center_unactive),
                contentDescription = null,
                modifier = Modifier.padding(animatedPaddingValues1.dp).align(Alignment.Center)

                    .clickable(interactionSource = remember{ MutableInteractionSource() }, indication = null,) {

                        MainScope().launch {
                            paddingValues1 = 12f
                            delay(300)
                            paddingValues1 = 0f
                        }
                    }
            )
        }
    }
}


@Composable
fun OuterCircle(enumValue: MULTIPLYER_ENUM) {
    val segments = listOf(
        MULTIPLYER_ENUM.MULTYPLIER_1x.value,
        MULTIPLYER_ENUM.MULTYPLIER_2x.value,
        MULTIPLYER_ENUM.MULTYPLIER_3x.value,
        MULTIPLYER_ENUM.MULTYPLIER_5x.value,
        MULTIPLYER_ENUM.MULTYPLIER_8x.value,
        MULTIPLYER_ENUM.MULTYPLIER_13x.value,
        MULTIPLYER_ENUM.MULTYPLIER_21x.value,
        MULTIPLYER_ENUM.MULTYPLIER_34x.value,
        MULTIPLYER_ENUM.MULTYPLIER_55x.value,
    )
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = canvasWidth / 2f
            val angleStep = 360f / segments.size
            val currentLabel = enumValue.ordinal
            val offset = -80f
            val selectedOffset = -8f
            val optimisingOffset = 28f

            // Calculate start and sweep angles
            val startAngleSelected = currentLabel * angleStep + offset - angleStep / 2 + optimisingOffset -30f
            val sweepAngleSelected = angleStep + 5f

            drawArc(
                color = AltBG,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = true
            )
            drawArc(
                color = SideTextColor,
                startAngle = startAngleSelected,
                sweepAngle = sweepAngleSelected,
                useCenter = true
            )

            // Draw the smaller gray circle with a slight offset to avoid artifacts
            drawCircle(
                color = MainBG,
                radius = radius * 0.4f,
                center = center
            )

            // Draw the text labels and numbers
            val labelTextSize = medium_text_size
            val labelPaint = Paint().apply {
                textSize = labelTextSize.toPx()
                textAlign = Paint.Align.CENTER
                typeface = ResourcesCompat.getFont(context, R.font.itim)
            }
            val textRadius = radius * 0.7f

            for (i in segments.indices) {
                val angle = i * angleStep + offset
                val x = canvasWidth / 2f + Math.cos(Math.toRadians(angle.toDouble())) * textRadius
                val y = canvasHeight / 2f + Math.sin(Math.toRadians(angle.toDouble())) * textRadius
                labelPaint.color = if (i == currentLabel) WhiteText.toArgb() else MainTextColor.toArgb()
                val label = "x" + segments[i].toString()
                drawContext.canvas.nativeCanvas.drawText(label, x.toFloat(), y.toFloat(), labelPaint)
            }
        }
    }
}



@Composable
fun ClaimButton(isFarming: Boolean,
                isPressable: Boolean,
                onPress: (() -> Unit)? = null,
                content: (@Composable () -> Unit)?=null){
    ButtonBackground(isFarming,isPressable,{
        if (onPress != null) {
            onPress()
        }
    },content)
}