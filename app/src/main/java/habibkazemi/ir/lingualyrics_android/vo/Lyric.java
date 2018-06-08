package habibkazemi.ir.lingualyrics_android.vo;

import android.arch.persistence.room.Entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"id"})
public class Lyric {

	private Integer id;

	@SerializedName("lyric_text")
	@Expose
	private String lyricText;

	@SerializedName("artist")
	@Expose
	private String artist;

	@SerializedName("success")
	@Expose
	private boolean success;

	@SerializedName("album")
	@Expose
	private String album;

	@SerializedName("cover_art_image_url")
	@Expose
	private String coverArtImageUrl;

	@SerializedName("title")
	@Expose
	private String title;

	@SerializedName("url")
	@Expose
	private String url;

	public void setLyricText(String lyricText){
		this.lyricText = lyricText;
	}

	public String getLyricText(){
		return lyricText;
	}

	public void setArtist(String artist){
		this.artist = artist;
	}

	public String getArtist(){
		return artist;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setAlbum(String album){
		this.album = album;
	}

	public String getAlbum(){
		return album;
	}

	public void setCoverArtImageUrl(String coverArtImageUrl){
		this.coverArtImageUrl = coverArtImageUrl;
	}

	public String getCoverArtImageUrl(){
		return coverArtImageUrl;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public Integer getId() {return this.id;}

	public void setId(int id) {this.id = id;}

	@Override
 	public String toString(){
		return 
			"Lyric{" +
			"lyric_text = '" + lyricText + '\'' + 
			",artist = '" + artist + '\'' + 
			",success = '" + success + '\'' + 
			",album = '" + album + '\'' + 
			",cover_art_image_url = '" + coverArtImageUrl + '\'' + 
			",title = '" + title + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}