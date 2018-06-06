
package habibkazemi.ir.lingualyrics_android.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Entity(primaryKeys = {"id"})
public class Lyric {

    public Integer id;

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("result")
    @Expose
    @Embedded(prefix = "result_")
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

    public int getId() {return this.id;}

    public void setId(int id) {this.id = id;}

}
