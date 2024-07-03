package silmex.apps.airdropcryptopoints.utils;

import static kotlin.random.RandomKt.Random;

public class IntegerUtils {

    public static Integer generateRandomInteger(){
        return Random(System.nanoTime()).nextInt();
    }
}
