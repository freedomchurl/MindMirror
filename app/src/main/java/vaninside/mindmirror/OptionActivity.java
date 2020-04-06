package vaninside.mindmirror;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {


    public boolean isAlarmSetting = false;
    public Switch AlarmSwitch = null;
    public TextView tAlarmTime = null;

    public Switch darkSwitch = null;
    public boolean isDarkMode = false;

    public ImageButton backButton = null;

    public Switch lockSwitch = null;
    public boolean isLocked = false;

    public int alarmHour = 0;
        public int alarmMin = 0;

    public LinearLayout instLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);


        InitView();
       // InitDB();
    }

    public void InitDB()
    {
        // 아무 설정도 없으면, false로 설정한다.
        // 설정이 있으면 그 값으로 대체 될 것이다.
        SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
        isAlarmSetting = sharedPreferences.getBoolean("alarmmode",false);
        AlarmSwitch.setChecked(isAlarmSetting);
        tAlarmTime.setClickable(isAlarmSetting);

        isDarkMode = sharedPreferences.getBoolean("darkmode",false);
        darkSwitch.setChecked(isDarkMode);

        isLocked = sharedPreferences.getBoolean("lockmode",false);
        lockSwitch.setChecked(isLocked);

        alarmHour = sharedPreferences.getInt("alarmHour",0);
        alarmMin = sharedPreferences.getInt("alarmMin",0);

    }

    public void InitView()
    {
        backButton = (ImageButton) findViewById(R.id.backtoMain);
        AlarmSwitch = (Switch) findViewById(R.id.alarmSwitch);
        instLayout = (LinearLayout) findViewById(R.id.instagramLayout);
        tAlarmTime = (TextView) findViewById(R.id.alramTime);
        darkSwitch = (Switch) findViewById(R.id.darkSwitch);
        lockSwitch = (Switch) findViewById(R.id.lockSwitch);

        InitDB(); // DB로부터 Setting 초기화.

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        instLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.instagram.com/vaninside");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
                // Instagram으로 넘어간다.
            }
        });


        tAlarmTime.setText("매일 " + alarmHour + ":" + alarmMin);
        tAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 시간을 설정하는 새로운 창으로 옮겨야 한다.
                if(isAlarmSetting==true) {
                    Intent it = new Intent(OptionActivity.this, AlarmPicker.class);
                    startActivityForResult(it,3);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"알람 설정이 되어있지 않습니다.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });



        AlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAlarmSetting = b;

                SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("alarmmode",isAlarmSetting); // key, value를 이용하여 저장하는 형
                editor.commit();
                tAlarmTime.setClickable(isAlarmSetting);

                // 여기다가 알람설정을 넣어야한다.
            }
        });

        darkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDarkMode = b;

                SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("darkmode",isDarkMode); // key, value를 이용하여 저장하는 형
                editor.commit();
            }
        });

        lockSwitch.setChecked(isLocked);
        lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isLocked = isChecked;
                // SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                //editor.putBoolean("lockmode",isLocked); // key, value를 이용하여 저장하는 형
                //editor.commit();

                if(isChecked == true && isLocked==false)
                {

                    //Log.d("Lock Set",222 + "");
                    // 이전에 flase였다. 이번에 비밀번호를 설정하는 경우
                    SetLock();
                }
                else if(isChecked == false && isLocked == true)
                {

                    //Log.d("Lock Set",111 + "");
                    // 이전에 true였다. 이번에 비밀번호를 지워야 한다.
                    RemoveLock();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            // 비밀번호 제거.
            if(resultCode == Activity.RESULT_OK)
            {
                isLocked = false;
                lockSwitch.setChecked(false);
                SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("lockmode",isLocked); // key, value를 이용하여 저장하는 형
                editor.remove("locker_pass"); // 비밀번호도 삭제한다.
                editor.commit();

            }
            else
            {
                lockSwitch.setChecked(true);
            }
        }
        else if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                isLocked = true;
            }
            else
                lockSwitch.setChecked(false);
        }
        else if(requestCode == 3)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                this.alarmHour = data.getIntExtra("returnHour",0);
                this.alarmMin = data.getIntExtra("returnMin",0);

                Log.d("ResultFromPicker",alarmHour + " " + alarmMin);

                tAlarmTime.setText("매일 " + alarmHour + ":" + alarmMin);

            }
        }
    }

    public void RemoveLock()
    {
        // 여기서는 Lock을 지워야한다.
        // 1. 현재 Lock이 걸려있는 경우다.
        // 2. 현재 비밀번호를 입력하시오. 라는 창이 떠야한다.
        // 3. 비밀번호가 맞을경우, startActivityonResult 에서 값을 가져와야한다.

       // Log.d("Lock Set",111 + "");
        Intent it = new Intent(getApplicationContext(),LockerActivity.class);
        it.putExtra("lockerType",1); // 비밀 번호 제거
        startActivityForResult(it,1);
    }

    public void SetLock()
    {
        // 여기서는 Lock을 설정해야한다.
        // 1. 새롭게 비밀번호 설정 페이지가 떠야한다.
        // 2. 비밀번호 재입력 페이지가 떠야하고.
        // 3. 두 비밀번호가 일치할 경우, SharedPreferences 에 저장하고 Option으로 돌아와야한다.

        //Log.d("Lock Set",222 + "");
        Intent it = new Intent(getApplicationContext(),LockerActivity.class);
        it.putExtra("lockerType",2); // 비밀 번호 제거
        startActivityForResult(it,2);

    }
}
