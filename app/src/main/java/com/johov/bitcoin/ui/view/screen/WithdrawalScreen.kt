package com.johov.bitcoin.ui.view.screen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.data.model.enums.CONNECTION_ERROR_ENUM
import com.johov.bitcoin.data.model.Transaction
import com.johov.bitcoin.ui.theme.AlmostTransparent
import com.johov.bitcoin.ui.theme.AltBG
import com.johov.bitcoin.ui.theme.InvalidOrange
import com.johov.bitcoin.ui.theme.NewBlack
import com.johov.bitcoin.ui.theme.NotUsedRed
import com.johov.bitcoin.ui.theme.SideTextColor
import com.johov.bitcoin.ui.theme.UsedGreen
import com.johov.bitcoin.ui.theme.Violet
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.WithdrawalTextColor
import com.johov.bitcoin.ui.theme.average_text_size
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.ui.view.composables.AnimatedTimeUnit
import com.johov.bitcoin.ui.view.composables.BalanceBar
import com.johov.bitcoin.ui.view.composables.InActiveBackground
import com.johov.bitcoin.ui.view.composables.timersp
import com.johov.bitcoin.viewmodel.MainViewModel
import com.johov.bitcoin.viewmodel.WithdrawalViewModel
import java.util.Locale
@Composable
fun WithdrawalScreen(
    viewModel: WithdrawalViewModel,
    mainViewModel: MainViewModel,
    onLaunch: () -> Unit,
    checkup: () -> Unit
){
        if(showPopupRate == true||showPopupRate==null){//new
            PopupDialog(viewModel)
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        TransactionHistory(
            hasShownFirstWithdrawal = hasShownFirstWithdrawal, transactionList!!, hasWorked,
            onClick = viewModel::copyCodeOnClick)

        if(!transactionList.isNullOrEmpty()){
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            WithdrawalScreenEnd(){
                viewModel.googlePlayCodeOnClick()
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
        }
        Box(Modifier.fillMaxSize()){

            Column(
                Modifier
                    .fillMaxWidth()
                    .align(
                        if (!transactionList.isNullOrEmpty()) Alignment.TopCenter else BiasAlignment(
                            0f,
                            -0.2f
                        )
                    ), horizontalAlignment = Alignment.CenterHorizontally/*, verticalArrangement = Arrangement.spacedBy(12.dp)*/) {

                TimerText(cooldownMilis,canWithdraw!!)
                Spacer(modifier = Modifier.fillMaxHeight(0.01f))
                InActiveBackground(true,{
                    MainScope().launch {
                        hasWorked.value = false
                        hasShownFirstWithdrawal.value = false
                        viewModel.withdrawalOnClick()
                    }},null,"Withdraw", isOrange = true, shouldBeActive = canWithdraw!!)


            }

        }
}
}

@Composable
private fun PopupDialog(viewModel: WithdrawalViewModel) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { viewModel.dismissRateScreen() }) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .paint(
                    painter = painterResource(id = R.drawable.popup_rate_bg),
                    contentScale = ContentScale.FillWidth
                )
        ) {
            Column(Modifier.align(Alignment.Center)) {


                Spacer(Modifier.fillMaxHeight(0.11f))
                Column(
                    Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "If you like our app, please rate it",
                        fontSize = 16.sp,
                        color = Violet,
                        textAlign = TextAlign.Center,
                        fontFamily = itimStyle,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                    var listOfStars = remember {
                        listOf(
                            mutableStateOf(false),
                            mutableStateOf(false),
                            mutableStateOf(false),
                            mutableStateOf(false),
                            mutableStateOf(false)
                        )
                    }

                    val scope = rememberCoroutineScope()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(painter = painterResource(id = if (listOfStars[0].value) R.drawable.popup_star_active else R.drawable.popup_star_inactive),
                            "",
                            modifier = Modifier
                                .size(28.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    listOfStars[0].value = true

                                    scope.launch {
                                        delay(500L)
                                        viewModel.dismissRateScreenAndNavigate()
                                    }
                                }
                        )
                        Image(painter = painterResource(id = if (listOfStars[1].value) R.drawable.popup_star_active else R.drawable.popup_star_inactive),
                            "",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    listOfStars[0].value = true
                                    listOfStars[1].value = true

                                    scope.launch {
                                        delay(500L)
                                        viewModel.dismissRateScreenAndNavigate()
                                    }
                                }
                                .size(28.dp)
                        )
                        Image(painter = painterResource(id = if (listOfStars[2].value) R.drawable.popup_star_active else R.drawable.popup_star_inactive),
                            "",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    listOfStars[0].value = true
                                    listOfStars[1].value = true
                                    listOfStars[2].value = true

                                    scope.launch {
                                        delay(500L)
                                        viewModel.dismissRateScreenAndNavigate()
                                    }
                                }
                                .size(28.dp)
                        )
                        Image(painter = painterResource(id = if (listOfStars[3].value) R.drawable.popup_star_active else R.drawable.popup_star_inactive),
                            "",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    listOfStars[0].value = true
                                    listOfStars[1].value = true
                                    listOfStars[2].value = true
                                    listOfStars[3].value = true

                                    scope.launch {
                                        delay(500L)
                                        viewModel.dismissRateScreenAndNavigate()
                                    }
                                }
                                .size(28.dp)
                        )
                        Image(painter = painterResource(id = if (listOfStars[4].value) R.drawable.popup_star_active else R.drawable.popup_star_inactive),
                            "",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    listOfStars[0].value = true
                                    listOfStars[1].value = true
                                    listOfStars[2].value = true
                                    listOfStars[3].value = true
                                    listOfStars[4].value = true

                                    scope.launch {
                                        delay(500L)
                                        viewModel.dismissRateScreenAndNavigate()
                                    }
                                }
                                .size(28.dp)
                        )
                    }
                }
            }
            Image(painter = painterResource(id = R.drawable.popup_rate_exit_icon),
                "",
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.dismissRateScreen() }
                    .padding(top = 5.dp, end = 5.dp)
                    .align(Alignment.TopEnd))
        }
    }
}

