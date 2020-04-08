package vaninside.mindmirror;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
    private int mode = 0;

    private String currentDay;
    private ImageButton button; // edit, finish button. Fragment change.
    private ImageButton deleteButton;
    private ImageButton exitButton;
    SQLiteDatabase db;

    private boolean isExist = false;

    private boolean isDark = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_NoTitleBar);
        // get currentDay Info
        currentDay = getIntent().getStringExtra("currentDay");


        SharedPreferences sharedPreferences = getSharedPreferences("mind_key", MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //String text = editText.getText().toString(); // 사용자가 입력한 저장할 데이터
        isDark = sharedPreferences.getBoolean("darkmode", false);


        setContentView(R.layout.detail_fragment);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        // mode 0 : No data. Edit Page
        // mode 1 : Have data. Show Page

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);

        Log.d("mode : ", Integer.toString(mode));

        if (mode == 0)
            isExist = false;
        else
            isExist = true;

        // Display Popup
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.9);
        int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        // Finish or Edit Button
        button = (ImageButton) findViewById(R.id.mybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((Integer) button.getTag() == 0) {
                    if(((DetailEditFragment) getSupportFragmentManager().findFragmentById(R.id.frame)).getMind() == 0){
                        Toast.makeText(getApplicationContext(), "감정을 선택하세요.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (isFragmentB) {
                            ((DetailEditFragment) getSupportFragmentManager().findFragmentById(R.id.frame)).saveData();
                            isExist = true;
                        }

                        switchFragment();
                    }
                    // Edit Page should save data.
                } else {
                    switchFragment();

                }
            }
        });

        exitButton = (ImageButton) findViewById(R.id.exitbutton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(isDark == false)
            exitButton.setImageResource(R.drawable.exit);
        else
            exitButton.setImageResource(R.drawable.cancel_button);


        deleteButton = (ImageButton) findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialogClick(v);

            }
        });

        if(isDark == false)
            deleteButton.setImageResource(R.drawable.delete);
        else
            deleteButton.setImageResource(R.drawable.trash_button);

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
            button.setTag(0);
            if(isDark == false)
                button.setImageResource(R.drawable.finish);
            else
                button.setImageResource(R.drawable.check_button);

            if (isExist)
                deleteButton.setVisibility(View.VISIBLE);
            else
                deleteButton.setVisibility(View.INVISIBLE);
        } else {
            transaction.replace(R.id.frame, fragmentA).commitAllowingStateLoss();
            isFragmentB = false;

            button.setTag(1);
            if(isDark == false)
                button.setImageResource(R.drawable.edit);
            else
                button.setImageResource(R.drawable.write_button);

            deleteButton.setVisibility(View.VISIBLE);
        }
    }


    public void switchFragment() {

        Fragment fr;
        if (isFragmentB) {
            fr = new DetailFragment();
            button.setTag(1);
            if(isDark == false)
                button.setImageResource(R.drawable.edit);
            else
                button.setImageResource(R.drawable.write_button);

            deleteButton.setVisibility(View.VISIBLE);
        } else {
            fr = new DetailEditFragment();
            button.setTag(0);
            if(isDark == false)
                button.setImageResource(R.drawable.finish);
            else
                button.setImageResource(R.drawable.check_button);

            deleteButton.setVisibility(View.INVISIBLE);
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

    public void DeleteDialogClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("").setMessage("정말로 삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // open database
                db = openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);
                db.execSQL("DELETE FROM mind_data where date =  '" + currentDay + "';");
                db.close();
                Toast.makeText(getApplicationContext(), "감정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
