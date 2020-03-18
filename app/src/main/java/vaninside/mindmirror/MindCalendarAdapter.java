package vaninside.mindmirror;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.TextView;

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

        if (viewType == HEADER_TYPE) {
            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_calendar_header, viewGroup, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true); // SPAN 을 하나로 통합
            viewHolder.itemView.setLayoutParams(params);

            return viewHolder;

        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, viewGroup, false));
        } else {
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

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            initView(itemView);

        }

        public void initView(View v) {

            itemDay = (TextView) v.findViewById(R.id.item_day);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : process click event.
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("data", "Test Popup");
                    context.startActivity(intent);


                }
            });

        }

        public void bind(ViewModel model) {

            // 일자 값 가져오기
            String day = ((Day) model).getDay();

            // 일자 값 View에 보이게하기
            itemDay.setText(day);

        }

        ;
    }
}
