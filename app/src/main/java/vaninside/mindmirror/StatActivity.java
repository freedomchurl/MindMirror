package vaninside.mindmirror;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import vaninside.mindmirror.Model.StatData;

public class StatActivity extends AppCompatActivity {

    SQLiteDatabase sampleDB = null;

    private StatAdapter adapter;
    public ArrayList<StatData> mStatList = new ArrayList<>();

    private ArrayList<Integer> mindList = null;

    public ArrayList<String> arrayList;
    public ArrayAdapter<String> arrayAdapter;
    private Spinner spinner;
    public int todayDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);


        arrayList = new ArrayList<String>();
        arrayList.add("일주일");
        arrayList.add("1개월");
        arrayList.add("3개월");
        arrayList.add("6개월");
        arrayList.add("1년");
        arrayList.add("전체");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),arrayList.get(position)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();

                setRecycle(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getTodayDate();
        init();
        //getData();
    }
    public void setRecycle(int pos)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("current: " + df.format(cal.getTime()));

        //cal.add(Calendar.MONTH, 2);
        //cal.add(Calendar.DATE, -3);
        System.out.println("after: " + df.format(cal.getTime()));

        int targetDate = -1;
        Log.d("Pos",pos + "");
        if(pos == 0) // 일주일
        {
            cal.add(Calendar.DATE,-7); //  7일을 뺸다.
            targetDate = getTargetDate(cal);
        }
        else if(pos == 1) // 1개월
        {
            cal.add(Calendar.MONTH,-1);
            targetDate = getTargetDate(cal);
        }
        else if(pos == 2) // 3개월
        {
            cal.add(Calendar.MONTH,-3);
            targetDate = getTargetDate(cal);
        }
        else if(pos == 3) // 6개월
        {
            cal.add(Calendar.MONTH,-6);
            targetDate = getTargetDate(cal);
        }
        else if(pos == 4) // 1년
        {
            cal.add(Calendar.YEAR,-1);
            targetDate = getTargetDate(cal);
        }
        else if(pos == 5) // 전체
        {
            targetDate = -1;
        }


        Log.d("TargetDate",targetDate + "" );
        selectData(targetDate);
        // 다 되었다면, 여기서 데이터를 statData로 가공한다.
        calStatData();
    }

    private void calStatData()
    {
        int num = this.mindList.size(); // size를 가져오고.

        Integer mindCount [] = new Integer[10];

        for(int i =0;i<10;i++)
        {
            mindCount[i] = new Integer(0);
        }

        for(int i=0;i<num;i++)
        {
            mindCount[this.mindList.get(i)-1]++;
            Log.d("Emotion",this.mindList.get(i)-1 + "");
        }

        if(num!=0) {
            Arrays.sort(mindCount, Collections.reverseOrder());


            this.mStatList.clear();


            for (int i = 0; i < 10; i++) {
                if (mindCount[i] != 0)
                    mStatList.add(new StatData(i + 1, mindCount[i], num));
            }

            adapter.notifyDataSetChanged();
        }
    }

    private int getTargetDate(Calendar cal)
    {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        System.out.println("current: " + df.format(cal.getTime()));

        //return Integer.valueOf("" + year + month + date);
        return Integer.valueOf(df.format(cal.getTime()));
    }

    public void getTodayDate()
    {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);

        this.todayDate = Integer.valueOf(year+month+day);

    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.statRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new StatAdapter(mStatList);
        recyclerView.setAdapter(adapter);
    }

    protected void selectData(int target){

        try {

            this.mindList = new ArrayList<Integer>();
            SQLiteDatabase ReadDB = this.openOrCreateDatabase("mind_calendar.db", MODE_PRIVATE, null);

            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = null;
            if(target != -1)
                c = ReadDB.rawQuery("SELECT * FROM " + "mind_data" + " WHERE date>=" + target + " AND date<=" + todayDate, null);
            else
                c = ReadDB.rawQuery("SELECT * FROM " + "mind_data", null);
            //Cursor c = null;
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        //테이블에서 두개의 컬럼값을 가져와서
                        String type = c.getString(c.getColumnIndex("mind"));
                        //String Phone = c.getString(c.getColumnIndex("phone"));

                        this.mindList.add(Integer.parseInt(type));
                        // List 에 넣는다.
                        //HashMap에 넣습니다.
                        //HashMap<String,String> persons = new HashMap<String,String>();

                        //persons.put(TAG_NAME,Name);
                        //persons.put(TAG_PHONE,Phone);

                        //ArrayList에 추가합니다..
                        //personList.add(persons);

                    } while (c.moveToNext());
                }
            }

            ReadDB.close();

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }

    }

    /*private ArrayList<StatData> getData(){
        // 임의의 데이터입니다.
        List<Integer> emotion = Arrays.asList(1, 2, 3);
        List<Integer> num = Arrays.asList(30, 50, 20);

        ArrayList<StatData> mData = new ArrayList<StatData>();
        for (int i = 0; i < emotion.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            StatData data = new StatData();
            data.setEmotion(emotion.get(i));
            data.setNum(num.get(i));
            mData.add(data);
            // 각 값이 들어간 data를 adapter에 추가합니다.
            //  adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        // adapter.notifyDataSetChanged();
        return mData;
    }*/
}