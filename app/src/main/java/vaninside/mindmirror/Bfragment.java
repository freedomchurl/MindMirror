package vaninside.mindmirror;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


// Detail Edit Page
public class Bfragment extends Fragment {
    private InteractListener interactListener;

    Context context;
    String currentDay;
    int mind;
    String text= "";
    EditText editText;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    boolean imageEditCheck = false;
    View.OnClickListener emotionClick;
    boolean isExist = false;

    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_edit, container, false);

        context = getActivity();
        currentDay = getArguments().getString("currentDay");

        final ImageView imageView = (ImageView) v.findViewById(R.id.mind_detail_edit_image);
        editText = (EditText) v.findViewById(R.id.detail_editText);


        imageView1 = (ImageView) v.findViewById(R.id.mind_1);
        imageView2 = (ImageView) v.findViewById(R.id.mind_2);
        imageView3 = (ImageView) v.findViewById(R.id.mind_3);

        db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);


        // NAME 컬럼 값이 'ppotta'인 모든 데이터 조회.
        String sqlSelect = "SELECT * FROM mind_data WHERE date=" + currentDay;

        Cursor cursor = db.rawQuery(sqlSelect, null);
        if (cursor != null) {
            if(cursor.getCount() == 0) {
               isExist = false;
            }else{
                isExist = true;
            while (cursor.moveToNext()) {
                // INTEGER로 선언된 첫 번째 "NO" 컬럼 값 가져오기.
                mind = cursor.getInt(2);
                text = cursor.getString(3);
            }

            editText.setText(text);

            if (mind == 0)
                imageView.setImageResource(R.drawable.new_emotion);
            else if (mind == 1)
                imageView.setImageResource(R.drawable.new_emotion_2);
            else if (mind == 2)
                imageView.setImageResource(R.drawable.new_emotion_3);


           // db.execSQL("UPDATE mind_data SET text="+editText.getText()+" WHERE date="+currentDay+";");
        }}


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageEditCheck) { // 버튼 다시 누르면 안보이게
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageEditCheck = false;
                }
                else { // 버튼 누르면 보이게
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageEditCheck = true;
                }
            }
        });

        emotionClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.mind_1){
                    mind = 1;
                    imageView.setImageResource(R.drawable.new_emotion_2);}
                else if(v.getId() == R.id.mind_2){
                    mind =2;
                    imageView.setImageResource(R.drawable.new_emotion_3);}
                else if(v.getId() == R.id.mind_3){
                    mind=3;
                    imageView.setImageResource(R.drawable.new_emotion_4);}


// 눌렀으면 감정들 사라지게 하고.
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageEditCheck = false;

                // 감정 이모티콘을 업데이트 해야지.
               // db.execSQL("UPDATE mind_data SET mind="+mind+" WHERE date="+currentDay+";");


            }


        };
        //db.execSQL("INSERT INTO mind_data(date, mind, text) values (20200320, 2, '바보 철')");
//        db.execSQL("INSERT INTO mind_data(date, text) values (20200320,'바보 철')");
        //interactListener.interact(mind, text);

        imageView1.setOnClickListener(emotionClick);
        imageView2.setOnClickListener(emotionClick);
        imageView3.setOnClickListener(emotionClick);
        return v;
    }

    public interface InteractListener {
        void interact(int mind, String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof InteractListener) {
            interactListener = (InteractListener) context;

        }else {
            throw new RuntimeException(context.toString() + " must implement InteractListener");
        }
    }

    @Override
    public void onPause() {
        text = editText.getText().toString();
        interactListener.interact(mind, text);


        db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

if(isExist)
        db.execSQL("UPDATE mind_data SET text='"+editText.getText().toString()+"', mind = "+mind+" WHERE date="+currentDay+";");
else {
    Log.d("data list", "hello " + currentDay + " " + mind + " " + text + " bye");
    db.execSQL("INSERT INTO mind_data(date, mind, text) values (" + currentDay + "," + mind + ",'" + text + "');");
}   db.close();
        super.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactListener = null;
    }
}