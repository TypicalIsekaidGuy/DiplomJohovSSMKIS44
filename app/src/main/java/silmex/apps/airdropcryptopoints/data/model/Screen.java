package silmex.apps.airdropcryptopoints.data.model;

public abstract class Screen {
    private final String route;

    private Screen(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public String withArgs(String... args) {
        StringBuilder builder = new StringBuilder();
        builder.append(route);
        for (String arg : args) {
            builder.append("/").append(arg);
        }
        return builder.toString();
    }

    public static final Screen HomeScreen = new Screen("home_screen") {};
    public static final Screen WithdrawalScreen = new Screen("withdrawal_screen") {};
    public static final Screen SplashScreen = new Screen("splash_screen") {};
    public static final Screen RefferalsScreen = new Screen("referrals_screen") {};
    public static final Screen LearningScreen = new Screen("learning_screen") {};
    public static final Screen LearningScreen1 = new Screen("learning_screen_1") {};
    public static final Screen LearningScreen2 = new Screen("learning_screen_2") {};
    public static final Screen LearningScreen3 = new Screen("learning_screen_3") {};
    public static final Screen ErrorInternetScreen = new Screen("error_internet_screen") {};
}
