package ir.habibkazemi.lingualyrics.vo

import android.arch.persistence.room.Entity


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
class Lyric {

    var id: Int? = null
        private set

    @SerializedName("lyric_text")
    @Expose
    var lyricText: String? = null

    @SerializedName("artist")
    @Expose
    var artist: String? = null

    @SerializedName("source")
    @Expose
    var source: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean = false

    @SerializedName("album")
    @Expose
    var album: String? = null

    @SerializedName("cover_art_image_url")
    @Expose
    var coverArtImageUrl: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    fun setId(id: Int) {
        this.id = id
    }

    override fun toString(): String {
        return """Lyric{lyric_text = '$lyricText',
            |artist = '$artist',
            |success = '$success',
            |source = '$source',
            |album = '$album',
            |cover_art_image_url = '$coverArtImageUrl',
            |title = '$title',
            |url = '$url'}"""
            .trimMargin()
    }
}