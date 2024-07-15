package silmex.apps.airdropcryptopoints.ui.view.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import silmex.apps.airdropcryptopoints.data.interfaces.UpgradeWheelCallBack
import silmex.apps.airdropcryptopoints.data.model.Screen
import silmex.apps.airdropcryptopoints.data.repository.UnityAdsRepository
import silmex.apps.airdropcryptopoints.ui.theme.MainBG
import silmex.apps.airdropcryptopoints.ui.view.screen.HomeScreen
import silmex.apps.airdropcryptopoints.ui.view.screen.LearningScreen1
import silmex.apps.airdropcryptopoints.ui.view.screen.LearningScreen2
import silmex.apps.airdropcryptopoints.ui.view.screen.LearningScreen3
import silmex.apps.airdropcryptopoints.ui.view.screen.RefferalsScreen
import silmex.apps.airdropcryptopoints.ui.view.screen.SplashScreen
import silmex.apps.airdropcryptopoints.ui.view.screen.WithdrawalScreen
import silmex.apps.airdropcryptopoints.utils.TagUtils
import silmex.apps.airdropcryptopoints.viewmodel.HomeViewModel
import silmex.apps.airdropcryptopoints.viewmodel.MainViewModel
import silmex.apps.airdropcryptopoints.viewmodel.RefferralViewModel
import silmex.apps.airdropcryptopoints.viewmodel.WithdrawalViewModel

@Composable
fun Navigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val didShowLearning by mainViewModel.didShowLearningScreen.observeAsState()
    var selected by remember{ mutableStateOf (Screen.SplashScreen) }

    val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
    val referralViewModel: RefferralViewModel = hiltViewModel<RefferralViewModel>()
    val withdrawalViewModel: WithdrawalViewModel = hiltViewModel<WithdrawalViewModel>()

    LaunchedEffect(true) {
        Log.d(TagUtils.MAINVIEWMODELTAG,"WORKEDUI")
        UnityAdsRepository.callBack = homeViewModel
    }
    Box(
        Modifier
            .background(MainBG)
            .fillMaxSize()){
        NavHost(
            navController = navController,
            startDestination = Screen.SplashScreen.route
        ) {


            composable(route = Screen.SplashScreen.route){
                SplashScreen {
                    selected = if (!didShowLearning!!) Screen.LearningScreen1 else Screen.HomeScreen
                    navController.navigate(if (!didShowLearning!!) Screen.LearningScreen1.route else Screen.HomeScreen.route)
                }
            }
            composable(route = Screen.LearningScreen1.route){
                LearningScreen1{
                    selected = Screen.LearningScreen2
                    navController.navigate(Screen.LearningScreen2.route)
                }
            }
            composable(route = Screen.LearningScreen2.route){
                LearningScreen2(mainViewModel.mainDataRepository.learningText){
                    selected = Screen.LearningScreen3
                    navController.navigate(Screen.LearningScreen3.route)
                }
            }
            composable(route = Screen.LearningScreen3.route){
                LearningScreen3{
                    mainViewModel.updateShowLearning()
                    selected = Screen.HomeScreen
                    navController.navigate(Screen.HomeScreen.route)
                }
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(homeViewModel,mainViewModel,{selected = Screen.HomeScreen})
            }
            composable(route = Screen.WithdrawalScreen.route) {
                WithdrawalScreen(withdrawalViewModel,mainViewModel,{selected = Screen.WithdrawalScreen})
            }
            composable(route = Screen.RefferalsScreen.route) {
                RefferalsScreen(referralViewModel,mainViewModel,{selected = Screen.RefferalsScreen})
            }
        }
        if(selected== Screen.HomeScreen||selected== Screen.RefferalsScreen||selected== Screen.WithdrawalScreen){
            NavigationBottomBar(modifier = Modifier.align(Alignment.BottomCenter), selected, {
                if(selected!= Screen.HomeScreen){
                    selected = Screen.HomeScreen
                    mainViewModel.emptyCoinList()
                    navController.navigate(Screen.HomeScreen.route)
                }
            }, {
                if(selected!= Screen.RefferalsScreen){
                    selected = Screen.RefferalsScreen
                    mainViewModel.emptyCoinList()
                    navController.navigate(Screen.RefferalsScreen.route)
                }
            }, {
                if(selected!= Screen.WithdrawalScreen){
                    selected = Screen.WithdrawalScreen
                    mainViewModel.emptyCoinList()
                    navController.navigate(Screen.WithdrawalScreen.route)
                }
            })
        }



    }





}