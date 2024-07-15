package silmex.apps.airdropcryptopoints.ui.view.screen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.data.model.Transaction
import silmex.apps.airdropcryptopoints.ui.theme.AlmostTransparent
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.ButtonTextColor
import silmex.apps.airdropcryptopoints.ui.theme.InvalidOrange
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.NotUsedRed
import silmex.apps.airdropcryptopoints.ui.theme.OffTextColor
import silmex.apps.airdropcryptopoints.ui.theme.UsedGreen
import silmex.apps.airdropcryptopoints.ui.theme.WithdrawalTextColor
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.view.composables.BalanceBar
import silmex.apps.airdropcryptopoints.ui.view.composables.DashedLine
import silmex.apps.airdropcryptopoints.ui.view.composables.InActiveBackground
import silmex.apps.airdropcryptopoints.viewmodel.MainViewModel
import silmex.apps.airdropcryptopoints.viewmodel.WithdrawalViewModel
import java.util.Locale

@Composable
fun WithdrawalScreen(viewModel: WithdrawalViewModel,mainViewModel: MainViewModel, onLaunch: ()->Unit){
    val balance by viewModel.balance.observeAsState()
    val isMining by viewModel.isMining.observeAsState()
    val coins by mainViewModel.coins.observeAsState()
    val currentBoost by mainViewModel.currentChosenMultipliyer.observeAsState()
    val transactionList by viewModel.transactionList.observeAsState()
    val uriGooglePlay by viewModel.mainDataRepository.urlGooglePlay.observeAsState()
    val progress by viewModel.progress.observeAsState()

    LaunchedEffect(true) {
        onLaunch()
    }

    var hasShownFirstWithdrawal = remember {
        mutableStateOf(false)
    }
    var hasWorked: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        hasWorked.value = false
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BalanceBar(balance!!,progress!!, isMining!!,coins!!,currentBoost!!.value, mainViewModel::removeCoin)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text(
                text = "Withdrawal will be available after a day",
                color = MainTextColor,
                fontFamily = itimStyle,
                fontSize = average_text_size,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )

            InActiveBackground(true,{
            MainScope().launch {
                hasWorked.value = false
                hasShownFirstWithdrawal.value = false
                viewModel.withdrawalOnClick()
            }},null,"Withdraw")
        }


        TransactionHistory(
            hasShownFirstWithdrawal = hasShownFirstWithdrawal, transactionList!!, hasWorked,
            onClick = viewModel::copyCodeOnClick)

        val uriHandler = LocalUriHandler.current
        WithdrawalScreenEnd(){
            uriHandler.openUri(uriGooglePlay!!)
        }
    }
}

@Composable
fun WithdrawalScreenEnd(onClick: () -> Unit){
    Column (verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()){

        var imageSize by remember { mutableStateOf(0.85f) }
        val animatedImageSize by animateFloatAsState(
            targetValue = imageSize,
            animationSpec = tween(durationMillis = 300)
        )
        Text(
            text = "Codes can be used in Supported Wallets".uppercase(Locale.ROOT),
            fontSize = 14.sp,
            color = MainTextColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Image(painter = painterResource(id = R.drawable.get_it_on_google_play_icon), contentDescription = "", contentScale = ContentScale.FillWidth, modifier = Modifier
            .fillMaxWidth(animatedImageSize / 2)
            .align(Alignment.CenterHorizontally)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                MainScope().launch {
                    imageSize = 0.6f
                    delay(300)
                    imageSize = 0.85f
                    onClick()
                }
            })
    }
}

