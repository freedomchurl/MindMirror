package vaninside.mindmirror;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

/* Intro Activity with 3 seconds Intro page */
// git test


public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
        final boolean isDarkMode = sharedPreferences.getBoolean("darkmode",false);

        final boolean isLocked = sharedPreferences.getBoolean("lockmode",false);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = null;

                if(isLocked == false)
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                else
                    intent = new Intent(getApplicationContext(),LockerActivity.class);


                if(isDarkMode == true)
                    ThemeUtil.applyTheme(ThemeUtil.DARK_MODE);
                else
                    ThemeUtil.applyTheme(ThemeUtil.LIGHT_MODE);


                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
