
package habibkazemi.ir.lingualyrics_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("lyric_text")
    @Expose
    private String lyricText;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("album")
    @Expose
    private String album;
    @SerializedName("cover_art_image_url")
    @Expose
    private String coverArtImageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyricText() {
        return lyricText;
    }

    public void setLyricText(String lyricText) {
        this.lyricText = lyricText;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getCoverArtImageUrl() {
        return coverArtImageUrl;
    }

    public void setCoverArtImageUrl(String coverArtImageUrl) {
        this.coverArtImageUrl = coverArtImageUrl;
    }

}
