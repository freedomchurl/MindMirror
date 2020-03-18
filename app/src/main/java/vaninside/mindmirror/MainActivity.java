package vaninside.mindmirror;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        LinearLayout mL = (LinearLayout) findViewById(R.id.calLayout);

        //--LinearLayout mainLayout = new LinearLayout(this);
        //--mainLayout.setOrientation(LinearLayout.VERTICAL);


        //LayoutInflater inflater = (LayoutInflater) getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        //ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.mindcalendar, mainLayout, false);

        MindCalendarView cal = new MindCalendarView(this, mL);
        //--LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //--params2.weight = 1;

        //cal.setLayoutParams(params2);
        //mainLayout.addView(cal);

        // --- setContentView(mainLayout);
        //setContentView(R.layout.mindcalendar);

        Button statButton = (Button) findViewById(R.id.statbutton);
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatActivity.class);
                startActivity(intent);
            }
        });

    }




}

