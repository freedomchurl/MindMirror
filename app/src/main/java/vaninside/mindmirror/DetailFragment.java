package vaninside.mindmirror;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import vaninside.mindmirror.Model.CreateViewToBitmap;

// Detail Page

public class DetailFragment extends Fragment {
    int mind = 0;

    Context context;
    String currentDay;
    String text;

    private TextView dayTextView;
    private TextView dayOfweekTextView;
    private ImageButton instagram;

    private ImageButton Kakao;

    CreateViewToBitmap shareManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.detail, container, false);
        final LinearLayout mind_layout = (LinearLayout) v.findViewById(R.id.mind_layout);
/*
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9) ;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,width);
        mind_layout.setLayoutParams(params);
*/
        context = getActivity();

        ImageView mindImage = (ImageView) v.findViewById(R.id.mind_image);
    /*
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER;
        mindImage.setLayoutParams(params2);
      */
        TextView textView = (TextView) v.findViewById(R.id.detail_memo);
        //final LinearLayout mind_layout = (LinearLayout) v.findViewById(R.id.mind_layout);

        if (getArguments() != null)
            currentDay = getArguments().getString("currentDay");

        dayTextView = (TextView) v.findViewById(R.id.day_textview);
        dayOfweekTextView = (TextView) v.findViewById(R.id.dayofweek_textview);
        instagram = (ImageButton) v.findViewById(R.id.instagram);

        this.Kakao = (ImageButton) v.findViewById(R.id.kakao);


        int today = Integer.parseInt(currentDay.substring(6, 8));
        dayTextView.setText(Integer.toString(today));

        GregorianCalendar myCalendar = new GregorianCalendar(Integer.parseInt(currentDay.substring(0, 4)), Integer.parseInt(currentDay.substring(4, 6)) - 1, Integer.parseInt(currentDay.substring(6, 8)), 0, 0, 0);
        int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekStr = "";

        switch (dayOfWeek) {
            case 1:
                dayOfWeekStr = "SUN";
                break;
            case 2:
                dayOfWeekStr = "MON";
                break;
            case 3:
                dayOfWeekStr = "TUE";
                break;
            case 4:
                dayOfWeekStr = "WED";
                break;
            case 5:
                dayOfWeekStr = "THU";
                break;
            case 6:
                dayOfWeekStr = "FRI";
                break;
            case 7:
                dayOfWeekStr = "SAT";
                break;
        }

        dayOfweekTextView.setText(dayOfWeekStr);


        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dayTextView.setVisibility(View.INVISIBLE);
                //dayOfweekTextView.setVisibility(View.INVISIBLE);
                Bitmap shareImage = shareManager.createViewToBitmap(context, (View) mind_layout, currentDay);
                //dayTextView.setVisibility(View.VISIBLE);
                //dayOfweekTextView.setVisibility(View.VISIBLE);
                shareInstagram(shareImage);

            }
        });

        this.Kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dayTextView.setVisibility(View.INVISIBLE);
                //dayOfweekTextView.setVisibility(View.INVISIBLE);
                Bitmap shareImage = shareManager.createViewToBitmap(context, (View) mind_layout, currentDay);
                //dayTextView.setVisibility(View.VISIBLE);
                //dayOfweekTextView.setVisibility(View.VISIBLE);
                shareKakao(shareImage);

            }
        });

        if (context != null) {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

            String sqlSelect = "SELECT * FROM mind_data WHERE date=" + currentDay;

            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    mind = cursor.getInt(2);
                    text = cursor.getString(3);
                }

                // ----------------------- 내 감정에 따라 감정 사진 설정.
                if (mind == 1)
                    mindImage.setImageResource(R.drawable.new_emotion_1);
                else if (mind == 2)
                    mindImage.setImageResource(R.drawable.new_emotion_2);
                else if (mind == 3)
                    mindImage.setImageResource(R.drawable.new_emotion_3);
                else if (mind == 4)
                    mindImage.setImageResource(R.drawable.new_emotion_4);
                else if (mind == 5)
                    mindImage.setImageResource(R.drawable.new_emotion_5);
                else if (mind == 6)
                    mindImage.setImageResource(R.drawable.new_emotion_6);
                else if (mind == 7)
                    mindImage.setImageResource(R.drawable.new_emotion_7);
                else if (mind == 8)
                    mindImage.setImageResource(R.drawable.new_emotion_8);
                else if (mind == 9)
                    mindImage.setImageResource(R.drawable.new_emotion_9);
                else if (mind == 10)
                    mindImage.setImageResource(R.drawable.new_emotion_10);
            }

            textView.setText(text);
        }

        return v;
    }


    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1;
    boolean permissionCheck = false;

    public void onRequestPermission() {
        int permissionReadStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_CODE);
        } else {
            permissionCheck = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "허용했으니 가능함", Toast.LENGTH_LONG).show();
                            permissionCheck = true;
                        } else {
                            Toast.makeText(context, "허용하지 않으면 공유못함", Toast.LENGTH_LONG).show();
                            permissionCheck = false;
                        }
                    }
                }

                break;
        }
    }

    public void shareInstagram(Bitmap bitmap) {
        onRequestPermission();
        if (permissionCheck) {
            String fileName = "share.png";
            File filePath = shareManager.saveBitmap(bitmap, fileName);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File test = new File(filePath, fileName);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationInfo().packageName + ".provider", new File(filePath, fileName));

            try {
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage("com.instagram.android");
                context.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(share);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "인스타그램 설치 안 됨", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void shareKakao(Bitmap bitmap) {
        onRequestPermission();
        /*
        TemplateParams params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(
                        "디저트 사진",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build())
                        .setDescrption("아메리카노, 빵, 케익")
                        .build())
                .setSocial(SocialObject.newBuilder()
                        .setLikeCount(10)
                        .setCommentCount(20)
                        .setSharedCount(30)
                        .setViewCount(40)
                        .build())
                .addButton(new ButtonObject(
                        "웹에서 보기",
                        LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .build()))
                .addButton(new ButtonObject(
                        "앱에서 보기",
                        LinkObject.newBuilder()
                                .setAndroidExecutionParams("key1=value1")
                                .setIosExecutionParams("key1=value1")
                                .build()))
                .build();
*/
        if (permissionCheck) {
            String fileName = "share.png";
            File filePath = shareManager.saveBitmap(bitmap, fileName);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File imageFile = new File(filePath, fileName);

            //File imageFile = new File("{로컬 이미지 파일 경로}");

            //final String fileURL = null;
            KakaoLinkService.getInstance()
                    .uploadImage(this.context, true, imageFile, new ResponseCallback<ImageUploadResponse>() {
                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "이미지 업로드 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(ImageUploadResponse result) {
                            Log.i("KAKAO_API", "이미지 업로드 성공");

                            Log.d("KAKAO_API", "URL: " + result.getOriginal().getUrl());

                            // TODO: 템플릿 컨텐츠로 이미지 URL 입력
                            String kYear = currentDay.substring(0, 4);
                            String kMonth = currentDay.substring(4, 6);
                            String kDate = currentDay.substring(6, 8);

                            TemplateParams params = FeedTemplate
                                    .newBuilder(ContentObject.newBuilder(
                                            "나의 " + kYear + "년 " + kMonth + "월 " + kDate + "일" + "  감정 일기",
                                            result.getOriginal().getUrl(),
                                            LinkObject.newBuilder()
                                                    .setWebUrl("https://developers.kakao.com")
                                                    .setMobileWebUrl("https://developers.kakao.com")
                                                    .build())
                                            .setDescrption(text)
                                            .build())
                                    .addButton(new ButtonObject(
                                            "앱에서 보기",
                                            LinkObject.newBuilder()
                                                    .setAndroidExecutionParams("key1=value1")
                                                    .setIosExecutionParams("key1=value1")
                                                    .build()))
                                    .build();

                            KakaoLinkService.getInstance()
                                    .sendDefault(context, params, new ResponseCallback<KakaoLinkResponse>() {
                                        @Override
                                        public void onFailure(ErrorResult errorResult) {
                                            Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                                        }

                                        //ddd
                                        @Override
                                        public void onSuccess(KakaoLinkResponse result) {
                                            Log.i("KAKAO_API", "카카오링크 공유 성공");

                                            // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                            Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                                            Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
                                        }
                                    });
                        }
                    });

        }

    }

}
