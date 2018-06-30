package me.mariamdiallo.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

// annotation making the class 'Parcelable'
@Parcel
public class Movie {

    //values from API
    String title;
    String overview;
    String posterPath; //only the path
    String backdropPath;

    public Movie() {}

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");

    }
}
