package vaninside.mindmirror.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import vaninside.mindmirror.R;

public class CreateViewToBitmap {

    public CreateViewToBitmap(){

    }

    public static Bitmap createViewToBitmap(Context context, View view) {
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
        canvas2.drawBitmap(sourceBitmap, sourceRect, destinationRect, null);

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
