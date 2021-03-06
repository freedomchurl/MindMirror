package vaninside.mindmirror;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

        ImageButton statButton = (ImageButton) findViewById(R.id.statbutton);
        ImageButton settingButton = (ImageButton) findViewById(R.id.settingbutton);

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

