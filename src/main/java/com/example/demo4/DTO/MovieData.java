package com.example.demo4.DTO;

public class MovieData {

        private int filmId;
        private String nameRu;
        private String nameEn;
        private String year;
        private String filmLength;
        private String rating;
        private int ratingVoteCount;
        private String posterUrl;
        private String posterUrlPreview;
        private int isAfisha;

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFilmLength() {
        return filmLength;
    }

    public void setFilmLength(String filmLength) {
        this.filmLength = filmLength;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getRatingVoteCount() {
        return ratingVoteCount;
    }

    public void setRatingVoteCount(int ratingVoteCount) {
        this.ratingVoteCount = ratingVoteCount;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPosterUrlPreview() {
        return posterUrlPreview;
    }

    public void setPosterUrlPreview(String posterUrlPreview) {
        this.posterUrlPreview = posterUrlPreview;
    }

    public int getIsAfisha() {
        return isAfisha;
    }

    public void setIsAfisha(int isAfisha) {
        this.isAfisha = isAfisha;
    }

    public MovieData(int filmId, String nameRu, String nameEn, String year, String filmLength, String rating, int ratingVoteCount, String posterUrl, String posterUrlPreview, int isAfisha) {
        this.filmId = filmId;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.year = year;
        this.filmLength = filmLength;
        this.rating = rating;
        this.ratingVoteCount = ratingVoteCount;
        this.posterUrl = posterUrl;
        this.posterUrlPreview = posterUrlPreview;
        this.isAfisha = isAfisha;

    }
}
