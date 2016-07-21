package com.example.preethakumaresan.moviesgalore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class SingleMovie extends AppCompatActivity {

    // shows the details of the movie
    TextView genre, plot, type, rating;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        genre = (TextView) findViewById(R.id.singleGenre);
        plot = (TextView) findViewById(R.id.singlePlot);
        poster = (ImageView) findViewById(R.id.singleImg);
        type = (TextView) findViewById(R.id.singleType);
        rating = (TextView) findViewById(R.id.singleRating);

        Intent a = getIntent();//get the details from the intent called in movieFragment
        if (a == null) {
            setTitle("TITLE");
            poster.setImageResource(R.mipmap.ic_launcher);
        } else {
            genre.setText(a.getStringExtra("ge"));
            plot.setText(a.getStringExtra("plot"));
            setTitle(a.getStringExtra("title"));

            Picasso.with(SingleMovie.this)
                    .load(a.getStringExtra("poster"))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(poster);
            type.setText(a.getStringExtra("type"));
            rating.setText(a.getStringExtra("rating"));
        }
    }
}