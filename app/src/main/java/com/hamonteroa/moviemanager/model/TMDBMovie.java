package com.hamonteroa.moviemanager.model;

/**
 * Created by hamonteroa on 1/13/17.
 */

public class TMDBMovie {

    private String title;
    private int id;
    private String posterPath;
    private String releaseYear;
    private String overview;

    public TMDBMovie(String title, int id, String posterPath, String releaseDate, String overview) {
        this.title = title;
        this.id = id;
        this.posterPath = posterPath;
        this.releaseYear = releaseDate.substring(0, 4);
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}
