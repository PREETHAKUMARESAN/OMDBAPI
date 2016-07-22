package com.example.preethakumaresan.moviesgalore;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void blink(View view){
        TextView image = (TextView) findViewById(R.id.shit);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        image.startAnimation(animation1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.AddMovMenu:
                Intent addM = new Intent(MainActivity.this, AddMovie.class);
                Log.e("myTag","admovie");
                startActivity(addM);
                break;
            case R.id.ViewAll:
                Intent viewAll = new Intent(MainActivity.this, ViewAllResults.class);
                startActivity(viewAll);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}