@Composable
fun TimerText(cooldownMilis: Long?, canWithdraw: Boolean){
        if(cooldownMilis==null||canWithdraw){

            Text(
                text = "Withdrawal is available",
                color = WithdrawalTextColor,
                fontFamily = itimStyle,
                fontSize = average_text_size,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
        else{

            val hours = ((cooldownMilis/1000) / 3600).toInt()
            val minutes = (((cooldownMilis/1000) / 60)%60).toInt()
            val seconds = ((cooldownMilis/1000) % 60).toInt()

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text(
                    text = "Withdrawal will be available after ",
                    color = WhiteText,
                    fontFamily = itimStyle,
                    fontSize = average_text_size,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
                Row {
                    AnimatedTimeUnit(timeUnit = hours / 10, SideTextColor) // Tens place of minutes
                    AnimatedTimeUnit(timeUnit = hours % 10,SideTextColor) // Tens place of minutes
                    Text(//usdt
                        text = ":",
                        color = SideTextColor,
                        textAlign = TextAlign.Center,fontFamily = krub_sb,
                        fontSize = timersp
                    )
                    AnimatedTimeUnit(timeUnit = minutes / 10,SideTextColor) // Tens place of minutes
                    AnimatedTimeUnit(timeUnit = minutes % 10,SideTextColor) // Units place of minutes

                    Text(//usdt
                        text = ":",
                        color = SideTextColor,
                        textAlign = TextAlign.Center,fontFamily = krub_sb,
                        fontSize = timersp
                    )
                    AnimatedTimeUnit(timeUnit = seconds / 10,SideTextColor) // Tens place of seconds
                    AnimatedTimeUnit(timeUnit = seconds % 10,SideTextColor) // Units place of seconds
                }
            }
        }

}
@Composable
fun WithdrawalScreenEnd(onClick: () -> Unit){
    Box (modifier = Modifier
        .fillMaxWidth()){

        Image(painter = painterResource(id = R.drawable.google_play_outside), contentDescription = "", contentScale = ContentScale.FillWidth, modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center))
        Column(
            Modifier
                .fillMaxWidth()
                .align(BiasAlignment(0f, -0.5f))){

            var imageSize by remember { mutableStateOf(0.15f) }
            val animatedImageSize by animateFloatAsState(
                targetValue = imageSize,
                animationSpec = tween(durationMillis = 300)
            )
            Text(
                text = "Codes can be used in Supported Wallets".uppercase(Locale.ROOT),
                fontSize = if(MainActivity.isBiggerThen700dp==true||MainActivity?.maxWidth?:30.dp>400.dp)13.sp else 12.sp,
                color = Violet,
                fontFamily = itimStyle,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = MainActivity.maxWidth?.times(0.05f) ?: 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Box(
                Modifier
                    .fillMaxWidth(0.8f / 2)
                    .align(Alignment.CenterHorizontally)
                    .height(MainActivity.maxHeight?.times(0.05f) ?: 80.dp)){

                Image(painter = painterResource(id = R.drawable.get_it_on_google_play_icon), contentDescription = "", contentScale = ContentScale.FillWidth, modifier = Modifier
                    .fillMaxWidth(1f - (animatedImageSize / 2))
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        MainScope().launch {
                            imageSize = 0.4f
                            delay(300)
                            imageSize = 0.15f
                            onClick()
                        }
                    })
            }
/*
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
*/
        }
    }
}

@Composable
fun TransactionHistory(hasShownFirstWithdrawal: MutableState<Boolean>, transList: List<Transaction>, hasWorked:MutableState<Boolean>, onClick: (String)->Unit){
    val list = transList
    val roundUp = 24.dp
    val cornerRadius = if (MainActivity.isBiggerThen700dp==false)40f  else 40f + ((MainActivity.maxHeight?.minus(640.dp)?.value?.div(6f)) ?: 0f)

    Box(
        modifier = Modifier
            .fillMaxHeight(0.45f)){
        Image(painterResource(id = R.drawable.big_black_background),"", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(MainActivity.maxHeight?.times(0.005f)?:32.dp)) {

                    Box(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(end = MainActivity.maxWidth?.times(0.05f) ?: 32.dp)){

                            Image(
                                painter = painterResource(id = R.drawable.history_of_transactions_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.CenterEnd)
                            )
                        }


                        Text(
                            text = "History of Transactions:",
                            fontFamily = itimStyle,
                            fontSize = 20.sp,
                            color = Violet,
                            modifier = Modifier
                                .align(
                                    Alignment.CenterStart
                                )
                                .padding(start = MainActivity.maxWidth?.times(0.05f) ?: 32.dp)
                        )
                    }
/*
                    DashedLine(Modifier.padding(horizontal = 8.dp))
*/
                }
            }
            if(!list.isNullOrEmpty()){
                LazyColumn(modifier = Modifier.fillMaxHeight(0.96f).clip(RoundedCornerShape(24.dp)), verticalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(0.03f)?:32.dp)){
/*                items(list.size){
                    LongWithdrawal(hasShownFirstWithdrawal,trans = list!![it],  it,list!!.size-1,hasWorked, {onClick(it)})
                }*/
                    item{
                        LongWithdrawal(hasShownFirstWithdrawal,list!![0],0,  list!!.size-1,hasWorked) { onClick(it) }
                    }
                    if(list!!.size>1){
                        Log.d("UITESTS",list!!.size.toString())
                        for(trans in 1..<list!!.size){
                            item{
                                LongWithdrawal(hasShownFirstWithdrawal,list!![trans],trans, list!!.size-1,hasWorked) { onClick(it) }
                            }
                        }
                    }
                }
            }
            if(list!!.isEmpty()){
                Column(Modifier) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.12f))
                    Image(
                        painter = painterResource(id = R.drawable.no_transaction_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(MainActivity.maxHeight?.times(0.05f) ?: 80.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                    Row(modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        Text(text ="The first transaction will appear\n" +
                                "when you make a withdrawal",
                            fontSize = 16.sp,
                            fontFamily = itimStyle,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF4E4E6C),
                            modifier = Modifier
                                .align(
                                    Alignment.CenterVertically
                                ))
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(if(MainActivity.isBiggerThen700dp == true) 0.2f else 0f))
                }
            }
        }
    }

}

