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
        String uniqueDevicePseudoID = "97" +            Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +            Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +            Build.HOST.length() % 10 +
                Build.ID.length() % 10 +            Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +            Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +            Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        String serial = Build.getRadioVersion()+ MainDataRepository.random_for_save;
        Log.d("workkk", new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString());
        return new UUID(uniqueDevicePseudoID.hashCode(), serial.hashCode()).toString();
    }
}
