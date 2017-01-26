package com.shvydchenko.flixter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shvydchenko.flixter.R;
import com.shvydchenko.flixter.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shvydchenko on 1/23/17.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private Context context;

    private static class UnpopularHolder {
        ImageView image;
        TextView title;
        TextView overview;
    }

    private static class PopularHolder {
        ImageView image;
        TextView title;
        TextView overview;
    }


    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0 , movies);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        Movie movie = getItem(position);

        switch(viewType)
        {
            case 0:
                PopularHolder popularHolder;

                if (convertView == null) {
                    popularHolder = new PopularHolder();
                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    convertView = inflater.inflate(R.layout.popular_movie_item, parent, false);
                    popularHolder.image = (ImageView) convertView.findViewById(R.id.movie_backdrop);
                    popularHolder.title = (TextView) convertView.findViewById(R.id.movie_title);
                    popularHolder.overview = (TextView) convertView.findViewById(R.id.movie_overview);
                    convertView.setTag(popularHolder);
                } else {
                    popularHolder = (PopularHolder) convertView.getTag();
                }
                popularHolder.image.setImageResource(0);

                popularHolder.title.setText(movie.getOriginalTitle());
                popularHolder.overview.setText(movie.getOverview());
                Picasso.with(getContext()).load(getImagePath(movie)).into(popularHolder.image);

                return convertView;
            case 1:
                UnpopularHolder unpopularHolder;

                if (convertView == null) {
                    unpopularHolder = new UnpopularHolder();

                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
                    unpopularHolder.image = (ImageView) convertView.findViewById(R.id.movie_image);
                    unpopularHolder.title = (TextView) convertView.findViewById(R.id.movie_title);
                    unpopularHolder.overview = (TextView) convertView.findViewById(R.id.movie_overview);
                    convertView.setTag(unpopularHolder);
                } else {
                    unpopularHolder = (UnpopularHolder) convertView.getTag();
                }

                unpopularHolder.image.setImageResource(0);
                unpopularHolder.title.setText(movie.getOriginalTitle());
                unpopularHolder.overview.setText(movie.getOverview());
                Picasso.with(getContext()).load(getImagePath(movie)).into(unpopularHolder.image);

                return convertView;
        }
        return null;
    }

    private String getImagePath(Movie movie) {
    //    int orientation = context.getResources().getConfiguration().orientation;
   //     (orientation == Configuration.ORIENTATION_PORTRAIT)
        if (movie.getViewType() == Movie.ViewType.POPULAR) {
                return movie.getBackdropPath();
        } else if (movie.getViewType() == Movie.ViewType.UNPOPULAR) {
            return movie.getPosterPath();
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType().ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return Movie.ViewType.values().length;
    }

}