@Composable
fun LongWithdrawal(hasShownFirstWithdrawal: MutableState<Boolean> = mutableStateOf(false), trans: Transaction, index: Int, size: Int, hasWorked: MutableState<Boolean>, onClick: (String)->Unit){
    fun getMoneyText(sum: Float):String{
        val formattedValue = String.format("%.2f", sum)
        return formattedValue.toString()+" USDT"
    }
    var isTheLastOne: Boolean by remember {
        Log.d("triggered","index "+ index + " size " + size)
        mutableStateOf(index!=0)
    }
    val colorList = listOf(NotUsedRed, UsedGreen,InvalidOrange)
    var bgcolor = if (isTheLastOne||hasWorked.value) AlmostTransparent else AlmostTransparent
    var bgcolor2 = if (isTheLastOne||hasWorked.value) colorList[trans.status] else AlmostTransparent
    var bdcolor = if (isTheLastOne||hasWorked.value) AltBG else AlmostTransparent
    var textcolor = if (isTheLastOne||hasWorked.value) NewBlack else AlmostTransparent
    var text3color = if (isTheLastOne||hasWorked.value) Color(0xFF5745C6) else AlmostTransparent
    var text2color = if (isTheLastOne||hasWorked.value) WhiteText else AlmostTransparent
    var alphaAnim = if (isTheLastOne||hasWorked.value) 1F else 0f
    val animDuration = 600
    val animatedColorBG2 by animateColorAsState(
        targetValue = bgcolor2,
        animationSpec = tween(durationMillis = animDuration)
    )
    val animatedColorText by animateColorAsState(
        targetValue = textcolor,
        animationSpec = tween(durationMillis = animDuration)
    )
    val animatedColorText2 by animateColorAsState(
        targetValue = text2color,
        animationSpec = tween(durationMillis = animDuration)
    )
    val animatedColorText3 by animateColorAsState(
        targetValue = text3color,
        animationSpec = tween(durationMillis = animDuration)
    )
    val animatedAlphaAnim by animateFloatAsState(
        targetValue = alphaAnim,
        animationSpec = tween(durationMillis = animDuration)
    )
    val textList = listOf("Not Used","Used","Invalid")
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
          Image(
            painterResource(id = R.drawable.transaction_bg),"",alpha = animatedAlphaAnim, modifier = Modifier
                  .align(Alignment.Center)
                  .height(MainActivity.maxHeight?.times(0.09f) ?: 54.dp)
                  .fillMaxWidth(0.9f), contentScale = ContentScale.FillBounds)
        Column (modifier = Modifier.fillMaxWidth()){
            if(true){
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                    .fillMaxWidth(if (MainActivity.isBiggerThen700dp == true) 0.85f else 0.95f)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxHeight(if (MainActivity.isBiggerThen700dp == true) 0.4f else 0.4f)){

                    Text(
                        text = "Amount",
                        color = animatedColorText2,
                        textAlign = TextAlign.Center,fontFamily = itimStyle,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Code",
                        color = animatedColorText2,
                        textAlign = TextAlign.Center,fontFamily = itimStyle,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Status",
                        color = animatedColorText2,
                        textAlign = TextAlign.Center,fontFamily = itimStyle,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            Spacer(Modifier.height(MainActivity.maxHeight?.times(0.015f)?:54.dp))


                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
                    .onGloballyPositioned {
                        if (index == 0 && !hasShownFirstWithdrawal.value && !hasWorked.value) {
                            hasWorked.value = true
                            hasShownFirstWithdrawal.value = true
                            MainScope().launch {
                                isTheLastOne = false
                                delay(500)
                                isTheLastOne = true
                            }
                        }
                    }) {
                    Box(modifier = Modifier
                        .weight(1.2F)
                        .fillMaxHeight()){

                        Text(//usdt
                            text = getMoneyText(trans.value),
                            color = animatedColorText,
                            textAlign = TextAlign.Center,fontFamily = krub_sb,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }


                    Row(//copyRow
                        modifier = Modifier
                            .weight(1.5F)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { onClick(trans.promocode) }, horizontalArrangement = Arrangement.Center
                    ){
                        var shakeOffset by remember { mutableStateOf(0f) }
                        val animatedImageSize by animateFloatAsState(
                            targetValue = shakeOffset,
                            animationSpec = tween(durationMillis = 100)
                        )
                        LaunchedEffect(true) {
                            for(i in 0..5){

                                shakeOffset = 12f
                                delay(100)
                                shakeOffset = 0f
                                delay(100)
                            }
                        }
                        val modifier = if(trans.status==0)Modifier
                            .graphicsLayer {
                                translationX = animatedImageSize
                            } else Modifier
                        Text(trans.promocode,color = animatedColorText3, fontFamily = krub_sb,fontSize = 16.sp,  textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.CenterVertically))
                        Image(
                            painterResource(id = R.drawable.copy_code_icon),"",alpha = animatedAlphaAnim,modifier = modifier
                                .size(16.dp)
                                .align(Alignment.CenterVertically))

                    }

                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()){

                        Text(//status
                            text = textList[trans.status],
                            color = animatedColorBG2,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center),
                            fontFamily = krub_sb,
                        )
                    }

                }
            }


 }
}