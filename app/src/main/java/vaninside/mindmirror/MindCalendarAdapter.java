package vaninside.mindmirror;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import vaninside.mindmirror.Model.CalendarHeader;
import vaninside.mindmirror.Model.Day;
import vaninside.mindmirror.Model.EmptyDay;
import vaninside.mindmirror.Model.ViewModel;


public class MindCalendarAdapter extends RecyclerView.Adapter {

    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;
    Context context;

    public MindCalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    // 데이터넣어서 완성하기.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        //Log.d("Order","2");
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model에 데이터들어감.
                model.setHeader((Long) item);
            }
            // view에 표시하기
            holder.bind(model);

        } else if (viewType == EMPTY_TYPE) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        } else if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {

                // Model에 Calendar값을 넣어서 몇일인지 데이터 넣기
                model.setCalendar((Calendar) item);
            }
            // Model의 데이터를 View에 표현하기
            holder.bind(model);
        }


    }

    @Override
    public int getItemViewType(int position) { // 뷰타입 나누기,
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; // 날짜 타입
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입.
        }
    }

    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        //Log.d("Order","1");
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_calendar_header, viewGroup, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true); // SPAN 을 하나로 통합
            viewHolder.itemView.setLayoutParams(params);
            Log.d("TEST", "header");

            return viewHolder;

        } else if (viewType == EMPTY_TYPE) {
            Log.d("TEST", "empty");
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, viewGroup, false));
        } else {

            Log.d("TEST", "day");
            return new DayViewHolder(inflater.inflate(R.layout.item_day, viewGroup, false));
        }
        //return null;
    }

    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }


    /**
     * viewHolder
     **/
    private class HeaderViewHolder extends RecyclerView.ViewHolder { //날짜 타입 ViewHolder

        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }


        public void initView(View v) {

            itemHeaderTitle = (TextView) v.findViewById(R.id.item_header_title);

        }

        public void bind(ViewModel model) {

            // 일자 값 가져오기
            String header = ((CalendarHeader) model).getHeader();
            // header에 표시하기, ex : 2018년 8월
            itemHeaderTitle.setText(header);



        }


    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 요일 타입 ViewHolder


        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v) {

        }

        public void bind(ViewModel model) {


        }
    }

    // TODO : item_day와 매칭
    private class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder

        TextView itemDay;
        ImageView Mindimage;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            initView(itemView);

        }

        public void initView(View v) {

            itemDay = (TextView) v.findViewById(R.id.item_day);
            Mindimage = (ImageView) v.findViewById(R.id.mind_image);



        }

        public void bind(ViewModel model) {

            // 일자 값 가져오기
            String day = ((Day) model).getDay();
            String currentDay = ((Day) model).getFullDay(); // yyyy-MM-dd
            /*
            String this_currentMonth;
            if(t_Header != null)
                this_currentMonth = t_Header.getText().toString();
            else
                this_currentMonth = currentMonth;
            // DB 에서 정보를 가져와야함.
            */
            // 오늘의 날짜.
            /*if(Integer.valueOf(day) < 10){
                day = "0"+day;
            }*/
            //String currentDay = this_currentMonth + day;
            //String currentDay = currentMonth + day;
            //String result = String.format("%s%02d",currentMonth,Integer.valueOf(day));
            Log.d(" DAY   ", day);

            SQLiteDatabase db;
            db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

            final Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("currentDay", currentDay);

            // NAME 컬럼 값이 'ppotta'인 모든 데이터 조회.
            String sqlSelect = "SELECT * FROM mind_data WHERE date="+currentDay ;

            Cursor cursor = db.rawQuery(sqlSelect, null) ;
            int mind=0;
            if(cursor != null) {
                if(cursor.getCount() == 0) {
                    Log.d("HERE   ", Integer.toString(mind));
                    Mindimage.setImageResource(R.drawable.new_emotion);
                    intent.putExtra("mode", 0);
                } else {
                    while (cursor.moveToNext()) {
                        mind = cursor.getInt(2);
                        //Log.d("HERE   ", Integer.toString(mind));
                        Log.d("Checked","DAY : " + currentDay + " ," + "EMOTION : " + mind);
                        if(mind == 1)
                            Mindimage.setImageResource(R.drawable.new_emotion_2);
                        else if (mind == 2)
                            Mindimage.setImageResource(R.drawable.new_emotion_3);

                    }
                    intent.putExtra("mode", 1);
                }
            }

            // 아이템을 클릭하면 상세 페이지로 넘어간다.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : process click event.
                    context.startActivity(intent);
                }
            });


            // 일자 값 View에 보이게하기
            itemDay.setText(day);
            db.close();
        }


    }
}
