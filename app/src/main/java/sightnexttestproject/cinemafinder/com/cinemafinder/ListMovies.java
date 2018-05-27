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
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.CustomAdapter2;

public class ListMovies extends BaseActivity {

    String location, cinema, selectedDate;

    ArrayList<String> names, desc, poster, namesForAdapter;
    ArrayList<Float> rating;

    DatabaseReference locationDbRef, moviesDbRef;

    @BindView(R.id.listMovies) ListView mListView;
    @BindView(R.id.listMoviesProgBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        ButterKnife.bind(this);

        setupToolbar();

        location = getIntent().getStringExtra("location");
        cinema = getIntent().getStringExtra("cinemaName");
        selectedDate = getIntent().getStringExtra("date");

        setTitle("Movies at "+ cinema +" on "+selectedDate);

        mListView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        locationDbRef = FirebaseDatabase.getInstance().getReference("locations");
        moviesDbRef = FirebaseDatabase.getInstance().getReference("movies");

        locationDbRef.child(location).child(cinema).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                names = new ArrayList<>();
                namesForAdapter = new ArrayList<String>();
                desc = new ArrayList<>();
                poster = new ArrayList<>();
                rating = new ArrayList<>();

                names = (ArrayList<String>) dataSnapshot.getValue();


                for (  final String movieName : names ) {

                    moviesDbRef.child(movieName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            namesForAdapter.add(movieName);
                            poster.add(dataSnapshot.child("poster").getValue(String.class));
                            rating.add(Float.parseFloat(dataSnapshot.child("rating").getValue(String.class)));
                            desc.add(dataSnapshot.child("desc").child("about").getValue(String.class));

                            mProgressBar.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);

                            CustomAdapter2 ca2 = new CustomAdapter2(ListMovies.this, namesForAdapter , desc, poster, rating);
                            mListView.setAdapter(ca2);

                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(ListMovies.this, MoviesAdapter.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("movieNames", names);
                                    bundle.putInt("pos", position);
                                    intent.putExtra("bundle", bundle);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
