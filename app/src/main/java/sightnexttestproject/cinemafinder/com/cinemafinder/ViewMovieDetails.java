package sightnexttestproject.cinemafinder.com.cinemafinder;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.DisplayBitmaps;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.MyTextView;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.MyTextViewBold;
import sightnexttestproject.cinemafinder.com.cinemafinder.Utils.TopAlignedImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMovieDetails extends Fragment {


    View view;

    Activity mActivity;

    String movie;

    int finalHeight, finalWidth;

    DatabaseReference moviesDbRef;

    MyTextView actorsTV, directorTV;
    MyTextViewBold aboutTV, movieNameTV;
    TopAlignedImageView mTopAlignedImageView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivity = activity;
    }

    public static ViewMovieDetails newInstance(int page, String movie) {
        ViewMovieDetails fragment = new ViewMovieDetails();
        Bundle args = new Bundle();
        args.putString("movieName", movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getString("movieName");
        }
    }

    public ViewMovieDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_movie_details, container, false);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.htab_collapse_toolbar);
        mTopAlignedImageView = (TopAlignedImageView) view.findViewById(R.id.htab_header);
        actorsTV = (MyTextView) view.findViewById(R.id.VMDActors);
        directorTV = (MyTextView) view.findViewById(R.id.VMDDirector);
        aboutTV = (MyTextViewBold) view.findViewById(R.id.VMDDesc);
        movieNameTV = (MyTextViewBold) view.findViewById(R.id.VMDName);

        finalWidth = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        finalHeight=finalWidth/2;
        mCollapsingToolbarLayout.getLayoutParams().height=finalHeight;
        mTopAlignedImageView.getLayoutParams().height=finalHeight;

        moviesDbRef = FirebaseDatabase.getInstance().getReference("movies");

        moviesDbRef.child(movie).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTopAlignedImageView.setImageBitmap(DisplayBitmaps.
                        getPhoto(dataSnapshot.child("poster").getValue(String.class), finalWidth, finalHeight,
                                mActivity));

                actorsTV.setText("Cast: "+(dataSnapshot.child("desc").child("Actors").getValue(String.class)));
                aboutTV.setText(dataSnapshot.child("desc").child("about").getValue(String.class));
                directorTV.setText("Director: "+dataSnapshot.child("desc").child("Director").getValue(String.class));
                movieNameTV.setText(movie);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
