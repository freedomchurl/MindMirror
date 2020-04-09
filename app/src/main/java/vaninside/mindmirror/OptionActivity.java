package vaninside.mindmirror;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

import java.util.Calendar;
import java.util.Date;

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
    public ImageButton instB = null;
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

        instB = (ImageButton) findViewById(R.id.instagram_my);

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

        instB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.instagram.com/vaninside");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });

        String sHour = String.format("%02d", alarmHour);
        String sMin = String.format("%02d", alarmMin);


        tAlarmTime.setText("매일 " + sHour + ":" + sMin);
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

                if(isAlarmSetting == true)
                {
                    setAlarm();
                }
                else if(isAlarmSetting == false)
                {
                    disableAlarm();
                }

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

                if(isDarkMode == true)
                    ThemeUtil.applyTheme(ThemeUtil.DARK_MODE);
                else
                    ThemeUtil.applyTheme(ThemeUtil.LIGHT_MODE);

                //Intent intent = getIntent();
                //finish();
                //startActivity(intent);
                finish();

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

                String sHour = String.format("%02d", alarmHour);
                String sMin = String.format("%02d", alarmMin);


                tAlarmTime.setText("매일 " + sHour + ":" + sMin);

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

    public void disableAlarm() {
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (PendingIntent.getBroadcast(this, 0, alarmIntent, 0) != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Notifications were disabled", Toast.LENGTH_SHORT).show();
        }
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setAlarm()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, this.alarmHour);
        calendar.set(Calendar.MINUTE, this.alarmMin);
        calendar.set(Calendar.SECOND, 0);
        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        Date currentDateTime = calendar.getTime();
        //String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
        //Toast.makeText(getApplicationContext(),date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

        //  Preference에 설정한 값 저장
        SharedPreferences.Editor editor = getSharedPreferences("mind_key", MODE_PRIVATE).edit();
        editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());
        editor.putInt("alarmHour",this.alarmHour);
        editor.putInt("alarmMin",this.alarmMin);
        //editor.apply();
        editor.commit();


        Boolean dailyNotify = true; // 무조건 알람을 사용

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {

            Log.d("Calendar2",calendar.getTime().toString());
            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }
}
