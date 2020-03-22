package vaninside.mindmirror;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

// Detail Page

public class Afragment extends Fragment {
    int mind = 0;
    Context context;
    String currentDay;
    String text;



    public Afragment(){
        //context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail, container, false);

        context = getActivity();
        ImageView local = (ImageView) v.findViewById(R.id.mind_detail_image);
        TextView textView = (TextView) v.findViewById(R.id.detail_textView);
/*

        TextView dayTextview;
        TextView dayOfTheWeekTextview;


        dayTextview = (TextView) v.findViewById(R.id.textView2);
        dayOfTheWeekTextview = (TextView) v.findViewById(R.id.textView3);


        int day = Integer.parseInt(currentDay.substring(6,7));
        Log.d("thisday", Integer.toString(day));
        dayTextview.setText(Integer.toString(day));
        day.setText(Integer.toString(day));

*/
        if(getArguments() !=  null)
            currentDay = getArguments().getString("currentDay");


        if (context != null) {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);


            // NAME 컬럼 값이 'ppotta'인 모든 데이터 조회.
            String sqlSelect = "SELECT * FROM mind_data WHERE date=" + currentDay;

            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // INTEGER로 선언된 첫 번째 "NO" 컬럼 값 가져오기.
                    mind = cursor.getInt(2);
                    text = cursor.getString(3);
                }

                if (mind == 0)
                    local.setImageResource(R.drawable.new_emotion);
                else if(mind == 1)
                    local.setImageResource(R.drawable.new_emotion_2);
                else if (mind == 2)
                    local.setImageResource(R.drawable.new_emotion_3);
            }

            textView.setText(text);

            //return inflater.inflate(R.layout.activity_detail, container, false);

        }
        return v;
    }
}
