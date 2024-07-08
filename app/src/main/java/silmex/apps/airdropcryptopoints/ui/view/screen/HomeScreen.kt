package silmex.apps.airdropcryptopoints.ui.view.screen

import android.graphics.Paint
import android.util.Log
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.data.model.MULTIPLYER_ENUM
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.MainBG
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.OffBGColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.ui.view.composables.BalanceBar
import silmex.apps.airdropcryptopoints.ui.view.composables.ButtonBackground
import silmex.apps.airdropcryptopoints.viewmodel.HomeViewModel
import silmex.apps.airdropcryptopoints.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel,mainViewModel: MainViewModel){
    val currentBoost = viewModel.currentChosenMultipliyer.observeAsState()
    val balance by viewModel.balance.observeAsState()
    val isMining by viewModel.isMining.observeAsState()
    val isMiningLD = viewModel.isMining
    val leftTime by viewModel.leftTime.observeAsState()
    val progress by viewModel.progress.observeAsState()
    val coins by mainViewModel.coins.observeAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BalanceBar(balance!!,progress!!, isMining!!,coins!!,currentBoost.value!!.value, mainViewModel::removeCoin)
        UpgradeWheel(isMiningLD, currentBoost) { viewModel.upgradeWheelClick() }
        ClaimButton(currentBoost!!.value!!.value,balance!!,
            progress!!,leftTime!!,isMining!!,!isMining!!,!isMining!!, {
            viewModel.claimClick()
        })
    }
}

@Composable
fun UpgradeWheel(isActive: MutableLiveData<Boolean>, currentBoost: State<MULTIPLYER_ENUM?>, upgradeWheelClick: ()->Unit) {



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

                OuterCircle(currentBoost, isActive.value!!)

            }
        }
        LaunchedEffect(currentBoost.value) {
            if(isActive.value!!){
                if(currentBoost.value!=MULTIPLYER_ENUM.MULTYPLIER_1x){

                    while(true){
                        paddingValues1 = 4f
                        delay(600)
                        paddingValues1 = 0f
                        delay(600)
                    }
                }
                else if(currentBoost.value==MULTIPLYER_ENUM.MULTYPLIER_55x){

                }
                else {
                    while(true){
                        paddingValues1 = 16f
                        delay(600)
                        paddingValues1 = 0f
                        delay(600)
                    }
                }
            }
        }
        val clickable = if(isActive.value!!) Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {

                MainScope().launch {
                    paddingValues1 = 12f
                    delay(300)
                    paddingValues1 = 0f
                    upgradeWheelClick()
                }
            } else   Modifier

        Box(
            Modifier
                .size(120.dp)
                .align(Alignment.Center)) {
            Image(
                painter = painterResource(id = if(isActive.value!!) R.drawable.upgrade_center_active else R.drawable.upgrade_center_unactive),
                contentDescription = null,
                modifier = clickable
                    .padding(if (currentBoost.value!=MULTIPLYER_ENUM.MULTYPLIER_55x) animatedPaddingValues1.dp else 0.dp)
                    .align(Alignment.Center)
            )
        }
    }
}


@Composable
fun OuterCircle(enumValue: State<MULTIPLYER_ENUM?>,isActive: Boolean) {
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
    var color = if (isActive||enumValue.value!=MULTIPLYER_ENUM.MULTYPLIER_1x) SideTextColor else OffBGColor
    LaunchedEffect (isActive){
        Log.d("workd",""+isActive)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = canvasWidth / 2f
            val angleStep = 360f / segments.size
            val currentLabel = enumValue!!.value!!.ordinal
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
                color = color,
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
fun ClaimButton(
    multipliyer: Int,
    balance: Float,
    progress: Float,
    leftTime: Long,
    isFarming: Boolean,
    isPressable: Boolean,
    isAnimatable: Boolean,
    onPress: (() -> Unit)? = null,
    content: (@Composable () -> Unit)?=null
){
    ButtonBackground(multipliyer,balance,progress,leftTime,isFarming,isPressable,isAnimatable,{
        if (onPress != null) {
            onPress()
        }
    },content)
}