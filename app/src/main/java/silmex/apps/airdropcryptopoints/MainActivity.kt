package silmex.apps.airdropcryptopoints

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import silmex.apps.airdropcryptopoints.ui.view.CustomToastView
import silmex.apps.airdropcryptopoints.ui.view.composables.Navigation
import silmex.apps.airdropcryptopoints.utils.TagUtils
import silmex.apps.airdropcryptopoints.viewmodel.MainViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableAntiScreenshoting()

        setUpObservers()

        setContent {
            Navigation(mainViewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        isOnPaused.value = false
    }

    override fun onPause() {
        super.onPause()
        isOnPaused.value = true

        mainViewModel.saveUserData()
    }


    fun setUpObservers(){

        shareIntent.observeForever { shareIntent ->
            if (shareIntent != null)
                startActivity(shareIntent)
        }

        toastText.observeForever { toastText ->
            if(!toastText.isNullOrEmpty()){
                val toastView = CustomToastView(this, hasSucceded)
                toastView.setMessage(toastText)

                val toast = Toast(this)
                toast.duration = LENGTH_SHORT
                toast.view = toastView
                toast.show()
            }
        }

    }


    private fun enableAntiScreenshoting(){

        /*        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

                if(!isDebuggable){
                    getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE);
                }*/

        WindowCompat.setDecorFitsSystemWindows(window,false)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableImmersiveMode()

        }
    }
    protected fun enableImmersiveMode() {
        window.apply {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    //object for interacting with activity
    companion object{
        val source = "silmex.apps.airdropcryptopoints"

        var isOnPaused = MutableLiveData<Boolean>(false)

        var shareIntent = MutableLiveData<Intent>();

        var toastText: MutableLiveData<String> = MutableLiveData<String>();

        var hasSucceded: Boolean? = null;



        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }

        fun getLocale(application: Application): String{
            val tm =  application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager;
            var lang = tm.getNetworkCountryIso();
            if (lang == null){    lang = Resources.getSystem().getConfiguration().locale.getLanguage();
            }
            return lang
        }
    }
}