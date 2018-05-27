package sightnexttestproject.cinemafinder.com.cinemafinder.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;

/**
 * Created by Owner on 27-Dec-17.
 */

public class DisplayBitmaps {

    public static Bitmap getPhoto(String selectedImage, int imageWidt, int imageHeigh, Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap photoBitmap = null;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        byte[] encodeByte = Base64.decode(selectedImage, Base64.DEFAULT);
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, bitmapOptions);
        //BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        int imageWidth = bitmapOptions.outWidth;
        int imageHeight = bitmapOptions.outHeight;

        float scale = 1.0f;

        if (imageWidth < imageHeight) {
            if (imageHeight > width * 1.0f) {
                scale =width * 1.0f / (imageHeight * 1.0f);
            }

        } else {
            if (imageWidth > width * 1.0f) {
                scale = width * 1.0f / (imageWidth * 1.0f);
            }

        }

        photoBitmap = decodeSampledBitmapFromResource(selectedImage, (int) ((imageWidt * scale)/1.5), (int) ((imageHeigh * scale)/1.5));
        return photoBitmap;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        System.out.println("imgdimen "+String.valueOf(reqHeight)+"xx"+String.valueOf(reqWidth));

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = Math.min(heightRatio, widthRatio);
            // inSampleSize = heightRatio < widthRatio ? heightRatio :
            // widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String encodedString, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig= Bitmap.Config.RGB_565;
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }
}

