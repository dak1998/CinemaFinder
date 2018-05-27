package sightnexttestproject.cinemafinder.com.cinemafinder.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;

import android.util.AttributeSet;
import android.view.View;


public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    public MyTextView(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);
        init();
    }

    public MyTextView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    public MyTextView(Context context){
        super(context);
        init();
    }

    public void init()
    {
        Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"fonts/Raleway-Regular.ttf");
        setTypeface(tf);
    }
}