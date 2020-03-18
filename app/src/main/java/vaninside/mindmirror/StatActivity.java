package vaninside.mindmirror;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vaninside.mindmirror.Model.StatData;

public class StatActivity extends AppCompatActivity {

    private StatAdapter adapter;
    public ArrayList<Object> mStatList = new ArrayList<>();

    public ArrayList<String> arrayList;
    public ArrayAdapter<String> arrayAdapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);


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


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        init();
        getData();
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.statRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new StatAdapter(getData());
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<StatData> getData(){
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
    }
}
