package com.example.preethakumaresan.moviesgalore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class CustomGridAdapter extends BaseAdapter {

    ArrayList<MovieGrid> movies;
    Context ctx;

    public CustomGridAdapter(Context context,String[] a, String[] b,String[] c,String[] d,String[] e,String[] f){
        movies = new ArrayList<MovieGrid>();
        this.ctx = context;
        for(int i=0; i<a.length; ++i){
            movies.add(new MovieGrid(a[i],b[i],c[i],d[i],e[i],f[i]));
        }
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        ImageView moviImg;
        TextView p,t;
        ViewHolder(View v){
            moviImg = (ImageView) v.findViewById(R.id.movieImg);
            t = (TextView) v.findViewById(R.id.titGri);
            p = (TextView) v.findViewById(R.id.genGri);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_layout, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        MovieGrid mv = movies.get(position);
        Picasso.with(ctx)
                .load(mv.getPosterG())
                .placeholder(R.mipmap.ic_launcher)
                .resize(180,180)
                .into(holder.moviImg);

        holder.t.setText(mv.getTitleG());
        holder.p.setText(mv.getGenreG());
        holder.moviImg.setTag(mv);

        return row;
    }
}