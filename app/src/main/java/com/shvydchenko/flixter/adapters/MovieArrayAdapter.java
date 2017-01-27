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

    private static class ViewHolder {
        ImageView image;
        TextView title;
        TextView overview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0 , movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        Movie movie = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = inflateCorrectView( viewType, parent);
            viewHolder.image = getCorrectImageView(viewType, convertView);
            viewHolder.title = (TextView) convertView.findViewById(R.id.movie_title);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.movie_overview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageResource(0);

        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());
        Picasso.with(getContext()).load(getImagePath(movie)).into(viewHolder.image);

        return convertView;

    }

    private String getImagePath(Movie movie) {
        if (movie.getViewType() == Movie.ViewType.POPULAR) {
            return movie.getBackdropPath();
        } else if (movie.getViewType() == Movie.ViewType.UNPOPULAR) {
            return movie.getPosterPath();
        }
        return null;
    }

    private View inflateCorrectView(int viewType, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == 0) {
            return inflater.inflate(R.layout.popular_movie_item, parent, false);
        } else if (viewType == 1) {
            return LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }
        return null;
    }

    private ImageView getCorrectImageView(int viewType, View convertView) {
        if (viewType == 0) {
            return (ImageView) convertView.findViewById(R.id.movie_backdrop);
        } else if (viewType == 1) {
            return (ImageView) convertView.findViewById(R.id.movie_image);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null) {
            return getItem(position).getViewType().ordinal();
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return Movie.ViewType.values().length;
    }

}
