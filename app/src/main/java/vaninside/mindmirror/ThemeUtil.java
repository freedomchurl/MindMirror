package vaninside.mindmirror;

//import android.os.Build;
//import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    public static final String LIGHT_MODE = "light";
    public static final String DARK_MODE = "dark";
    public static final String DEFAULT_MODE = "default";

    public static void applyTheme(String themeColor) {
        switch (themeColor) {
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case DARK_MODE: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d("check", "dark mode");
                break;
            }
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
    }
}
