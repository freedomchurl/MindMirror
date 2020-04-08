package vaninside.mindmirror.Model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.content.res.ResourcesCompat;
import vaninside.mindmirror.R;

public class CreateViewToBitmap {

    public CreateViewToBitmap(){

    }

    public static Bitmap createViewToBitmap(Context context, View view, String currentDay) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap sourceBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(sourceBitmap);
        view.draw(canvas);

        Bitmap resultBitmap= Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(resultBitmap);
        canvas2.drawColor(context.getResources().getColor(R.color.sketchColor));



        Rect sourceRect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        Rect destinationRect = new Rect((resultBitmap.getWidth() - sourceBitmap.getWidth())/2, resultBitmap.getHeight()-sourceBitmap.getHeight(), (resultBitmap.getWidth() + sourceBitmap.getWidth())/2, resultBitmap.getHeight());
        Rect borderRect = new Rect(0, 0, sourceBitmap.getWidth(), resultBitmap.getHeight());

        canvas2.drawBitmap(sourceBitmap, sourceRect, destinationRect, null);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        paint.setColor(context.getResources().getColor(R.color.colorTitle));
        canvas2.drawRect(borderRect, paint);

        // draw text to the Canvas center
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        float scale = context.getResources().getDisplayMetrics().density;
        paint.setTextSize(100);
        String gText = "";

        int x = (resultBitmap.getWidth()/2);
        int y = (resultBitmap.getHeight()/2) - 150;

        Paint Pnt = new Paint();
        Pnt.setTextSize((int) (40 * scale));
        Pnt.setAntiAlias(true);
        Pnt.setColor(context.getResources().getColor(R.color.intro_text));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.sdmi_saeng);
        if (Build.VERSION.SDK_INT >= 28) {
            // This does only works from SDK 28 and higher
            Typeface typefaceA = ResourcesCompat.getFont(context, R.font.sdmi_saeng);
            typeface = Typeface.create(typefaceA, 700, false);
        } else {
            // This always works (Whole name without .ttf)
            typeface = ResourcesCompat.getFont(context, R.font.sdmi_saeng);
        }
        Pnt.setTypeface(typeface);
        Pnt.setTextAlign(Paint.Align.CENTER);


        Paint highlight = new Paint();

        int mind = 0;
        if (context != null) {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase("mind_calendar.db", Context.MODE_PRIVATE, null);

            String sqlSelect = "SELECT * FROM mind_data WHERE date=" + currentDay;

            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    mind = cursor.getInt(2);
                }

                // ----------------------- 내 감정에 따라 감정 사진 설정.
                if (mind == 1){
                    gText = "행복한 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight1));
                }
                else if (mind == 2){
                    gText = "신나는 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight2));
                }
                else if (mind == 3){
                    gText = "평범한 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight3));
                }
                else if (mind == 4){
                    gText = "설레는 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight4));
                }
                else if (mind == 5){
                    gText = "지루한 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight5));
                }
                else if (mind == 6){
                    gText = "화나는 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight6));
                }
                else if (mind == 7){
                    gText = "곤란한 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight7));
                }
                else if (mind == 8){
                    gText = "아픈 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight8));
                }
                else if (mind == 9){
                    gText = "우울한 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight9));
                }
                else if (mind == 10){
                    gText = "슬픈 하루";
                    highlight.setColor(context.getResources().getColor(R.color.highlight10));
                }
            }

            db.close();
        }


        canvas2.drawText(gText, x, y, Pnt);
        canvas2.drawRect(x-(Pnt.measureText(gText))/2, y-(Pnt.ascent()+Pnt.descent())/2, x+(Pnt.measureText(gText))/2, y+(Pnt.ascent()+Pnt.descent())/2
                , highlight);




        return resultBitmap;
    }

    public static File saveBitmap(Bitmap bitmap, String fileName){
        Bitmap thisBitmap = bitmap;
        String storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String thisFileName = fileName;

        String folderName = "/Mindmirror/";
        String fullPath = storage + folderName;
        File filePath = null;

        try {
            filePath = new File(fullPath);
            if (!filePath.isDirectory()) {
                filePath.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(fullPath + fileName);
            thisBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        return filePath;
    }
}
