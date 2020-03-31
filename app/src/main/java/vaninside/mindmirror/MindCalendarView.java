package vaninside.mindmirror;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.AttributedCharacterIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MindCalendarView extends LinearLayout {

    public int mCenterPosition;
    public ArrayList<Object> mCalendarList = new ArrayList<>();

    public TextView textView;
    public RecyclerView recyclerView;
    public MindCalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;
    private LayoutInflater inflater = null;

    public MindCalendarView(Context context, LinearLayout container){
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        onCreateView(container);
    }

    public View onCreateView(ViewGroup container) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.mindcalendar, container, true);
        initView(rootView);
        initSet();
        setRecycler();
        return rootView;
    }

    public void initView(View v){
        textView = (TextView)v.findViewById(R.id.title);
        recyclerView = (RecyclerView)v.findViewById(R.id.calendar);
    }

    public void initSet(){
        initCalendarList();
    }

    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    private void setRecycler() {

        if (mCalendarList == null) {
            Log.w(TAG, "No Query, not initializing RecyclerView");
        }

        manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new MindCalendarAdapter(mCalendarList);

        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            recyclerView.scrollToPosition(mCenterPosition);
        }
    }

    public void setCalendarList(GregorianCalendar cal) {

        ArrayList<Object> calendarList = new ArrayList<>();

        for (int i = -300; i < 300; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                // 0일때 오늘 날짜. 그래서 오늘 날짜로 부터 300달 이후, 300달 이전 까지 표시 가능.

                if (i == 0) {
                    // 오늘 날짜를 Center Position으로 정하고.
                    mCenterPosition = calendarList.size();
                }

                calendarList.add(calendar.getTimeInMillis()); // 캘린더에 시간을 저장하고.

                // 위에서 달만 설정하고, 날짜는 1 일로 설정함.
                // DAY_OF_WEEK 는 현재 요일
                // DAY_OF_MONTH 는 현재 월.
                // 1 : 일, 2: 월 ....
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 현재 월의 날짜.

                // 1일 전의 빈칸
                for (int j = 0; j < dayOfWeek; j++) {
                    calendarList.add(Keys.EMPTY);
                }

                // 날짜 입력 칸
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                }

                GregorianCalendar myCalendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, max, 0, 0, 0);
                int tailEmpty = 7 - myCalendar.get(Calendar.DAY_OF_WEEK);

                // 마지막 날 이후의 빈칸
                for (int j = 0; j < tailEmpty; j++) {
                    calendarList.add(Keys.EMPTY);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCalendarList = calendarList;
    }

    public class Keys {
        public static final String EMPTY = "empty";
    }

   }

