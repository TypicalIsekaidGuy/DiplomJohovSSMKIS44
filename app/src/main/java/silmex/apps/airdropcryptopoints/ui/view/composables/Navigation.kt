package silmex.apps.airdropcryptopoints.ui.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import silmex.apps.airdropcryptopoints.data.model.Screen
import silmex.apps.airdropcryptopoints.ui.theme.MainBG
import silmex.apps.airdropcryptopoints.ui.view.screen.HomeScreen
import silmex.apps.airdropcryptopoints.ui.view.screen.LearningScreen1
import silmex.apps.airdropcryptopoints.ui.view.screen.LearningScreen2
import silmex.apps.airdropcryptopoints.ui.view.screen.LearningScreen3
import silmex.apps.airdropcryptopoints.ui.view.screen.RefferalsScreen
import silmex.apps.airdropcryptopoints.ui.view.screen.SplashScreen
import silmex.apps.airdropcryptopoints.ui.view.screen.WithdrawalScreen
import silmex.apps.airdropcryptopoints.viewmodel.HomeViewModel
import silmex.apps.airdropcryptopoints.viewmodel.RefferralViewModel
import silmex.apps.airdropcryptopoints.viewmodel.WithdrawalViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    var selected by remember{ mutableStateOf (Screen.HomeScreen) }
    Box(
        Modifier
            .background(MainBG)
            .fillMaxSize()){
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreen.route
        ) {


            composable(route = Screen.SplashScreen.route){
                SplashScreen {
                    selected = Screen.LearningScreen1
                    navController.navigate(Screen.LearningScreen1.route)
                }
            }
            composable(route = Screen.LearningScreen1.route){
                LearningScreen1{
                    selected = Screen.LearningScreen2
                    navController.navigate(Screen.LearningScreen2.route)
                }
            }
            composable(route = Screen.LearningScreen2.route){
                LearningScreen2{
                    selected = Screen.LearningScreen3
                    navController.navigate(Screen.LearningScreen3.route)
                }
            }
            composable(route = Screen.LearningScreen3.route){
                LearningScreen3{
                    selected = Screen.HomeScreen
                    navController.navigate(Screen.HomeScreen.route)
                }
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(hiltViewModel<HomeViewModel>())
            }
            composable(route = Screen.WithdrawalScreen.route) {
                WithdrawalScreen(hiltViewModel<WithdrawalViewModel>())
            }
            composable(route = Screen.RefferalsScreen.route) {
                RefferalsScreen(hiltViewModel<RefferralViewModel>())
            }
        }
        if(selected== Screen.HomeScreen||selected== Screen.RefferalsScreen||selected== Screen.WithdrawalScreen){
            NavigationBottomBar(modifier = Modifier.align(Alignment.BottomCenter), selected, {
                if(selected!= Screen.HomeScreen){
                    selected = Screen.HomeScreen
                    navController.navigate(Screen.HomeScreen.route)
                }
            }, {
                if(selected!= Screen.RefferalsScreen){
                    selected = Screen.RefferalsScreen
                    navController.navigate(Screen.RefferalsScreen.route)
                }
            }, {
                if(selected!= Screen.WithdrawalScreen){
                    selected = Screen.WithdrawalScreen
                    navController.navigate(Screen.WithdrawalScreen.route)
                }
            })
        }



    }





}