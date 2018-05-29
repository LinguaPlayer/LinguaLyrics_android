
package habibkazemi.ir.lingualyrics_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Lyric {

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("result")
    @Expose
    Result result;
    @SerializedName("artist")
    @Expose
    String artist;

    public Lyric clone() {
        Lyric cloneLyric = new Lyric();
        cloneLyric.title = title;
        cloneLyric.result = result;
        cloneLyric.artist = artist;
        return cloneLyric;
    }

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
