package silmex.apps.airdropcryptopoints.ui.view.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.data.model.Screen
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.OffTextColor
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size_f
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle

@Composable
fun NavigationBottomBar(modifier: Modifier, selected: Screen, onHomeChoose: ()->Unit, onRefferalsChoose: ()->Unit, onWithdrawalChoose: ()->Unit,){


    var paddingValues1 by remember { mutableStateOf(0f) }

    val animatedPaddingValues1 by animateFloatAsState(
        targetValue = paddingValues1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var paddingFontValues1 by remember { mutableStateOf(0f) }

    val animatedPaddingFontValues1 by animateFloatAsState(
        targetValue = paddingValues1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    var paddingValues2 by remember { mutableStateOf(0f) }

    val animatedPaddingValues2 by animateFloatAsState(
        targetValue = paddingValues2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var paddingFontValues2 by remember { mutableStateOf(0f) }

    val animatedPaddingFontValues2 by animateFloatAsState(
        targetValue = paddingValues2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    var paddingValues3 by remember { mutableStateOf(0f) }

    val animatedPaddingValues3 by animateFloatAsState(
        targetValue = paddingValues3,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var paddingFontValues3 by remember { mutableStateOf(0f) }

    val animatedPaddingFontValues3 by animateFloatAsState(
        targetValue = paddingValues3,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    Box(
        modifier
            .fillMaxWidth()
            .background(AltBG)
            .fillMaxHeight(0.1f)){
        Row(modifier = Modifier.fillMaxWidth(0.8f).align(Alignment.Center), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Box(modifier = Modifier.align(
                Alignment.CenterVertically
            )){

                Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.align(
                    Alignment.Center)
                    .clickable(interactionSource = remember{ MutableInteractionSource() }, indication = null,) {

                        MainScope().launch {
                            paddingValues1 = 6f
                            paddingFontValues1 = 1f
                            delay(300)
                            paddingValues1 = 0f
                            paddingFontValues1 = 0f
                            onHomeChoose()
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally){
                    Image(painter = painterResource(id = if(selected== Screen.HomeScreen) R.drawable.home_active_icon else R.drawable.home_inactive_icon),"", modifier = Modifier.size((32-animatedPaddingValues1).dp).align(Alignment.CenterHorizontally))
                    Text("Home", fontSize = (average_text_size_f - animatedPaddingFontValues1).sp,color =  if(selected== Screen.HomeScreen) SideTextColor else OffTextColor, fontFamily = itimStyle)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp),modifier = Modifier.align(
                Alignment.CenterVertically
            )
                .clickable(interactionSource = remember{ MutableInteractionSource() }, indication = null,) {
                    MainScope().launch {
                        paddingValues2 = 6f
                        paddingFontValues2 = 1f
                        delay(300)
                        paddingValues2 = 0f
                        paddingFontValues2 = 0f
                        onRefferalsChoose()
                    } }, horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = painterResource(id = if(selected== Screen.RefferalsScreen) R.drawable.referrals_active_icon else R.drawable.referrals_inactive_icon),"", modifier = Modifier.size((32-animatedPaddingValues2).dp).align(Alignment.CenterHorizontally))
                Text("Referral", fontSize =  (average_text_size_f - animatedPaddingFontValues2).sp,color =  if(selected== Screen.RefferalsScreen) SideTextColor else OffTextColor, fontFamily = itimStyle)
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp),modifier = Modifier.align(
                Alignment.CenterVertically
            )
                .clickable(interactionSource = remember{ MutableInteractionSource() }, indication = null,) {
                    MainScope().launch {
                        paddingValues3 = 6f
                        paddingFontValues3 = 1f
                        delay(300)
                        paddingValues3 = 0f
                        paddingFontValues3 = 0f
                        onWithdrawalChoose()
                    } }, horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = painterResource(id = if(selected== Screen.WithdrawalScreen) R.drawable.wallet_active_icon else R.drawable.wallet_inactive_icon),"", modifier = Modifier.size((32-animatedPaddingValues3).dp).align(Alignment.CenterHorizontally))
                Text("Wallet", fontSize =  (average_text_size_f - animatedPaddingFontValues3).sp,color =  if(selected== Screen.WithdrawalScreen) SideTextColor else OffTextColor, fontFamily = itimStyle)
            }
        }
    }
}