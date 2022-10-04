package ru.nshi.jackson;

public class TrackResponse {
    private String trackName;
    private String artistName;
    private String country;
    private String trackTime;

    public TrackResponse() {
    }

    public TrackResponse(String trackName, String artistName, String country, String trackTime) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.country = country;
        this.trackTime = trackTime;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(String trackTime) {
        this.trackTime = trackTime;
    }
}
