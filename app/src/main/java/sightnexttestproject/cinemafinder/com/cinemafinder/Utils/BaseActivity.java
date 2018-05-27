package sightnexttestproject.cinemafinder.com.cinemafinder.Utils;


import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import sightnexttestproject.cinemafinder.com.cinemafinder.R;


public class BaseActivity extends AppCompatActivity {
    MyTextView textViewTitle;
    Toolbar customToolbar;

    static ConnectivityManager CM;
    static NetworkInfo ninfo;

    //    public static String ACTIVITY_TITLE = "DJCSI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setupToolbar(){
        customToolbar=(Toolbar)findViewById(R.id.home_toolbar);
        textViewTitle = (MyTextView) customToolbar.findViewById(R.id.home_toolbar_title);
        customToolbar.setTitle("");
        setSupportActionBar(customToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        CM = (ConnectivityManager) BaseActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        ninfo = CM.getActiveNetworkInfo();
        if (!(ninfo != null && ninfo.isConnected())) {
            new AlertDialog.Builder(BaseActivity.this).setTitle("Alert").setCancelable(false).setMessage("Please connect to the Internet.").setPositiveButton("Okay.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            }).show();
        }
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }


}
