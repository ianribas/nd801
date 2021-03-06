package com.example.ianribas.mypopularmovies.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.ianribas.mypopularmovies.AbstractNetworkAwareActivity;
import com.example.ianribas.mypopularmovies.ApplicationModule;
import com.example.ianribas.mypopularmovies.R;
import com.example.ianribas.mypopularmovies.movielist.MovieListActivity;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AbstractNetworkAwareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mOfflineView = findViewById(R.id.layout_offline);

        DaggerMovieDetailComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .build().inject(this);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            setupFragment();
        }
    }

    private void setupFragment() {
        Fragment fragment = MovieDetailFragment.create(
                getIntent().getLongExtra(MovieDetailFragment.ARG_MOVIE_ID, 0L));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetworkUnavailable() {
        super.onNetworkUnavailable();

        findViewById(R.id.movie_detail_container).setVisibility(View.GONE);
        mOfflineView.setVisibility(View.VISIBLE);

        findViewById(R.id.button_try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNetworkAvailable();
            }
        });
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();

        findViewById(R.id.movie_detail_container).setVisibility(View.VISIBLE);
        mOfflineView.setVisibility(View.GONE);
        setupFragment();
    }
}