@Composable
fun TransactionHistory(hasShownFirstWithdrawal: MutableState<Boolean>, transList: List<Transaction>, hasWorked:MutableState<Boolean>, onClick: (String)->Unit){
    val list = transList
    fun getMoneyText(sum: Float):String{
        val formattedValue = String.format("%.2f", sum)
        return formattedValue.toString()+" USDT"
    }
    val roundUp = 24.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AltBG, shape = RoundedCornerShape(roundUp))
            .border(1.dp, ButtonTextColor, shape = RoundedCornerShape(roundUp))
            .clip(RoundedCornerShape(roundUp))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(AltBG, shape = RoundedCornerShape(roundUp))
                .border(1.dp, ButtonTextColor, shape = RoundedCornerShape(roundUp))
        ) {
            Box(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.history_of_transactions_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterStart)
                )


                Text(
                    text = "History of Transactions",
                    fontFamily = itimStyle,
                    fontSize = 20.sp,
                    color = MainTextColor,
                    modifier = Modifier
                        .align(
                            Alignment.Center
                        )
                        .padding(start = 16.dp)
                )
            }
        }
        if(!list.isNullOrEmpty()){
            LazyColumn(modifier = Modifier.fillMaxHeight(0.4F)){
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
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Row(modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text(text = "Here will be your withdrawals",
                    fontSize = 16.sp,
                    color = OffTextColor,
                    modifier = Modifier
                        .align(
                            Alignment.CenterVertically
                        ))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
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
    var textcolor = if (isTheLastOne||hasWorked.value) WithdrawalTextColor else AlmostTransparent
    var text2color = if (isTheLastOne||hasWorked.value) OffTextColor else AlmostTransparent
    var alphaAnim = if (isTheLastOne||hasWorked.value) 1F else 0f
    val animDuration = 600
    val animatedColorBG by animateColorAsState(
        targetValue = bgcolor,
        animationSpec = tween(durationMillis = animDuration)
    )
    val animatedColorBG2 by animateColorAsState(
        targetValue = bgcolor2,
        animationSpec = tween(durationMillis = animDuration)
    )
    val animtaedBorderColor by animateColorAsState(
        targetValue = bdcolor,
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
    val animatedAlphaAnim by animateFloatAsState(
        targetValue = alphaAnim,
        animationSpec = tween(durationMillis = animDuration)
    )
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val textList = listOf("Not Used","Used","Invalid")
    val configuration = LocalConfiguration.current
    Column (modifier = Modifier.fillMaxWidth()){
        if(index==0){
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)){

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

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
            }
            .height(60.dp)) {
            Box(modifier = Modifier
                .weight(1.2F)
                .fillMaxHeight()){

                Text(//usdt
                    text = getMoneyText(trans.value),
                    color = animatedColorText,
                    textAlign = TextAlign.Center,fontFamily = itimStyle,
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
                Text(trans.promocode,color = animatedColorText, fontFamily = itimStyle,fontSize = 16.sp,  textAlign = TextAlign.Center, modifier = Modifier.align(Alignment.CenterVertically))
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
                    fontFamily = itimStyle,
                )
            }
            /*
                    Row(horizontalArrangement = Arrangement.Center,modifier=
                    Modifier
                        .weight(1.2F)
                        .fillMaxHeight()
                        .border(1.dp, animtaedBorderColor, shape = RoundedCornerShape(16.dp))
                        .background(animatedColorBG, shape = RoundedCornerShape(16.dp))){
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Amount",
                                color = animatedColorText2,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Code",
                                color = animatedColorText2,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween, modifier =
                    Modifier
                        .weight(1.3F)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            clipboardManager.setText(AnnotatedString(trans.promocode))
                            onClick()
                        }
                        .background(animatedColorBG, shape = RoundedCornerShape(16.dp))){
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween,modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)) {
                            Text(
                                text = getMoneyText(trans.value),
                                color = animatedColorText,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )

                        }

                    }
                    Row(horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                            .border(
                                1.dp,
                                animatedColorBG2,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .background(animatedColorBG, shape = RoundedCornerShape(16.dp))){
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = textList[trans.status],
                                color = animatedColorBG2,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                            )
                        }
                    }*/

        }
        if(index!=size)
            DashedLine()
    }
}