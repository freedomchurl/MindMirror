package vaninside.mindmirror;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

// Detail Edit Page
public class DetailEditFragment extends Fragment {

    Context context;
    String currentDay;

    int mind = 0;
    String text= "";

    private TextView dayTextView;
    private TextView dayOfweekTextView;
    public static EditText editText;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView imageView10;
    boolean imageEditCheck = false;
    View.OnClickListener emotionClick;
    boolean isExist = false;

    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_edit, container, false);

        context = getActivity();
        currentDay = getArguments().getString("currentDay");

        final ImageView imageView = (ImageView) v.findViewById(R.id.mind_detail_edit_image);
        editText = (EditText) v.findViewById(R.id.detail_editText);

        // -------------------- 감정 옵션.
        imageView1 = (ImageView) v.findViewById(R.id.mind_1);
        imageView2 = (ImageView) v.findViewById(R.id.mind_2);
        imageView3 = (ImageView) v.findViewById(R.id.mind_3);
        imageView4 = (ImageView) v.findViewById(R.id.mind_4);
        imageView5 = (ImageView) v.findViewById(R.id.mind_5);
        imageView6 = (ImageView) v.findViewById(R.id.mind_6);
        imageView7 = (ImageView) v.findViewById(R.id.mind_7);
        imageView8 = (ImageView) v.findViewById(R.id.mind_8);
        imageView9 = (ImageView) v.findViewById(R.id.mind_9);
        imageView10 = (ImageView) v.findViewById(R.id.mind_10);

        dayTextView = (TextView) v.findViewById(R.id.edit_day_textview);
        dayOfweekTextView = (TextView) v.findViewById(R.id.edit_dayofweek_textview);

        int today = Integer.parseInt(currentDay.substring(6,8));

        dayTextView.setText(Integer.toString(today));

        GregorianCalendar myCalendar = new GregorianCalendar(Integer.parseInt(currentDay.substring(0,4)), Integer.parseInt(currentDay.substring(4,6))-1, Integer.parseInt(currentDay.substring(6,8)), 0, 0, 0);
        int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekStr="";

        switch (dayOfWeek){
            case 1 :
                dayOfWeekStr = "SUN";
                break;
            case 2 :
                dayOfWeekStr = "MON";
                break;
            case 3 :
                dayOfWeekStr = "TUE";
                break;
            case 4 :
                dayOfWeekStr = "WED";
                break;
            case 5 :
                dayOfWeekStr = "THU";
                break;
            case 6 :
                dayOfWeekStr = "FRI";
                break;
            case 7 :
                dayOfWeekStr = "SAT";
                break;
        }
        dayOfweekTextView.setText(dayOfWeekStr);

        db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

        String sqlSelect = "SELECT * FROM mind_data WHERE date=" + currentDay;

        Cursor cursor = db.rawQuery(sqlSelect, null);
        if (cursor != null) {
            if(cursor.getCount() == 0) {
               isExist = false;
            }else{
                isExist = true;
            while (cursor.moveToNext()) {
                mind = cursor.getInt(2);
                text = cursor.getString(3);
            }

            editText.setText(text);

            // ---------------- 저장된 mind 따라서 사진 표시해주기.
                if (mind == 1)
                    imageView.setImageResource(R.drawable.new_emotion_1);
                else if (mind == 2)
                    imageView.setImageResource(R.drawable.new_emotion_2);
                else if (mind == 3)
                    imageView.setImageResource(R.drawable.new_emotion_3);
                else if (mind == 4)
                    imageView.setImageResource(R.drawable.new_emotion_4);
                else if (mind == 5)
                    imageView.setImageResource(R.drawable.new_emotion_5);
                else if (mind == 6)
                    imageView.setImageResource(R.drawable.new_emotion_6);
                else if (mind == 7)
                    imageView.setImageResource(R.drawable.new_emotion_7);
                else if (mind == 8)
                    imageView.setImageResource(R.drawable.new_emotion_8);
                else if (mind == 9)
                    imageView.setImageResource(R.drawable.new_emotion_9);
                else if (mind == 10)
                    imageView.setImageResource(R.drawable.new_emotion_10);

        }}

        // 감정 수정하기에서 감정 그림을 클릭하면
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageEditCheck) { // 버튼 다시 누르면 옵션 안보이게
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    imageView5.setVisibility(View.INVISIBLE);
                    imageView6.setVisibility(View.INVISIBLE);
                    imageView7.setVisibility(View.INVISIBLE);
                    imageView8.setVisibility(View.INVISIBLE);
                    imageView9.setVisibility(View.INVISIBLE);
                    imageView10.setVisibility(View.INVISIBLE);
                    imageEditCheck = false;
                }
                else { // 버튼 누르면 옵션 보이게
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    imageView5.setVisibility(View.VISIBLE);
                    imageView6.setVisibility(View.VISIBLE);
                    imageView7.setVisibility(View.VISIBLE);
                    imageView8.setVisibility(View.VISIBLE);
                    imageView9.setVisibility(View.VISIBLE);
                    imageView10.setVisibility(View.VISIBLE);
                    imageEditCheck = true;
                }
            }
        });

        // 옵션 클릭하면 내 감정 설정.
        emotionClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.mind_1){
                    mind = 1;
                    imageView.setImageResource(R.drawable.new_emotion_1);}
                else if(v.getId() == R.id.mind_2){
                    mind =2;
                    imageView.setImageResource(R.drawable.new_emotion_2);}
                else if(v.getId() == R.id.mind_3){
                    mind=3;
                    imageView.setImageResource(R.drawable.new_emotion_3);}
                else if(v.getId() == R.id.mind_4){
                    mind=4;
                    imageView.setImageResource(R.drawable.new_emotion_4);}
                else if(v.getId() == R.id.mind_5){
                    mind=5;
                    imageView.setImageResource(R.drawable.new_emotion_5);}
                else if(v.getId() == R.id.mind_6){
                    mind=6;
                    imageView.setImageResource(R.drawable.new_emotion_6);}
                else if(v.getId() == R.id.mind_7){
                    mind=7;
                    imageView.setImageResource(R.drawable.new_emotion_7);}
                else if(v.getId() == R.id.mind_8){
                    mind=8;
                    imageView.setImageResource(R.drawable.new_emotion_8);}
                else if(v.getId() == R.id.mind_9){
                    mind=9;
                    imageView.setImageResource(R.drawable.new_emotion_9);}
                else if(v.getId() == R.id.mind_10){
                    mind=10;
                    imageView.setImageResource(R.drawable.new_emotion_10);}


            }


        };

        imageView1.setOnClickListener(emotionClick);
        imageView2.setOnClickListener(emotionClick);
        imageView3.setOnClickListener(emotionClick);
        imageView4.setOnClickListener(emotionClick);
        imageView5.setOnClickListener(emotionClick);
        imageView6.setOnClickListener(emotionClick);
        imageView7.setOnClickListener(emotionClick);
        imageView8.setOnClickListener(emotionClick);
        imageView9.setOnClickListener(emotionClick);
        imageView10.setOnClickListener(emotionClick);

        db.close();
        return v;
    }




    public int getMind(){
        return mind;
    }

    public void saveData(){
        text = editText.getText().toString();

        db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

        if (isExist)
            db.execSQL("UPDATE mind_data SET text='" + editText.getText().toString() + "', mind = " + mind + " WHERE date=" + currentDay + ";");
        else {
            db.execSQL("INSERT INTO mind_data(date, mind, text) values (" + currentDay + "," + mind + ",'" + text + "');");
        }

        db.close();
    }
}