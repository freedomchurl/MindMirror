package vaninside.mindmirror.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateViewToBitmap {

    public CreateViewToBitmap(){

    }

    public static Bitmap createViewToBitmap(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
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
