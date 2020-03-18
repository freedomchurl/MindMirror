package vaninside.mindmirror;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class DetailActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Afragment fragmentA;
    private Bfragment fragmentB;
    private FragmentTransaction transaction;
    private boolean isFragmentB= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 70%

        int height = (int) (display.getHeight() * 0.8);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;


        fragmentManager = getSupportFragmentManager();

        fragmentA = new Afragment();
        fragmentB = new Bfragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, fragmentA).commitAllowingStateLoss();

        Button button = (Button) findViewById(R.id.mybutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();

            }
        });
    };

    public void switchFragment(){
        Fragment fr;
        if(isFragmentB){
            fr = new Afragment();
        } else {
            fr = new Bfragment();
        }


        isFragmentB = (isFragmentB) ? false : true;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame, fr);
        transaction.commit();

    }




}
