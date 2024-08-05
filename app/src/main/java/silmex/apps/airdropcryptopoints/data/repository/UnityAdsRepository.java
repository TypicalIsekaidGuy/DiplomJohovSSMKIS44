package silmex.apps.airdropcryptopoints.data.repository;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;
import silmex.apps.airdropcryptopoints.data.interfaces.UpgradeWheelCallBack;
import silmex.apps.airdropcryptopoints.viewmodel.MainViewModel;

@ActivityScoped
public class UnityAdsRepository implements IUnityAdsInitializationListener {
    Activity activity;

    public static UpgradeWheelCallBack callBack;

    public UnityAdsRepository(Context context){
        activity = (Activity) context;
    }

    // <editor-fold desc="UnityAds">

    @Override
    public void onInitializationComplete() {
        // No operation
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.d("tried", message != null ? message : "Unknown error");
    }

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            MainViewModel.hasLoadedAd.setValue(true);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e(
                    "UnityAdsExample",
                    "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message
            );
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e(
                    "UnityAdsExample",
                    "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message
            );
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            if (state == UnityAds.UnityAdsShowCompletionState.COMPLETED) {
                UnityAds.load(MainDataRepository.unityBlock, loadListener);
                if(callBack==null)
                    Log.d("callback","this cant be");
                callBack.upgradeWheel();
                MainViewModel.hasLoadedAd.setValue(false);
            } else {
                UnityAds.load(MainDataRepository.unityBlock, loadListener);
                MainViewModel.hasLoadedAd.setValue(false);
            }
        }
    };

    public void showUnityAds() {
        Log.d("Worked","WORKED");
        UnityAds.show(activity,
                MainDataRepository.unityBlock,
                new UnityAdsShowOptions(),
                showListener
        );
    }

    public void initializeUnity(){
        UnityAds.initialize(
                activity.getApplicationContext(),
                MainDataRepository.unityID,
                false,
                this
        );
        Log.d("NETWORKTAG","1"+MainDataRepository.unityID);
        Log.d("NETWORKTAG","1"+MainDataRepository.unityBlock);
        UnityAds.load(MainDataRepository.unityBlock, loadListener);
    }

// </editor-fold>

}
