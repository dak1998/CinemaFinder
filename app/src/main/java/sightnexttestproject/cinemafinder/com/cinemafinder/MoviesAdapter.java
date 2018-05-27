package sightnexttestproject.cinemafinder.com.cinemafinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    static int pgcount = 0;
    int pageno;

    static  ArrayList<String> movieNames;

    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_adapter);

        vpPager = (ViewPager) findViewById(R.id.moviesAdapterViewPager);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        movieNames = new ArrayList<>();
        movieNames.addAll(bundle.getStringArrayList("movieNames"));
        pageno = bundle.getInt("pos");
        pgcount = movieNames.size();

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(pageno);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return pgcount;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return ViewMovieDetails.newInstance(position + 1, movieNames.get(position));
        }

    }
}
