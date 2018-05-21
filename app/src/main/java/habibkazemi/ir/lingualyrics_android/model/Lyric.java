
package habibkazemi.ir.lingualyrics_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lyric {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("artist")
    @Expose
    private String artist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
