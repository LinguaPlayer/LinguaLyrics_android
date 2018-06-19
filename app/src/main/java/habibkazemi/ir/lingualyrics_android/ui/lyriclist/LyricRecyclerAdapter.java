package habibkazemi.ir.lingualyrics_android.ui.lyriclist;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import habibkazemi.ir.lingualyrics_android.R;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;

public class LyricRecyclerAdapter extends PagedListAdapter<LyricLink, LyricRecyclerAdapter.ViewHolder> {

    OnItemClickListener listener;

    public LyricRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<LyricLink> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<LyricLink>() {
                @Override
                public boolean areItemsTheSame(LyricLink oldItem, LyricLink newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(LyricLink oldItem, LyricLink newItem) {
                    return (oldItem.getUrl().equals(newItem.getUrl()) && oldItem.getDescription().equals(newItem.getDescription()));
                }
            };


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View lyricDetail = inflater.inflate(R.layout.lyric_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(lyricDetail);
        return viewHolder;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LyricLink lyricLink = getItem(position);
        holder.bind(lyricLink);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lyric_description;
        private LyricLink lyricLink;

        public ViewHolder(View itemView) {
            super(itemView);
            lyric_description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        public void bind(LyricLink lyricLink) {
            this.lyricLink = lyricLink;
            this.lyric_description.setText(lyricLink.getDescription());
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(lyricLink);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(LyricLink lyricLink);
    }
}
