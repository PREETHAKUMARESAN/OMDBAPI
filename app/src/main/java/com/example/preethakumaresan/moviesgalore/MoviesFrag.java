package com.example.preethakumaresan.moviesgalore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class MoviesFrag extends Fragment {

    private static Context ct;

    public static MoviesFrag newInstance(Context c) {
        MoviesFrag fragment = new MoviesFrag();
        ct = c;
        return fragment;
    }

    public MoviesFrag() {
        // Required empty public constructor
    }

    GridView shitgrid;
    DbHelper shite= null;
    List<String> titles = new ArrayList<>();
    List<String> plots = new ArrayList<>();
    List<String> genres = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<String> posters = new ArrayList<>();
    List<String> rating = new ArrayList<>();

    String[] titleGrid;
    String[] plotGrid;
    String[] genreGrid;
    String[] ratGrid;
    String[] posterGrid;
    String[] typeGrid;

    boolean sorted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_moviesfrag, container, false);
        shitgrid = (GridView) rootView.findViewById(R.id.gridViewMovies);
        getMovies();

        setHasOptionsMenu(true);

        titleGrid = titles.toArray(new String[0]);
        plotGrid = plots.toArray(new String[0]);
        genreGrid = genres.toArray(new String[0]);
        ratGrid = rating.toArray(new String[0]);
        posterGrid = posters.toArray(new String[0]);
        typeGrid = types.toArray(new String[0]);

        shitgrid.setAdapter(new CustomGridAdapter(ct, typeGrid, titleGrid, ratGrid, posterGrid, plotGrid, genreGrid));
        shitgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ct, SingleMovie.class);

                CustomGridAdapter.ViewHolder holder = (CustomGridAdapter.ViewHolder) view.getTag();
                MovieGrid thisgrd = (MovieGrid) holder.moviImg.getTag();

                i.putExtra("title", thisgrd.getTitleG());
                i.putExtra("plot", thisgrd.getPlotG());
                i.putExtra("ge", thisgrd.getGenreG());
                i.putExtra("poster", thisgrd.getPosterG());
                i.putExtra("type", thisgrd.getTypeG());
                i.putExtra("rating", thisgrd.getRatingG());

                startActivity(i);
            }
        });

        return rootView;
    }

    public void getMovies() {
        try {
            final RuntimeExceptionDao<MovieRow, Integer> moviesDao = getHelper(ct).getMovieRunDao();
            List<MovieRow> movies = moviesDao.queryForEq("type", "movie");
            int i = 0;
            while (i < movies.size()) {
                titles.add(movies.get(i).getTitle());
                plots.add(movies.get(i).getPlot());
                posters.add(movies.get(i).getImage());
                rating.add(movies.get(i).getRating());
                genres.add(movies.get(i).getGenre());
                types.add(movies.get(i).getType());
                i++;
            }
            if (shite!= null) {
                OpenHelperManager.releaseHelper();
                shite= null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DbHelper getHelper(Context ctx) {
        if (shite== null) {
            shite= OpenHelperManager.getHelper(ctx, DbHelper.class);
        }
        return shite;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Sort){
            if(!sorted){
                getSortedMovies();
                Toast.makeText(ct,"sorted!",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getSortedMovies(){
        try{
            QueryBuilder<MovieRow,Integer> qb = getHelper(ct).getMovieRunDao().queryBuilder();
            qb.where().eq("type","movie");
            qb.orderBy("rating",false);
            List<MovieRow> movies = qb.query();
            int i=0;
            titles.clear();
            plots.clear();
            posters.clear();
            rating.clear();
            genres.clear();
            types.clear();
            while (i < movies.size()) {
                titles.add(movies.get(i).getTitle());
                plots.add(movies.get(i).getPlot());
                posters.add(movies.get(i).getImage());
                rating.add(movies.get(i).getRating());
                genres.add(movies.get(i).getGenre());
                types.add(movies.get(i).getType());
                i++;
            }

            titleGrid = titles.toArray(new String[0]);
            plotGrid = plots.toArray(new String[0]);
            genreGrid = genres.toArray(new String[0]);
            ratGrid = rating.toArray(new String[0]);
            posterGrid = posters.toArray(new String[0]);
            typeGrid = types.toArray(new String[0]);

            shitgrid.setAdapter(new CustomGridAdapter(ct, typeGrid, titleGrid, ratGrid, posterGrid, plotGrid, genreGrid));

            if (shite!= null) {
                OpenHelperManager.releaseHelper();
                shite= null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}