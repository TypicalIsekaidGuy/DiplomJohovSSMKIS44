package com.johov.bitcoin.ui.view.screen

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM
import com.johov.bitcoin.data.model.enums.MULTIPLYER_ENUM
import com.johov.bitcoin.ui.view.composables.BalanceBar
import com.johov.bitcoin.ui.view.composables.ButtonBackground
import com.johov.bitcoin.utils.TagUtils
import com.johov.bitcoin.viewmodel.HomeViewModel
import com.johov.bitcoin.viewmodel.MainViewModel
Домашний экран был реализован с использованием подхода, характерного для Jetpack Compose — через разделение логики и отображения. Каждый элемент интерфейса получает данные из ViewModel, что позволяет ему обновляться автоматически при изменении состояния приложения. Это делает взаимодействие пользователя с интерфейсом динамичным и предсказуемым.

В основе построения экрана лежит колонна — компонент, который выстраивает содержимое вертикально. Такая структура удобна для мобильных устройств, где основное взаимодействие происходит в портретной ориентации. Внутри колонны размещены все ключевые элементы домашнего экрана, между которыми предусмотрены отступы для улучшения читаемости и восприятия информации.

Одним из центральных элементов является блок визуализации баланса , расположенный в верхней части экрана. Он показывает текущее состояние заработанных средств и использует анимированный текст для плавного изменения значений. Фон этого блока оформлен с помощью изображения, которое добавляет визуальную глубину и помогает акцентировать внимание на важности процесса заработка. Данные о балансе, прогрессе и состоянии активности обновляются в реальном времени, что даёт пользователю постоянную обратную связь о своих достижениях.

Ниже расположен элемент для увеличения скорости заработка , выполненный в виде квадратного блока с закруглёнными краями. При нажатии на него запускается рекламный ролик, после просмотра которого пользователь получает временный множитель к своей скорости получения средств. Возможность использования этой функции зависит от внутреннего состояния приложения, что контролируется через реактивные переменные. Эта часть интерфейса не только стимулирует пользователя к взаимодействию с рекламой, но и обеспечивает дополнительный источник дохода для разработчика.

Ещё одним важным элементом является кнопка получения вознаграждения , которая становится доступной только после истечения определённого времени — примерно 4 часов. Это сделано специально, чтобы мотивировать пользователя регулярно возвращаться в приложение и следить за своим прогрессом. Пока время не истекло, кнопка находится в неактивном состоянии, но остаётся видимой, напоминая о возможности получить награду. Такая система создаёт ожидание и усиливает эмоциональную вовлечённость пользователя в процесс.

