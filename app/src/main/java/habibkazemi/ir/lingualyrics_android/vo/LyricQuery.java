package habibkazemi.ir.lingualyrics_android.vo;

public class LyricQuery {
    private String title;
    private String artist;

    public LyricQuery() {}

    public LyricQuery(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
       return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
       this.artist = artist;
    }
}
