package silmex.apps.airdropcryptopoints.utils;

import static kotlin.random.RandomKt.Random;

import android.os.Build;
import android.util.Log;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

import silmex.apps.airdropcryptopoints.data.repository.MainDataRepository;

public class StringUtils {
    public static String getBalanceText(Float balance) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setGroupingUsed(true);
        return formatter.format(balance).replace(","," ");
    }

    public static String generateDeviceIdentifier() {
        String uniqueDevicePseudoID;
        String serial;
        if(MainDataRepository.tempRandom!=null&&MainDataRepository.tempRandom!=MainDataRepository.random_for_save.getValue()){
            uniqueDevicePseudoID = "97" +            Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +            Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +            Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +            Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +            Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +            Build.TYPE.length() % 10 +
                    Build.USER.length() % 10+ MainDataRepository.tempRandom;
            serial = Build.getRadioVersion();
            MethodUtils.safeSetValue(MainDataRepository.random_for_save,MainDataRepository.tempRandom);
        }
        else{
            uniqueDevicePseudoID = "97" +            Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +            Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +            Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +            Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +            Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +            Build.TYPE.length() % 10 +
                    Build.USER.length() % 10+ MainDataRepository.random_for_save.getValue();
            serial = Build.getRadioVersion();
        }
        Log.d("workkk", new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString());
        return new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString();
    }
    public static String generateDeviceIdentifier(int random_for_save) {
        String uniqueDevicePseudoID = "97" +            Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +            Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +            Build.HOST.length() % 10 +
                Build.ID.length() % 10 +            Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +            Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +            Build.TYPE.length() % 10 +
                Build.USER.length() % 10+ random_for_save;
        String serial = Build.getRadioVersion();
        Log.d("workkk", new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString());
        return new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString();
    }
}
