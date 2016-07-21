package com.example.preethakumaresan.moviesgalore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class AddMovie  extends AppCompatActivity {

    EditText title;
    Button add;
    TextView genre, plot, rating, type;
    ImageView poster;

    DbHelper shit;
    ApiInterface apiService;

    boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        Log.e("myTag","addmovie create");
        genre = (TextView) findViewById(R.id.addGenre);
        poster = (ImageView) findViewById(R.id.addImg);
        title = (EditText) findViewById(R.id.addTxt);
        add = (Button) findViewById(R.id.addBtn);
        plot = (TextView) findViewById(R.id.addPlot);
        rating = (TextView) findViewById(R.id.addRat);
        type = (TextView) findViewById(R.id.addType);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.e("myTag","addmovie create 2");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = title.getText().toString();
                Log.e("myTag","addmovie create 3");
                getMovie(query);
            }
        });
    }

    private DbHelper getHelper() {
        if (shit== null) {
            Log.e("myTag","databasehelper");
            shit= OpenHelperManager.getHelper(this, DbHelper.class);
        }
        return shit;
    }

    public boolean addMovie(String im, String ti, String ge, String pl, String ra, String ty) {
        try {
            final RuntimeExceptionDao<MovieRow, Integer> movieDao = getHelper().getMovieRunDao();

            if (movieDao.create(new MovieRow(im, ti, ge, pl, ra, ty)) == 1) {
                done= true;
                Log.e("myTag","addmovie create 4");
                Toast.makeText(AddMovie.this, "Addition Successful", Toast.LENGTH_SHORT).show();
                Intent ani = new Intent(AddMovie.this, ViewAllResults.class);
                startActivity(ani);
            } else {
                done= false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return done;
    }

    public void getMovie(String title) {
        done= false;
        Map<String, String> map = new HashMap<>();
        map.put("t", title);
        map.put("r", "json");
        Call<Movie> call = apiService.getMovieTitle(map);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.body().getResponse().equals("True")) {
                    setTitle(response.body().getTitle());
                    plot.setText(response.body().getPlot());
                    //posterText.setText(response.body().getPoster());
                    Picasso.with(AddMovie.this)
                            .load(response.body().getPoster())
                            .placeholder(R.mipmap.ic_launcher)
                            .into(poster);

                    genre.setText(response.body().getGenre());
                    rating.setText( response.body().getImdbRating());
                    type.setText(response.body().getType());
                    if (addMovie(response.body().getPoster(), response.body().getTitle(), response.body().getGenre(), response.body().getPlot(), response.body().getImdbRating(), response.body().getType())) {
                        done= true;
                    }
                } else {

                    done = false;
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (shit!= null) {
            OpenHelperManager.releaseHelper();
            shit= null;
        }
        finish();
    }
}
