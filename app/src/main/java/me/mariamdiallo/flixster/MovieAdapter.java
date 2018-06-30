package me.mariamdiallo.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.mariamdiallo.flixster.models.Movie;
import me.mariamdiallo.flixster.models.Config;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // list of movies
    ArrayList<Movie> movies;
    // config needed for image urls
    Config config;
    //context for rendering
    Context context;

    public void setConfig(Config config) {
        this.config = config;
    }

    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    // creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // get the movie data at a specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // build url for poster image
        String imageUrl = null;

        // if in portrait mode, load the poster image
        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            // load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get the correct placeholder and imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;

        // rounded corners transformation
        RoundedCornersTransformation transformation = new RoundedCornersTransformation(
                10,
                10
        );

        // put glide image options in variable
        RequestOptions options = RequestOptions.bitmapTransform(transformation)
                .placeholder(placeholderId)
                .error(placeholderId);

        // load image using glide
        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as an inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvOverview;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            // set this as itemView's onlicklistener
            itemView.setOnClickListener(this);
        }

        // go to details activity on click
        @Override
        public void onClick(View view) {
            // get item position
            int position = getAdapterPosition();
            // get the movie at the position from movies array
            Movie movie = movies.get(position);
            // create intent for the new activity
            Intent intent = new Intent (context, MovieDetailsActivity.class);
            // serialize the movie using parceler, use its short name as a key
            intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
            // show the activity
            context.startActivity(intent);
        }
    }
}
