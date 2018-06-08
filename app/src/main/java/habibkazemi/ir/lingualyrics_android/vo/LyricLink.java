package habibkazemi.ir.lingualyrics_android.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = {"id"}, indices = {@Index(value= {"url", "userQuery"}, unique = true),
                                         @Index(value={"description"})})
public class LyricLink {

    private Integer id;

	@SerializedName("description")
    @Expose
	private String description;
	@Expose
	@SerializedName("url")
	private String url;

    @Expose()
    @SerializedName("query")
    private String userQuery;

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

    public void setUserQuery(String userQuery){ this.userQuery = userQuery; }

    public String getUserQuery() { return userQuery; }


    public Integer getId() {return this.id;}

    public void setId(int id) {this.id = id;}


        @Override
 	public String toString(){
		return 
			"LyricLink{" +
			"description = '" + description + '\'' + 
			",url = '" + url + '\'' +
            ",query = '" + userQuery + '\'' +
			"}";
		}
}
