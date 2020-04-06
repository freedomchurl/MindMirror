package vaninside.mindmirror;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
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

    SQLiteDatabase db;

    public MindCalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            if (item instanceof Long) {
                model.setHeader((Long) item);
            }
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
                model.setCalendar((Calendar) item);
            }
            holder.bind(model);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE;
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자
        } else {
            return DAY_TYPE; // 일자
        }
    }

    // viewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_calendar_header, viewGroup, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true);
            viewHolder.itemView.setLayoutParams(params);

            return viewHolder;

        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, viewGroup, false));
        } else {
            return new DayViewHolder(inflater.inflate(R.layout.item_day, viewGroup, false));
        }
    }

    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }


    /*** viewHolder ***/
    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }


        public void initView(View v) {
            itemHeaderTitle = (TextView) v.findViewById(R.id.item_header_title);
        }

        public void bind(ViewModel model) {
            String header = ((CalendarHeader) model).getHeader();
            itemHeaderTitle.setText(header);
        }


    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void initView(View v) {

        }

        public void bind(ViewModel model) {

        }
    }

    private class DayViewHolder extends RecyclerView.ViewHolder {

        TextView thisDate;
        ImageView Mindimage;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            initView(itemView);
        }

        public void initView(View v) {
            thisDate = (TextView) v.findViewById(R.id.item_day);
            Mindimage = (ImageView) v.findViewById(R.id.mind_image);
        }

        public void bind(ViewModel model) {

            // 일자 값 가져오기
            String day = ((Day) model).getDay();
            String currentDay = ((Day) model).getFullDay(); // yyyy-MM-dd

            Date today = new Date();
            Date currentdayDate = new Date(((Day) model).getMilliseconds());

            final Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("currentDay", currentDay);

            db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

            String sqlSelect = "SELECT * FROM mind_data WHERE date="+currentDay ;

            Cursor cursor = db.rawQuery(sqlSelect, null) ;
            int mind=0;
            if(cursor != null) {
                if(cursor.getCount() == 0) { // NO data.
                    intent.putExtra("mode", 0);
                    if(currentdayDate.getTime() <= today.getTime()){ // 오늘 이전의 날짜일 경우
                        Mindimage.setImageResource(R.drawable.new_emotion);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO : process click event.
                                context.startActivity(intent);
                            }
                        });
                    }else { // 오늘 이후이면 표시 안함.
                        Mindimage.setImageResource(R.drawable.test_emotion);
                    }
                } else {
                    intent.putExtra("mode", 1);
                    while (cursor.moveToNext()) {
                        mind = cursor.getInt(2);

                        // ---------- Mind 에 따라 알맞은 이모티콘 표시
                        if(mind == 1)
                            Mindimage.setImageResource(R.drawable.new_emotion_2);
                        else if (mind == 2)
                            Mindimage.setImageResource(R.drawable.new_emotion_3);
                        else if (mind == 3)
                            Mindimage.setImageResource(R.drawable.new_emotion_4);
                        else if (mind == 4)
                            Mindimage.setImageResource(R.drawable.new_emotion_5);
                    }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO : process click event.
                            context.startActivity(intent);
                        }
                    });
                }
            }

            // 오늘의 날짜 setText.
            thisDate.setText(day);
            db.close();
        }
    }
}
