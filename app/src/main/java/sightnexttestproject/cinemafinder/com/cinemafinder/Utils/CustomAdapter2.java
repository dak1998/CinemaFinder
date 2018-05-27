package sightnexttestproject.cinemafinder.com.cinemafinder.Utils;

/**
 * Created by Owner on 26-May-18.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;

import java.util.ArrayList;

import sightnexttestproject.cinemafinder.com.cinemafinder.R;

public class CustomAdapter2 extends ArrayAdapter<String> {

    public ArrayList<String> title;
    public ArrayList<String> subtitle;
    public ArrayList<String> poster;
    ArrayList<Float> rating;

    public Activity context;

    public CustomAdapter2 (Activity context, ArrayList<String> title, ArrayList<String> subtitle,
                           ArrayList<String> poster, ArrayList<Float> rating){
        super(context, R.layout.list2,title);
        this.context=context;
        this.title=title;
        this.subtitle=subtitle;
        this.poster=poster;
        this.rating=rating;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(R.layout.list2, null, true);

        Log.e("mn1",String.valueOf(position));

        MyTextViewBold titleTV=(MyTextViewBold) row.findViewById(R.id.LMtextViewTitle);
        MyTextView subtitleTV=(MyTextView) row.findViewById(R.id.LMtextViewSubtitle);
        TopAlignedImageView posterTIV = (TopAlignedImageView) row.findViewById(R.id.LMimageViewRow);
        CardView cv=(CardView)row.findViewById(R.id.LMCardView);
        RatingBar ratingBar = (RatingBar) row.findViewById(R.id.LMRatingBar);


        titleTV.setText(title.get(position));
        subtitleTV.setText(subtitle.get(position));

        ratingBar.setRating(rating.get(position));

        int finalWidth = context.getWindowManager().getDefaultDisplay().getHeight();
        int finalHeight=finalWidth/2;

        posterTIV.getLayoutParams().height = finalHeight;
        Bitmap bitmap=DisplayBitmaps.getPhoto(poster.get(position), finalWidth, finalHeight, context);
        posterTIV.setImageBitmap(bitmap);

        return row;
    }
}