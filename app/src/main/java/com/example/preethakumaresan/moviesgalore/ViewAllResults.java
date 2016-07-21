package com.example.preethakumaresan.moviesgalore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class ViewAllResults extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    DbHelper shite = null;

    private static AutoCompleteTextView actv;
    ArrayAdapter adapter;
    EditText reactHere;
    boolean searched = false;
    private Subscription subscription;
    String[] storeTitles;

    List<String> titles = new ArrayList<>();
    String myTitle = "MOVIES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_results);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(myTitle);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initializeAdapter();
        storeTitles= titles.toArray(new String[0]);

        actv = (AutoCompleteTextView) findViewById(R.id.rxAutoTxt);
        actv.setVisibility(View.VISIBLE);
        adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item,storeTitles);
        actv.setThreshold(1);
        actv.setAdapter(adapter);

        reactHere = (EditText) findViewById(R.id.rxTxt);
        reactHere.setVisibility(View.INVISIBLE);

        subscription = RxTextView.textChangeEvents(reactHere)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {

                        if (reactHere.getText().toString().equals("")) return false;
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchObserver());
                 actv.addTextChangedListener(new TextWatcher() {
            String beffore;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beffore = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(beffore.equals(s.toString()))) {
                    reactHere.setText(s.toString());
                    reactHere.setSelection(reactHere.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private DbHelper getHelper() {
        if (shite== null) {
            shite= OpenHelperManager.getHelper(this, DbHelper.class);
            Log.e("myTag","gethelper 2");
        }
        return shite;
    }

    public void initializeAdapter() {
        try {
            final RuntimeExceptionDao<MovieRow, Integer> moviesDao = getHelper().getMovieRunDao();
            List<MovieRow> movies = moviesDao.queryForAll();
            int i = 0;
            while (i < movies.size()) {
                titles.add(movies.get(i).getTitle());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MovieRow> getSingleMovie(String ti) {
        List<MovieRow> movie = new ArrayList<MovieRow>();
        try {
            final RuntimeExceptionDao<MovieRow, Integer> moviesDao = getHelper().getMovieRunDao();
            movie = moviesDao.queryForEq("title", ti);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movie;
    }

    private Observer<TextViewTextChangeEvent> getSearchObserver() {
        return new Observer<TextViewTextChangeEvent>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                if (!(actv.getText().toString().equals(textViewTextChangeEvent.text().toString()))) {
                    actv.setText(textViewTextChangeEvent.text().toString());
                    actv.showDropDown();
                }
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_view_all_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.Search:
                if (!searched) {
                    searched = true;
                    reactHere.setVisibility(View.VISIBLE);
                } else {
                    if (reactHere.getText().toString().equals("")) {
                        searched = false;
                        reactHere.setVisibility(View.INVISIBLE);
                    } else {
                        //Search
                        searched = false;
                        String query = reactHere.getText().toString();
                        List<MovieRow> movi = getSingleMovie(query);

                            String tit = movi.get(0).getTitle();
                            String pl = movi.get(0).getPlot();
                            String po = movi.get(0).getImage();
                            String ge = movi.get(0).getGenre();
                            String ty = movi.get(0).getType();
                            String ra = movi.get(0).getRating();

                            Intent i = new Intent(ViewAllResults.this, SingleMovie.class);
                            i.putExtra("title", tit);
                            i.putExtra("plot", pl);
                            i.putExtra("poster", po);
                            i.putExtra("ge", ge);
                            i.putExtra("rating", ra);
                            i.putExtra("type", ty);

                            startActivity(i);

                    }
                }
                break;
            case R.id.AddMovMenu:
                Intent addMov = new Intent(ViewAllResults.this, AddMovie.class);
                startActivity(addMov);
                break;
            case R.id.Sort:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    setTitle("MOVIES");
                    myTitle = "MOVIES";
                    getSupportActionBar().setTitle(myTitle);
                    return MoviesFrag.newInstance(ViewAllResults.this);
                case 1:
                    setTitle("SERIES");
                    myTitle = "SERIES";
                    getSupportActionBar().setTitle(myTitle);
                    return SeriesFrag.newInstance(ViewAllResults.this);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MOVIES";
                case 1:
                    return "SERIES";
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shite!= null) {
            OpenHelperManager.releaseHelper();
            shite= null;
        }
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
