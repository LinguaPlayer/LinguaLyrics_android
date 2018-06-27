package habibkazemi.ir.lingualyrics_android.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import org.parceler.Parcel

@Parcel
class Result {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("lyric_text")
    @Expose
    var lyricText: String? = null
    @SerializedName("artist")
    @Expose
    var artist: String? = null
    @SerializedName("album")
    @Expose
    var album: String? = null
    @SerializedName("cover_art_image_url")
    @Expose
    var coverArtImageUrl: String? = null

    override fun toString(): String {
        return """Result{title = '$title'
            |lyricText = '$lyricText',
            |artist = '$artist',
            |album = '$album',
            |coverArtImageUrl= '$coverArtImageUrl'}"""
            .trimMargin()
    }

}
