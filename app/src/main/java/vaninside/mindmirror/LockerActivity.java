package vaninside.mindmirror;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import static com.kakao.util.helper.Utility.getPackageInfo;

//import static com.kakao.util.helper.Utility.getPackageInfo;

public class LockerActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[] bKeyNum = new Button[10];
    private Button bBackKey = null;
    private String passNum = "";
    private String myPass = null;

    private int lockerType = 0;

    private TextView tPassText = null;

    private ImageView [] pwView = new ImageView[4];

    private int isFirst = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        this.tPassText = (TextView) findViewById(R.id.enterPassText);
        lockerType = getIntent().getIntExtra("lockerType",0); // 기본적으로 0
        /*
        0 - 일반 잠금 해제
        1 - 잠금 모드 취소
        2 - 잠금 모드 설정
         */
        InitViewType();

        //최종 커밋

        InitKeyNum();
        //String keyHash = getKeyHash(getApplicationContext());
        //Log.d("KEYHAST Value",keyHash);
    }
    /*
    public static String getKeyHash(final Context context) {

        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("HASHKEY", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }

        return null;
    }
*/
    private void InitViewType()
    {
        if(lockerType == 0)
        {
            // 일반적인 기존구현되어있는 View가 나온다.
            // Nothing.
        }
        else if(lockerType == 1)
        {
            // 현재 비밀번호를 입력하세요.
            this.tPassText.setText("현재 비밀번호를 입력하세요.");
        }
        else if(lockerType == 2)
        {
            this.tPassText.setText("새로운 비밀번호를 입력하세요.");
        }
    }

    private void InitKeyNum()
    {
        pwView[0] = (ImageView) findViewById(R.id.pw_1);
        pwView[1] = (ImageView) findViewById(R.id.pw_2);
        pwView[2] = (ImageView) findViewById(R.id.pw_3);
        pwView[3] = (ImageView) findViewById(R.id.pw_4);

        bKeyNum[0] = (Button)findViewById(R.id.keypad0);
        bKeyNum[1] = (Button)findViewById(R.id.keypad1);
        bKeyNum[2] = (Button)findViewById(R.id.keypad2);
        bKeyNum[3] = (Button)findViewById(R.id.keypad3);
        bKeyNum[4] = (Button)findViewById(R.id.keypad4);
        bKeyNum[5] = (Button)findViewById(R.id.keypad5);
        bKeyNum[6] = (Button)findViewById(R.id.keypad6);
        bKeyNum[7] = (Button)findViewById(R.id.keypad7);
        bKeyNum[8] = (Button)findViewById(R.id.keypad8);
        bKeyNum[9] = (Button)findViewById(R.id.keypad9);

        bBackKey = (Button) findViewById(R.id.keypadback);

        for(int i=0;i<10;i++){
            bKeyNum[i].setOnClickListener(LockerActivity.this);
        }
        bBackKey.setOnClickListener(LockerActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        //Toast myToast = Toast.makeText(this.getApplicationContext(), "AAA", Toast.LENGTH_SHORT);
        //myToast.show();
        int clicked_value = -2;
        switch(view.getId()){
            case R.id.keypad0 :
                clicked_value = 0;
                break;
            case R.id.keypad1:
                clicked_value = 1;
                break;
            case R.id.keypad2 :
                clicked_value = 2;
                break;
            case R.id.keypad3 :
                clicked_value = 3;
                break;
            case R.id.keypad4 :
                clicked_value = 4;
                break;
            case R.id.keypad5:
                clicked_value = 5;
                break;
            case R.id.keypad6 :
                clicked_value = 6;
                break;
            case R.id.keypad7:
                clicked_value = 7;
                break;
            case R.id.keypad8:
                clicked_value = 8;
                break;
            case R.id.keypad9:
                clicked_value = 9;
                break;
            case R.id.keypadback:
                clicked_value = -1;
                break;
        }

        Log.d("Clicked Value",String.valueOf(clicked_value));
        if(clicked_value == -1)
        {
            int passLength = passNum.length();
            if(passLength != 0)
                passNum = passNum.substring(0,passLength-1); // 그 이전까지 잘라낸다. 하나 지워짐.
            Log.d("mypass2",passNum);
        }
        else if(clicked_value >=0 && clicked_value <=9)
        {
            passNum = passNum + clicked_value; // 글자 하나 추가.
            Log.d("mypass",passNum);


            if(passNum.length() >= 4) // 길이가 다 차면
            {
                if(lockerType == 2)
                {
                    // 비밀번호를 새로 설정한다.
                    if(this.isFirst == 0)
                    {
                        myPass = passNum; // 일단 임시로 저장.
                        passNum = "";
                        this.isFirst++;
                        this.tPassText.setText("비밀번호를 한번 더 입력하세요.");
                    }
                    else if(this.isFirst == 1)
                    {
                        if(myPass.equals(passNum))
                        {
                            // 비밀번호가 설정되었다.
                            SharedPreferences sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
                            //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("lockmode",true);
                            editor.putString("locker_pass",passNum); // key, value를 이용하여 저장하는 형
                            editor.commit();

                            Toast toast = Toast.makeText(getApplicationContext(),"비밀번호가 설정되었습니다.",Toast.LENGTH_SHORT);
                            toast.show();
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                        else
                        {
                            // 같지않다면,
                            Toast toast = Toast.makeText(getApplicationContext(),"비밀번호가 다릅니다.",Toast.LENGTH_SHORT);
                            toast.show();
                            this.isFirst = 0;
                            this.tPassText.setText("새로운 비밀번호를 입력하세요.");
                            passNum = "";
                        }
                    }
                }
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences("mind_key", MODE_PRIVATE);

                    //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    //String text = editText.getText().toString(); // 사용자가 입력한 저장할 데이터
                    myPass = sharedPreferences.getString("locker_pass", "0000");

                    if (passNum.equals(myPass)) {
                        if (lockerType == 0) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);//액티비티를 메인으로 넘긴다.
                            finish();
                        } else if (lockerType == 1) {
                            // 비밀번호 해제.

                            // 비밀번호가 설정되었다.
                            sharedPreferences = getSharedPreferences("mind_key",MODE_PRIVATE);
                            //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("lockmode",false);
                            //editor.putString("locker_pass",passNum); // key, value를 이용하여 저장하는 형
                            editor.commit();

                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    } else {
                        Toast failLogin = Toast.makeText(this.getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT);
                        failLogin.show();
                        passNum = "";
                    }
                    //editor.putString("lokcer_pass","1234"); // key, value를 이용하여 저장하는 형태
                }

            }
        }

        resetPassView();

    }

    public void resetPassView()
    {
        /*
        비밀번호 뷰를 초기화.
         */
        int passLen = passNum.length(); // 길이를 받아온다.

        if(passLen == 0)
        {
            for(int i=0;i<4;i++)
            {
                this.pwView[i].setImageResource(R.drawable.default_pw);
            }
        }
        else if(passLen == 1)
        {
            this.pwView[0].setImageResource(R.drawable.new_emotion_2);
            for(int i=1;i<4;i++)
            {
                this.pwView[i].setImageResource(R.drawable.default_pw);
            }
        }
        else if(passLen == 2)
        {
            this.pwView[0].setImageResource(R.drawable.new_emotion_2);
            this.pwView[1].setImageResource(R.drawable.new_emotion_4);

            for(int i=2;i<4;i++)
            {
                this.pwView[i].setImageResource(R.drawable.default_pw);
            }
        }
        else if(passLen == 3)
        {
            this.pwView[0].setImageResource(R.drawable.new_emotion_2);
            this.pwView[1].setImageResource(R.drawable.new_emotion_4);
            this.pwView[2].setImageResource(R.drawable.new_emotion_1);

            for(int i=3;i<4;i++)
            {
                this.pwView[i].setImageResource(R.drawable.default_pw);
            }
        }
        else if(passLen == 4)
        {
            this.pwView[0].setImageResource(R.drawable.new_emotion_2);
            this.pwView[1].setImageResource(R.drawable.new_emotion_4);
            this.pwView[2].setImageResource(R.drawable.new_emotion_1);
            this.pwView[3].setImageResource(R.drawable.new_emotion_3);

        }

    }
}
