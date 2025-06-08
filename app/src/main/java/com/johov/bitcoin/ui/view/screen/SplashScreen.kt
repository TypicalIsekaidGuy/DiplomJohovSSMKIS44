package com.johov.bitcoin.ui.view.screen

import android.util.Log
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.utils.MethodUtils
import com.johov.bitcoin.utils.TagUtils
import com.johov.bitcoin.viewmodel.MainViewModel
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun SplashScreen(mainViewModel: MainViewModel,afterDelayFun: ()->Unit){
    val errorEnum by MainViewModel.connectionErrorEnum.observeAsState()
    val hadConnectionError by MainViewModel.hadConnectionError.observeAsState()
    val appName = stringResource(id = R.string.app_name)

    LaunchedEffect(hadConnectionError) {
        Log.d(TagUtils.MAINVIEWMODELTAG,"connectoin error"+hadConnectionError)
        Log.d(TagUtils.MAINVIEWMODELTAG,"connectoin error"+errorEnum)
        if(errorEnum== CONNECTION_ERROR_ENUM.LOAD_ALL_DATA_STARTUP){
            Log.d(TagUtils.MAINVIEWMODELTAG,"connectoin error"+"did work")
            MethodUtils.safeSetValue(MainViewModel.connectionErrorEnum,null)
            delay(5000L)
            mainViewModel.loadAllData()
        }
        MethodUtils.safeSetValue(MainViewModel.hadConnectionError,false)
        delay(3000L)
        afterDelayFun()
    }

    Box(
        Modifier
            .paint(painterResource(id = R.drawable.splash), contentScale = ContentScale.FillWidth)
            .fillMaxSize()){
        Column(verticalArrangement = Arrangement.spacedBy(MainActivity.maxHeight?.times(0.1f)?:50.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(0.8f)
            .align(Alignment.Center)) {

            Image(painter = painterResource(id = R.drawable.airdrop_icon), contentDescription = "", modifier = Modifier
                .size(256.dp)
                .align(Alignment.CenterHorizontally))
            Text("Bitcoin Earner", fontFamily = krub_sb,  fontSize = 28.sp,color = Color(0xFFE7E7FB))


        }
        WaveAnimationScreen(/*Modifier.align(Alignment.BottomCenter)*/)
        /*WaveRailAnimationScreen()*/
/*
        InfiniteLoadingAnimation(Modifier.align(Alignment.BottomEnd).padding(end = MainActivity.maxWidth?.times(0.02f)?:16.dp, bottom = MainActivity.maxWidth?.times(0.02f)?:16.dp))
*/
    }
}
/*@Composable
fun WaveAnimationScreen() {
    // Get the screen width from MainActivity or use a fallback default value
    val screenWidth = MainActivity.maxWidth?.value ?: 100f

    // Calculate start and end X positions (10% to 80% of screen width)
    val startX = screenWidth * 0.07f
    val endX = screenWidth * 0.77f

    // Infinite transition for smooth back-and-forth motion with pauses at peaks
    val infiniteTransition = rememberInfiniteTransition()
    val xOffset by infiniteTransition.animateFloat(
        initialValue = startX,
        targetValue = endX,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = EaseInOutQuad // Smooth easing with slow peaks
            ),
            repeatMode = RepeatMode.Reverse // Move back and forth smoothly
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Moving object (ball) between the waves
        WaveMovingObject(
            modifier = Modifier.size(40.dp).align(BiasAlignment(-1f, 0.7f)),
            xOffset = xOffset,
            waveHeight = MainActivity.maxHeight?.times(0.05f)?.value ?: 50f,
            waveLength = endX - startX
        )

        // Second wave line (higher position)
        DrawWaveLine(
            modifier = Modifier.fillMaxWidth().align(BiasAlignment(0f, 0.83f)),
            screenWidth = screenWidth,
            waveHeight = MainActivity.maxHeight?.times(0.05f)?.value ?: 50f,
            waveOffset = 25f, // Offset to appear above the ball
            color = Color.Black
        )
    }
}*/

@Composable
fun DrawWaveLine(
    modifier: Modifier,
    screenWidth: Float,
    waveHeight: Float,
    waveOffset: Float,
    color: Color
) {
    Canvas(modifier = modifier.height(MainActivity.maxHeight?.times(0.1f) ?: 50.dp)) {
        val path = androidx.compose.ui.graphics.Path()
        val waveLength = screenWidth // Adjusted wavelength to control wave frequency

        // Start drawing from the beginning of the screen
        path.moveTo(0f, 0f)

        for (x in 0..screenWidth.toInt()) {
            // Calculate the y-coordinate for each x value
            val progress = (((MainActivity.maxWidth?.value?:100f) * (2 * PI) - PI / 2)).toFloat()

            // Invert the sine wave to flip it upside down
            val yOffset = waveHeight * (sin(progress) + 1f) / 2f
            val y = waveHeight * (sin(progress) + 1f) / 2f/*waveHeight * sin((2 * PI * x) / waveLength).toFloat() + waveOffset*/
            path.lineTo(x.toFloat(), y)
        }

        // Draw the completed wave line across the screen width
        drawPath(path = path, color = color, style = Stroke(width = 5.dp.toPx()))
    }
}


@Composable
fun WaveMovingObject(
    modifier: Modifier = Modifier,
    xOffset: Float, // Animated X value
    waveHeight: Float, // Wave amplitude
    waveLength: Float // Wavelength of the wave pattern
) {
    // Calculate progress along the sine wave, flipped to be upside down
    val progress = ((xOffset / waveLength) * (2 * PI) - PI / 2).toFloat()

    // Invert the sine wave to flip it upside down
    val yOffset = waveHeight * (sin(progress) + 1f) / 2f

    // Move the circle along the inverted wave path
    Canvas(modifier = modifier.offset(x = xOffset.dp, y = yOffset.dp)) {
        drawCircle(color = WhiteText) // Draw the circle following the wave
    }
}

@Composable
fun WaveRailMovingObject(
    modifier: Modifier = Modifier,
    xOffset: Float, // Current X position
    waveHeight: Float, // Wave amplitude (how tall the wave is)
    waveLength: Float, // Length of one wave
    pathColor: Color = Color(0xFFB39DDB) // Light purple for the rail
) {
    // Predefine the wave path
    val path = androidx.compose.ui.graphics.Path().apply {
        // Move to the starting point of the path
        // Arbitrary large width
        val progress = ((xOffset / waveLength) * (2 * PI) - PI / 2).toFloat()
        val y = waveHeight * (sin(progress) + 1f) / 2f
        moveTo(1f, y)

        // Create a series of sine wave curves along the X-axis
        for (x in 1..2000/*(MainActivity.maxWidth?.value?.toInt() ?:2000)*/) { // Arbitrary large width
            val progress = ((x / waveLength) * (2 * PI) - PI / 2).toFloat()
            val y = waveHeight * (sin(progress) + 1f) / 2f
            lineTo(x.toFloat(), y)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        // Draw the rail (wave path)
        drawPath(
            path = path,
            color = pathColor,
            style = Stroke(width = 8f) // Make the rail path visible
        )

        // Calculate the current Y position of the ball on the rail
        val progress = ((xOffset / waveLength) * (2 * PI) - PI / 2).toFloat()
        val yOffset = waveHeight * (sin(progress) + 1f) / 2f

        // Draw the ball on the rail at the correct position
        drawCircle(
            color = Color.White,
            radius = 10.dp.toPx(),
            center = Offset(xOffset, yOffset)
        )
    }
}

@Composable
fun WaveRailAnimationScreen() {
    // Get the screen width (or fallback to default)
    val screenWidth = MainActivity.maxWidth?.value ?: 100f

    // Calculate start and end X positions for animation
    val startX = screenWidth * 0.07f
    val endX = screenWidth * 0.77f

    // Infinite transition to animate X position back and forth
    val infiniteTransition = rememberInfiniteTransition()
    val xOffset by infiniteTransition.animateFloat(
        initialValue = startX,
        targetValue = endX,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = EaseInOutQuad
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        WaveRailMovingObject(
            modifier = Modifier.align(Alignment.Center),
            xOffset = xOffset,
            waveHeight = MainActivity.maxHeight?.times(0.15f)?.value ?: 50f,
            waveLength = (endX - startX)
        )
    }
}

@Composable
fun InfiniteLoadingAnimation(modifier: Modifier) {

    var sizeState1 by remember { mutableStateOf(1.dp/*UiConstants.maxHeight?.times(0.05f)?: 32.dp*/) }

    // Define the animation specs
    val size1 = animateDpAsState(
        targetValue = sizeState1,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )


    var sizeState2 by remember { mutableStateOf(1.dp/*UiConstants.maxHeight?.times(0.05f)?: 32.dp*/) }

    // Define the animation specs
    val size2 = animateDpAsState(
        targetValue = sizeState2,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )

    var sizeState3 by remember { mutableStateOf(1.dp/*UiConstants.maxHeight?.times(0.05f)?: 32.dp*/) }

    // Define the animation specs
    val size3 = animateDpAsState(
        targetValue = sizeState3,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
    )

    // Switch size between 32.dp and 48.dp every 500 milliseconds
    LaunchedEffect(Unit) {
        while (true) {
            sizeState1 = MainActivity.maxHeight?.times(0.012f)?: 32.dp
            delay(400)
            sizeState2 = MainActivity.maxHeight?.times(0.015f)?: 48.dp
            delay(400)
            sizeState3 = MainActivity.maxHeight?.times(0.02f)?: 60.dp
            sizeState1 = MainActivity.maxHeight?.times(0.01f)?: 16.dp
            delay(400)
            sizeState2 = MainActivity.maxHeight?.times(0.01f)?: 16.dp
            delay(400)
            sizeState3 = MainActivity.maxHeight?.times(0.01f)?: 16.dp
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.2f)
            .fillMaxHeight(0.05f),
        contentAlignment = Alignment.BottomCenter
    ) {
        val alignment1: Alignment = BiasAlignment(-0.5f,0.1f)
        val alignment2: Alignment = BiasAlignment(-0f,0.1f)
        val alignment3: Alignment = BiasAlignment(0.5f,0.1f)
        Box( modifier = Modifier
            .fillMaxSize()
            .align(Alignment.BottomCenter)){
            Spacer(modifier = Modifier.fillMaxWidth(0.125f))
            Image(painter = painterResource(id = R.drawable.loading_anim_item), contentDescription = "", modifier = Modifier
                .size(
                    size1.value
                )
                .align(alignment1))
            Image(painter = painterResource(id = R.drawable.loading_anim_item), contentDescription = "", modifier = Modifier
                .size(
                    size2.value
                )
                .align(alignment2))
            Image(painter = painterResource(id = R.drawable.loading_anim_item), contentDescription = "", modifier = Modifier
                .size(
                    size3.value
                )
                .align(alignment3))
        }
    }
}

@Composable
fun WaveAnimationScreen() {
    // Get the screen width from MainActivity or use a fallback default value
    val screenWidth = MainActivity.maxWidth?.value ?: 100f

    // Calculate start and end X positions (10% to 80% of screen width)
    val startX = screenWidth * 0.07f
    val endX = screenWidth * 0.77f

    // Infinite transition for smooth back-and-forth motion with pauses at peaks
    val infiniteTransition = rememberInfiniteTransition()
    val xOffset by infiniteTransition.animateFloat(
        initialValue = startX,
        targetValue = endX,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = EaseInOutQuad // Smooth easing with slow peaks
            ),
            repeatMode = RepeatMode.Reverse // Move back and forth smoothly
        )
    )

    // Calculate the wave height
    val waveHeight = MainActivity.maxHeight?.times(0.05f)?.value ?: 50f

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {/*
        // Draw the top and bottom "rails"
        DividerLine(yOffset = 0.7f - (waveHeight / 2) / (MainActivity.maxHeight?.value ?: 1f))
        DividerLine(yOffset = 0.7f + (waveHeight / 2) / (MainActivity.maxHeight?.value ?: 1f))
*/

        Image(painterResource(id =R.drawable.waveline2), contentDescription = "", contentScale = ContentScale.FillWidth,modifier = Modifier.fillMaxWidth()
            .align(BiasAlignment(-0.6f, 0.92f)))
        // Moving object (ball) between the rails
        WaveMovingObject(
            modifier = Modifier
                .size(40.dp)
                .align(BiasAlignment(-1f, if(MainActivity.isBiggerThen700dp==true)0.7f else 0.65f)), // Initial position along the wave path
            xOffset = xOffset,
            waveHeight = waveHeight,
            waveLength = endX - startX
        )
        Image(painterResource(id =R.drawable.waveline2), contentDescription = "", contentScale = ContentScale.FillWidth,modifier = Modifier.fillMaxWidth()
            .align(BiasAlignment(-0.6f, 0.96f)))
        Image(painterResource(id =R.drawable.waveline2), contentDescription = "", contentScale = ContentScale.FillWidth,modifier = Modifier.fillMaxWidth()
            .align(BiasAlignment(-0.6f, 1f)))
    }
}

@Composable
fun DividerLine(yOffset: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp) // Thin line representing the rail
            .background(Color.Gray)
           /* .align(BiasAlignment(0f, yOffset)) // Position the line with BiasAlignment*/
    )
}


