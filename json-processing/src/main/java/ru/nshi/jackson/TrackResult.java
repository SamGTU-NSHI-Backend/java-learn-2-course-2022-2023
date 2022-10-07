package ru.nshi.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackResult {
    public static final String PROPERTY_ARTIST_ID = "artistId";
    @JsonProperty(PROPERTY_ARTIST_ID)
    @JsonAlias({"artist_id", "artist"})
    private Long artist;
    @JsonProperty(value = "trackName")
    private String trackName;

    private long trackTimeMillis;
    private String country;

    public TrackResult() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public Long getArtist() {
        return artist;
    }

    public void setArtist(Long artist) {
        this.artist = artist;
    }

    public long getTrackTimeMillis() {
        return trackTimeMillis;
    }

    public void setTrackTimeMillis(long trackTimeMillis) {
        this.trackTimeMillis = trackTimeMillis;
    }

    @Override
    public String toString() {
        return "TrackResult{" +
            "artistId=" + artist +
            ", trackName='" + trackName + '\'' +
            ", trackTimeMillis=" + trackTimeMillis +
            ", country='" + country + '\'' +
            '}';
    }
}
