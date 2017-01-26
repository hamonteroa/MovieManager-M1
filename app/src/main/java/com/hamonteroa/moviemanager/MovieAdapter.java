package com.hamonteroa.moviemanager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hamonteroa.moviemanager.model.TMDBMovie;
import com.hamonteroa.moviemanager.utility.MMConstants;

import java.util.ArrayList;

import static com.hamonteroa.moviemanager.sync.TMDBNetworkUrl.LOG_TAG;

public class MovieAdapter extends ArrayAdapter<TMDBMovie> implements AdapterView.OnItemClickListener {

    private TextView titleTextView, yearTextView;

    public MovieAdapter(Context context, ArrayList<TMDBMovie> movies) {
        super(context, R.layout.list_item_movie, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater movieInflater = LayoutInflater.from(getContext());
        View customView = movieInflater.inflate(R.layout.list_item_movie, parent, false);

        TMDBMovie movie = getItem(position);

        titleTextView = (TextView)customView.findViewById(R.id.title_textView);
        titleTextView.setText(movie.getTitle());
        yearTextView = (TextView)customView.findViewById(R.id.year_textView);
        yearTextView.setText(movie.getReleaseYear());

        return customView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TMDBMovie movie = super.getItem(position);
        Log.v(LOG_TAG, "onItemClick movie: " + movie.getTitle());
        Gson gson = new Gson();

        Intent intent = new Intent(getContext(), DetailMovieActivity.class);
        intent.putExtra(MMConstants.MOVIE, gson.toJson(movie));

        getContext().startActivity(intent);
    }
}
