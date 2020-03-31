package vaninside.mindmirror;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import vaninside.mindmirror.Model.StatData;
import vaninside.mindmirror.Model.StatItem;

public class StatAdapter extends RecyclerView.Adapter {


    private ArrayList<StatData> mStatList;

    public StatAdapter(ArrayList<StatData> list){
        mStatList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        // MindCalendarAdapter.HeaderViewHolder holder = (MindCalendarAdapter.HeaderViewHolder) viewHolder;
        // Object item = mCalendarList.get(position);

        StatAdapter.StatViewHolder holder = (StatViewHolder) viewHolder;
        holder.onBind(mStatList.get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        StatAdapter.StatViewHolder viewHolder = new  StatAdapter.StatViewHolder(inflater.inflate(R.layout.item_stat, viewGroup, false));

            /*
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true); // SPAN 을 하나로 통합
            viewHolder.itemView.setLayoutParams(params);
*/
        return viewHolder;

    }

    @Override
    public int getItemCount() {

        if (mStatList != null) {
            return mStatList.size();
        }
        return 0;
    }

    public class StatViewHolder extends RecyclerView.ViewHolder { //날짜 타입 ViewHolder

        int max = 100;
        int progress = 80;
        ProgressBar progressBar;
        ImageView mindType;

        public StatViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void initView(View v) {

            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            mindType = (ImageView) v.findViewById(R.id.mindType);
        }

        public void onBind(StatData data) {
/*
            // 일자 값 가져오기
            String header = ((CalendarHeader) model).getHeader();
            // header에 표시하기, ex : 2018년 8월
            itemHeaderTitle.setText(header);
            */

            progressBar.setMax(data.getTotal());
            progressBar.setProgress(data.getNum());

            if(data.getEmotion() == 1)
                mindType.setBackgroundColor(Color.RED);
            else if(data.getEmotion() == 2)
                mindType.setBackgroundColor(Color.BLUE);
        }
    }

}