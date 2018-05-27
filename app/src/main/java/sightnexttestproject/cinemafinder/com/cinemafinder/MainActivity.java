package sightnexttestproject.cinemafinder.com.cinemafinder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.htextview.base.HTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.BaseActivity;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.LocationHelper;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.MyTextViewBold;

public class MainActivity extends BaseActivity implements ConnectionCallbacks,
        OnConnectionFailedListener,OnRequestPermissionsResultCallback {

    DatabaseReference moviesDbRef, locationDbRef, cinemasDbRef;

    ArrayList<String> cinemaNames, moviesNames;

    @BindView(R.id.btnLocation)Button btnProceed;
    @BindView(R.id.helloTV) HTextView helloTextView;
    @BindView(R.id.date_card_view) CardView mCardViewDate;
    @BindView(R.id.location_card_view) CardView mCardViewLocation;
    @BindView(R.id.loadingLL) LinearLayout loadingLayout;
    @BindView(R.id.calendarIcon) ImageView calendarIV;
    @BindView(R.id.dateHolder) MyTextViewBold dateTV;

    String currentLocation;

    private Location mLastLocation;

    double latitude;
    double longitude;

    LocationHelper locationHelper;

    int delay = 2000;
    Handler handler;
    ArrayList<String> arrMessages = new ArrayList<>();
    int position=0;

    int mYear, mMonth, mDay;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setTitle("Cinema Finder");

        locationHelper=new LocationHelper(this);
        locationHelper.checkpermission();

        ButterKnife.bind(this);

        moviesDbRef = FirebaseDatabase.getInstance().getReference("movies");
        cinemasDbRef = FirebaseDatabase.getInstance().getReference("cinemas");
        locationDbRef = FirebaseDatabase.getInstance().getReference("locations");

        loadingLayout.setVisibility(View.GONE);
        mCardViewDate.setVisibility(View.VISIBLE);
        mCardViewLocation.setVisibility(View.VISIBLE);
        helloTextView.setVisibility(View.VISIBLE);

        arrMessages.add("Hello User");
        arrMessages.add("Select A Date");
        arrMessages.add("And");
        arrMessages.add("Click The Button Below");

        helloTextView.animateText(arrMessages.get(position));

        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

                handler.postDelayed(this, delay);
                if(position>=arrMessages.size())
                    position=0;
                helloTextView.animateText(arrMessages.get(position));
                position++;
            }
        }, delay);

        cinemasDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cinemaNames = new ArrayList<>();

                cinemaNames = (ArrayList<String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        moviesDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                moviesNames = new ArrayList<>();

                for ( DataSnapshot childDataSnapshot: dataSnapshot.getChildren() ) {
                    moviesNames.add(childDataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        calendarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                selectedDate = (String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                        + "/" + String.valueOf(year));

                                dateTV.setText(selectedDate);
                                btnProceed.setEnabled(true);

                            }
                        }, mYear, mMonth, mDay);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date today = new Date();
                datePickerDialog.getDatePicker().setMinDate(today.getTime());
                datePickerDialog.show();
            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLastLocation=locationHelper.getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();


                } else {

                    if(btnProceed.isEnabled())
                        btnProceed.setEnabled(false);

                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }
            }
        });

        if (locationHelper.checkPlayServices()) {

            locationHelper.buildGoogleApiClient();
        }

    }


    public void getAddress() {
        Address locationAddress;

        locationAddress = locationHelper.getAddress(latitude, longitude);

        if (locationAddress != null) {

            String address = locationAddress.getAddressLine(0);

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                String temp="";
                int len, count=0;

                if ( (currentLocation.charAt(currentLocation.length()-1)) == ',' )
                    len = (currentLocation.length()-2);
                else
                    len = (currentLocation.length()-1);

                for (int i=len; i>=0; i--){

                    if ( currentLocation.charAt(i) == ',' ) {

                        count++;

                        if (count == 3) {

                            int j = i - 1;

                            while (currentLocation.charAt(j) != ',') {

                                temp += currentLocation.charAt(j);
                                j--;

                            }

                            break;
                        }
                    }
                }

                String finalLocation = new StringBuffer(temp).reverse().toString().trim();
                currentLocation = finalLocation;

                showToast("You are currently at "+currentLocation);

                mCardViewLocation.setVisibility(View.GONE);
                mCardViewDate.setVisibility(View.GONE);
                helloTextView.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);

                locationDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(currentLocation)) {

                            Intent intent = new Intent(MainActivity.this, ViewCinemas.class);
                            intent.putExtra("location", currentLocation);
                            intent.putExtra("date", selectedDate);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                        }
                        else {

                            Collections.shuffle(moviesNames);
                            Collections.shuffle(cinemaNames);

                            int cinemaCount = randInt(4,8);

                            for (int i=0; i<cinemaCount; i++) {

                                if ( cinemaNames.get(i) != null) {

                                    String nameWithLocation = (cinemaNames.get(i) + ", " + currentLocation);


                                    int moviesCount = randInt(3, 6);

                                    ArrayList<String> selectedMovies = new ArrayList<String>();

                                    for (int j = 0; j < moviesCount; j++)
                                        selectedMovies.add(moviesNames.get(j));

                                    locationDbRef.child(currentLocation).child(nameWithLocation).setValue(selectedMovies);
                                }

                            }

                            Intent intent = new Intent(MainActivity.this, ViewCinemas.class);
                            intent.putExtra("location", currentLocation);
                            intent.putExtra("date", selectedDate);
                            loadingLayout.setVisibility(View.GONE);
                            mCardViewLocation.setVisibility(View.VISIBLE);
                            mCardViewDate.setVisibility(View.VISIBLE);
                            helloTextView.setVisibility(View.VISIBLE);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            } else
                showToast("Something went wrong");
        } else
            showToast("Something went wrong");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        mLastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public int randInt(int min, int max) {

        Random randm = new Random();

        int randomNum = randm.nextInt((max - min) + 1) + min;

        return randomNum;
    }


}