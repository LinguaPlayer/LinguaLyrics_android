package habibkazemi.ir.lingualyrics_android.ui.lyriclist

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import habibkazemi.ir.lingualyrics_android.R
import habibkazemi.ir.lingualyrics_android.vo.LyricLink
import kotlinx.android.synthetic.main.lyric_detail.view.*

class LyricRecyclerAdapter : PagedListAdapter<LyricLink, LyricRecyclerAdapter.ViewHolder>(DIFF_CALLBACK) {

    internal var listener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val lyricDetail = inflater.inflate(R.layout.lyric_detail, parent, false)
        return ViewHolder(lyricDetail)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lyricLink = getItem(position)
        holder.bind(lyricLink)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val lyric_description: TextView = itemView.description
        private var lyricLink: LyricLink? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(lyricLink: LyricLink?) {
            this.lyricLink = lyricLink
            this.lyric_description.text = lyricLink?.description
        }

        override fun onClick(v: View) {
            listener?.onItemClick(lyricLink)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(lyricLink: LyricLink?)
    }

    companion object {

        val DIFF_CALLBACK: DiffUtil.ItemCallback<LyricLink> = object : DiffUtil.ItemCallback<LyricLink>() {
            override fun areItemsTheSame(oldItem: LyricLink, newItem: LyricLink): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: LyricLink, newItem: LyricLink): Boolean {
                return oldItem.url == newItem.url && oldItem.description == newItem.description
            }
        }
    }
}
