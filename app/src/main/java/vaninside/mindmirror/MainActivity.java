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

/* Main Page with Mind Calendar and Menu */

public class MainActivity extends AppCompatActivity {
    MindCalendarView cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// --------------- Database Setting ( mind_data table in mind_calendar.db )

        SQLiteDatabase db;
        db = openOrCreateDatabase("mind_calendar.db",MODE_PRIVATE, null);

        String sql = "CREATE TABLE IF NOT EXISTS "+"mind_data"+" ("
                +"id integer PRIMARY KEY autoincrement, "
                +"date integer NOT NULL, "
                +"mind integer NOT NULL, "
                +"text text "
                +");";
        db.execSQL(sql);

        db.close();

// --------------- Database Setting finish

        setContentView(R.layout.activity_main);
        LinearLayout calLayout = (LinearLayout) findViewById(R.id.calLayout);

        cal = new MindCalendarView(this, calLayout);

        Button statButton = (Button) findViewById(R.id.statbutton);
        Button settingButton = (Button) findViewById(R.id.settingbutton);

        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatActivity.class);
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
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