Весь экран организован таким образом, чтобы пользователь сразу понимал, какие действия он может выполнить в данный момент. Последовательное расположение элементов, чёткая структура и равномерные отступы обеспечивают гармоничный внешний вид и простоту взаимодействия. Все компоненты связаны с системой состояний, что позволяет интерфейсу динамически меняться под текущие условия без необходимости ручного управления UI.
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    onLaunch: () -> Unit,
    checkup: () -> Unit
){
    val currentBoost = viewModel.currentChosenMultipliyer.observeAsState()
    val balance by viewModel.balance.observeAsState()
    val claimedBalance by mainViewModel.claimedBalance.observeAsState()
    val isMining by viewModel.isMining.observeAsState()
    val isMiningLD = viewModel.isMining
    val leftTime by viewModel.leftTime.observeAsState()
    val progress by viewModel.progress.observeAsState()
    val coins by mainViewModel.coins.observeAsState()
    val hadConnectionError by MainViewModel.hadConnectionError.observeAsState()
    val connectionErrorEnum by MainViewModel.connectionErrorEnum.observeAsState()

    var isUpgradable by remember {
        mutableStateOf(!isMiningLD.value!!||currentBoost.value== MULTIPLYER_ENUM.MULTYPLIER_55x)
    }
    Column {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            BalanceBar(claimedBalance!!,balance!!,progress!!, isMining!!,coins!!,currentBoost.value!!.value, mainViewModel::removeCoin)
            /*Spacer(modifier = Modifier.fillMaxHeight(0.04f))*/
            UpgradeWheel(isUpgradable,isMiningLD, currentBoost) { viewModel.upgradeWheelClick() }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            ClaimButton(currentBoost!!.value!!.value,balance!!,
                progress!!,leftTime!!,isMining!!,!isMining!!,!isMining!!, {
                    viewModel.claimClick()
                })

        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
}

@Composable
fun UpgradeWheel(isUpgradable: Boolean,isActive: MutableLiveData<Boolean>, currentBoost: State<MULTIPLYER_ENUM?>, upgradeWheelClick: ()->Unit) {

    var paddingValues1 by remember { mutableStateOf(0f) }

    val animatedPaddingValues1 by animateFloatAsState(
        targetValue = paddingValues1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BoxWithConstraints(
            Modifier
                .align(Alignment.Center)) {

            BoxWithConstraints(

                /*                if(screenHeightDp>600.dp)
                                    Modifier
                                        .fillMaxWidth()
                                        .height(maxWidth)
                            else */Modifier
                    .fillMaxHeight(0.6f)
                    .width(maxHeight)
                    .align(Alignment.Center)) {

                Stairs(modifier = Modifier.align(Alignment.Center),isUpgradable,isActive,currentBoost,upgradeWheelClick)
                /*OuterCircle(currentBoost, isActive.value!!)*/

            }
        }
        LaunchedEffect(currentBoost.value, isActive.value) {
            if(isActive.value!!){
                if(currentBoost.value== MULTIPLYER_ENUM.MULTYPLIER_1x){
                    while(true){
                        paddingValues1 = 16f
                        delay(600)
                        paddingValues1 = 0f
                        delay(600)
                    }
                }
                else if(currentBoost.value== MULTIPLYER_ENUM.MULTYPLIER_55x)
                else {
                    while(true){
                        paddingValues1 = 4f
                        delay(600)
                        paddingValues1 = 0f
                        delay(600)
                    }
                }
            }
        }
        val clickable = if(isActive.value!!) Modifier
            .clickable(
                enabled = isUpgradable,
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

/*        Box(
            Modifier
                .size(120.dp)
                .align(Alignment.Center)) {
            Image(
                painter = painterResource(id = if(isUpgradable) R.drawable.upgrade_center_active else R.drawable.upgrade_center_unactive),
                contentDescription = null,
                modifier = clickable
                    .padding(if (isUpgradable) animatedPaddingValues1.dp else 0.dp)
                    .align(Alignment.Center)
            )
        }*/
    }
}

@Composable
fun Stairs(modifier: Modifier,isUpgradable: Boolean,isActive: MutableLiveData<Boolean>, currentBoost: State<MULTIPLYER_ENUM?>, upgradeWheelClick: ()->Unit){

    var image = R.drawable.outer_circle_empty

    var s = false
    LaunchedEffect(key1 = currentBoost.value) {

        image = when(currentBoost.value!!){
            MULTIPLYER_ENUM.MULTYPLIER_1x -> R.drawable.upgrade0
            MULTIPLYER_ENUM.MULTYPLIER_2x -> R.drawable.upgrade1
            MULTIPLYER_ENUM.MULTYPLIER_3x -> R.drawable.upgrade2
            MULTIPLYER_ENUM.MULTYPLIER_5x -> R.drawable.upgrade3
            MULTIPLYER_ENUM.MULTYPLIER_8x -> R.drawable.upgrade4
            MULTIPLYER_ENUM.MULTYPLIER_13x -> R.drawable.upgrade5
            MULTIPLYER_ENUM.MULTYPLIER_21x -> R.drawable.upgrade6
            MULTIPLYER_ENUM.MULTYPLIER_34x -> R.drawable.upgrade_7
            MULTIPLYER_ENUM.MULTYPLIER_55x -> R.drawable.upgrade_8
        }
        s = currentBoost.value == MULTIPLYER_ENUM.MULTYPLIER_55x
    }

    image = when(currentBoost.value!!){
        MULTIPLYER_ENUM.MULTYPLIER_1x -> R.drawable.upgrade0
        MULTIPLYER_ENUM.MULTYPLIER_2x -> R.drawable.upgrade1
        MULTIPLYER_ENUM.MULTYPLIER_3x -> R.drawable.upgrade2
        MULTIPLYER_ENUM.MULTYPLIER_5x -> R.drawable.upgrade3
        MULTIPLYER_ENUM.MULTYPLIER_8x -> R.drawable.upgrade4
        MULTIPLYER_ENUM.MULTYPLIER_13x -> R.drawable.upgrade5
        MULTIPLYER_ENUM.MULTYPLIER_21x -> R.drawable.upgrade6
        MULTIPLYER_ENUM.MULTYPLIER_34x -> R.drawable.upgrade_7
        MULTIPLYER_ENUM.MULTYPLIER_55x -> R.drawable.upgrade_8
    }

    if(!isActive.value!!) {image = R.drawable.outer_circle_empty}

    Box(
        modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(1f)) {
        Image(painter = painterResource(id = image),"", modifier = Modifier
            .fillMaxWidth(if(MainActivity.isBiggerThen700dp==true)0.9f else 0.8f)
            .align(Alignment.Center)
            .fillMaxHeight(0.9f), contentScale = ContentScale.FillBounds)
        s = currentBoost.value == MULTIPLYER_ENUM.MULTYPLIER_55x
        UpgradeButton(Modifier.align(Alignment.Center),isActive.value!!,{upgradeWheelClick()},isMaxedOut=s)
    }
}

@Composable
fun UpgradeButton(modifier: Modifier,isActive: Boolean, upgradeWheelClick: ()->Unit, isMaxedOut: Boolean){


    var paddingValues by remember { mutableStateOf(20f) }
    var paddingFontValues by remember { mutableStateOf(4f) }

    val animatedPaddingValuesHor by animateFloatAsState(
        targetValue = paddingValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val animatedFontPaddingValues by animateFloatAsState(
        targetValue = paddingFontValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val text = "UPGRADE"

    LaunchedEffect(isMaxedOut,true){
        if(!isMaxedOut){
        while(true){
            paddingValues = 0f
            paddingFontValues = 0f
            delay(600)
            paddingValues = 0.03f
            paddingFontValues =4f
            delay(600)
        }}
        else{

            paddingValues = 0f
            paddingFontValues = 0f
        }
    }
    val modifier1 =
        modifier
            .clickable(
                enabled = isActive&&!isMaxedOut,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                MainScope().launch {
                    paddingValues = 0f
                    paddingFontValues = 0f
                    delay(300)
                    paddingValues = 0.05f
                    paddingFontValues = 4f
                    upgradeWheelClick()
                }
            }

    Image(painter = painterResource(id = if(isActive&&!isMaxedOut)R.drawable.button_upgrade else R.drawable.button_upgrade_off), contentDescription = "", modifier = modifier1.size(MainActivity.maxWidth?.times(0.3f)?:53.dp).padding(MainActivity.maxWidth?.times(animatedPaddingValuesHor)?:53.dp))
}

/*
@Composable
fun OuterCircle(enumValue: State<MULTIPLYER_ENUM?>, isActive: Boolean) {
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
    var color = if (isActive||enumValue.value!= MULTIPLYER_ENUM.MULTYPLIER_1x) SideTextColor else OffBGColor
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
                color = OffColorText,
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
                labelPaint.color = if (i == currentLabel) Color.White.toArgb() else MainTextColor.toArgb()
                if(!isActive&&i!=currentLabel){
                    labelPaint.color = OffTextColor.toArgb()
                }
                val label = "x" + segments[i].toString()
                drawContext.canvas.nativeCanvas.drawText(label, x.toFloat(), y.toFloat(), labelPaint)
            }
        }
    }
}

*/

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
    content: (@Composable (mod: Modifier) -> Unit)?=null
){
    ButtonBackground(multipliyer,balance,progress,leftTime,isFarming,isPressable,isAnimatable,{
        if (onPress != null) {
            onPress()
        }
    },content = content)
}