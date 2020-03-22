package vaninside.mindmirror;

import android.database.Cursor;
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
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements Bfragment.InteractListener{

    private FragmentManager fragmentManager;
    private Afragment fragmentA;
    private Bfragment fragmentB;
    private FragmentTransaction transaction;
    private boolean isFragmentB= false;
    private String currentDay;
    private int mind=0;
    private Button button;

    public TextView dayTextview;
    public TextView dayOfTheWeekTextview;

    SQLiteDatabase db;

    // Data for edit
    private int mMind;
    private String mText;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dayTextview = (TextView) findViewById(R.id.textView2);
        dayOfTheWeekTextview = (TextView) findViewById(R.id.textView3);

        currentDay = getIntent().getStringExtra("currentDay");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", 0);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
        db = openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);


        // NAME 컬럼 값이 'ppotta'인 모든 데이터 조회.
        String sqlSelect = "SELECT * FROM mind_data WHERE date="+currentDay ;

        Cursor cursor = db.rawQuery(sqlSelect, null) ;
        if(cursor != null) {
            while (cursor.moveToNext()) {
                // INTEGER로 선언된 첫 번째 "NO" 컬럼 값 가져오기.
                mind = cursor.getInt(2);
        }

            button = (Button) findViewById(R.id.mybutton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TEST" , mMind + " " + mText);
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

        fragmentManager = getSupportFragmentManager();

        fragmentA = new Afragment();
        fragmentB = new Bfragment();

        Bundle bundle = new Bundle();
        bundle.putString("currentDay", currentDay);

        fragmentA.setArguments(bundle);
        fragmentB.setArguments(bundle);

        transaction = fragmentManager.beginTransaction();
        if(mode == 0){
            //mMind = 0;
            //mText = null;
            transaction.replace(R.id.frame, fragmentB).commitAllowingStateLoss();
            isFragmentB = true;
            button.setText("FINISH");
        }else{

            //db = openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);
            //db.execSQL("UPDATE mind_data SET text="+mText+" WHERE date="+currentDay+";");
            //db.execSQL("INSERT INTO mind_data(mind, text) values ("+mMind+","+mText+");");
            transaction.replace(R.id.frame, fragmentA).commitAllowingStateLoss();
            isFragmentB = false;
            button.setText("EDIT");
        }
        // state 의 저장과 관련없게 작동하려면.



    };
    }

    public void switchFragment() {
        Fragment fr;
        if (isFragmentB) {
            db = openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);
            //Log.d("string test",mText);
            db.execSQL("UPDATE mind_data SET text="+mText+" WHERE date="+currentDay+";");
            //db.execSQL("INSERT INTO mind_data(mind, text) values ("+mMind+","+mText+");");
            db.close();
            fr = new Afragment();
            button.setText("EDIT");
        } else {
            mMind = 0;
            mText = null;
            fr = new Bfragment();
            button.setText("FINISH");
        }


        Bundle bundle = new Bundle();
        bundle.putString("currentDay", currentDay);

        fr.setArguments(bundle);

        isFragmentB = (isFragmentB) ? false : true;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame, fr);
        transaction.commit();

    }

    @Override
    public void interact(int mind, String text) {
        mMind = mind;
        mText = text;
    }
}
