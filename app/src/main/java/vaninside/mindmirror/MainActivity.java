package vaninside.mindmirror;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    MindCalendarView cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

////////////// Database Test

        SQLiteDatabase db;
        db = openOrCreateDatabase("mind_calendar.db",MODE_PRIVATE, null);

        String sql = "CREATE TABLE IF NOT EXISTS "+"mind_data"+" ("
                +"id integer PRIMARY KEY autoincrement, "
                +"date text NOT NULL, "
                +"mind integer NOT NULL, "
                +"text text "
                +");";
        db.execSQL(sql);

        db.execSQL("INSERT INTO mind_data(date, mind, text) values (20200319, 1, '멋진 지민이')");
        db.execSQL("INSERT INTO mind_data(date, mind, text) values (20200320, 2, '바보 철')");


        db.close();
/////////////////// Database Test
        setContentView(R.layout.activity_main);
        LinearLayout mL = (LinearLayout) findViewById(R.id.calLayout);

        cal = new MindCalendarView(this, mL);


        Button statButton = (Button) findViewById(R.id.statbutton);
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        cal.mAdapter.notifyDataSetChanged();
        super.onResume();

    }
}

