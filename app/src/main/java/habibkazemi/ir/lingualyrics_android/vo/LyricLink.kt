package habibkazemi.ir.lingualyrics_android.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"], indices = [(Index(value = ["url", "userQuery"], unique = true)),
                                         (Index(value = ["description"]))])
class LyricLink {

    var id: Int? = null
        private set

    @SerializedName("description")
    @Expose
    var description: String? = null
    @Expose
    @SerializedName("url")
    var url: String? = null

    @Expose
    @SerializedName("query")
    var userQuery: String? = null

    fun setId(id: Int) {
        this.id = id
    }


    override fun toString(): String {
        return """LyricLink{description = '$description'
            |url = '$url',
            |query = '$userQuery'}"""
            .trimMargin()
    }
}
