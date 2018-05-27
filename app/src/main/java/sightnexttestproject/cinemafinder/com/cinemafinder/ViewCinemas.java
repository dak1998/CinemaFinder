package sightnexttestproject.cinemafinder.com.cinemafinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.BaseActivity;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.CustomAdapter1;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.MyTextViewBold;

public class ViewCinemas extends BaseActivity {

    String location, selectedDate;

    ArrayList<String> cinemaNames;

    DatabaseReference locationDbRef;

    @BindView(R.id.ViewCinemasListView) ListView mListView;
    @BindView(R.id.ViewCinemasProgBar)  ProgressBar mProgressBar;
    @BindView(R.id.dateVC) MyTextViewBold dateHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cinemas);

        setupToolbar();
        setTitle("Select a Cinema");

        ButterKnife.bind(this);

        location = getIntent().getStringExtra("location");
        selectedDate = getIntent().getStringExtra("date");

        dateHolder.setText("Date: "+selectedDate);

        mListView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);



        locationDbRef = FirebaseDatabase.getInstance().getReference("locations");

        locationDbRef.child(location).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cinemaNames = new ArrayList<>();

                for( DataSnapshot childSnapshot : dataSnapshot.getChildren() )
                    cinemaNames.add(childSnapshot.getKey());

                Log.e("cn", cinemaNames.toString());

                CustomAdapter1 ca1 = new CustomAdapter1(ViewCinemas.this, cinemaNames);
                mListView.setAdapter(ca1);

                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ViewCinemas.this, ListMovies.class);
                        intent.putExtra("location", location);
                        intent.putExtra("cinemaName", cinemaNames.get(position));
                        Log.e("cn1", cinemaNames.get(position));
                        intent.putExtra("date", selectedDate);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
