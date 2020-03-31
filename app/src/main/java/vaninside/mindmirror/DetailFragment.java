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
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button instagram;


    CreateViewToBitmap shareManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.detail, container, false);

        context = getActivity();
        ImageView mindImage = (ImageView) v.findViewById(R.id.mind_image);
        TextView textView = (TextView) v.findViewById(R.id.detail_memo);

        if (getArguments() != null)
            currentDay = getArguments().getString("currentDay");

        dayTextView = (TextView) v.findViewById(R.id.day_textview);
        dayOfweekTextView = (TextView) v.findViewById(R.id.dayofweek_textview);
        instagram = (Button) v.findViewById(R.id.instagram);

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
                Bitmap shareImage = shareManager.createViewToBitmap(context, v);
                shareInstagram(shareImage);

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
                if (mind == 0)
                    mindImage.setImageResource(R.drawable.new_emotion);
                else if (mind == 1)
                    mindImage.setImageResource(R.drawable.new_emotion_2);
                else if (mind == 2)
                    mindImage.setImageResource(R.drawable.new_emotion_3);
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


}