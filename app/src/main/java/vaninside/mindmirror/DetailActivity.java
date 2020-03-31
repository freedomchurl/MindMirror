package vaninside.mindmirror;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import vaninside.mindmirror.Model.CreateViewToBitmap;

/* Detail Page when you click the calendar */
/* Show Emotion and Memo of each day */

public class DetailActivity extends AppCompatActivity {

    // fragment A : detail show page
    // fragment B : detail edit page

    private FragmentManager fragmentManager;
    private DetailFragment fragmentA;
    private DetailEditFragment fragmentB;
    private FragmentTransaction transaction;
    private boolean isFragmentB = false;

    private String currentDay;
    private Button button;

    SQLiteDatabase db;

    private boolean isExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get currentDay Info
        currentDay = getIntent().getStringExtra("currentDay");

        setContentView(R.layout.detail_fragment);

        // mode 0 : No data. Edit Page
        // mode 1 : Have data. Show Page

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", 0);

        if (mode == 0)
            isExist = false;
        else
            isExist = true;

        // Display Popup
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        // Finish or Edit Button
        button = (Button) findViewById(R.id.mybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText() == "FINISH") {
                    // Edit Page should save data.
                    if (isFragmentB) {
                        ((DetailEditFragment) getSupportFragmentManager().findFragmentById(R.id.frame)).saveData();
                    }
                }
                switchFragment();
            }
        });

        Button exitButton = (Button) findViewById(R.id.exitbutton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open database
                db = openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);
                db.execSQL("DELETE FROM mind_data where date =  '" + currentDay + "';");
                db.close();
                Toast.makeText(getApplicationContext(), "데이터 삭제 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        // Fragment Setting
        fragmentManager = getSupportFragmentManager();

        fragmentA = new DetailFragment();
        fragmentB = new DetailEditFragment();

        // get currentDay data
        Bundle bundle = new Bundle();
        bundle.putString("currentDay", currentDay);

        fragmentA.setArguments(bundle);
        fragmentB.setArguments(bundle);

        transaction = fragmentManager.beginTransaction();
        if (mode == 0) {
            transaction.replace(R.id.frame, fragmentB).commitAllowingStateLoss();
            isFragmentB = true;
            button.setText("FINISH");
            if (isExist)
                deleteButton.setVisibility(View.VISIBLE);
            else
                deleteButton.setVisibility(View.INVISIBLE);
        } else {
            transaction.replace(R.id.frame, fragmentA).commitAllowingStateLoss();
            isFragmentB = false;
            button.setText("EDIT");
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }


    public void switchFragment() {
        Fragment fr;
        if (isFragmentB) {
            fr = new DetailFragment();
            button.setText("EDIT");
        } else {
            fr = new DetailEditFragment();
            button.setText("FINISH");
        }

        // pass currentDay Info.
        Bundle bundle = new Bundle();
        bundle.putString("currentDay", currentDay);

        fr.setArguments(bundle);

        isFragmentB = (isFragmentB) ? false : true;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame, fr);
        transaction.commit();
    }


}
