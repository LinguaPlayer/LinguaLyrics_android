package habibkazemi.ir.lingualyrics_android.ui.lyriclist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import habibkazemi.ir.lingualyrics_android.R;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;

public class LyricRecyclerAdapter extends RecyclerView.Adapter<LyricRecyclerAdapter.ViewHolder> {

    List<LyricLink> mLyriclinks;
    OnItemClickListener listener;

    public LyricRecyclerAdapter(List<LyricLink> lyricLinks) {
        this.mLyriclinks = lyricLinks;
    }

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
        LyricLink lyricLink = mLyriclinks.get(position);
        holder.setLyricDescription(lyricLink);
    }

    public void setData(List<LyricLink> lyricLinks) {
        this.mLyriclinks = lyricLinks;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLyriclinks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lyric_description;

        public ViewHolder(View itemView) {
            super(itemView);
            lyric_description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        public void setLyricDescription(LyricLink lyric_description) {
            this.lyric_description.setText(lyric_description.getDescription());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            LyricLink lyricLink = mLyriclinks.get(position);
            listener.onItemClick(lyricLink);
            Toast.makeText(v.getContext(), lyricLink.getUrl(), Toast.LENGTH_LONG).show();

        }
    }

    public interface OnItemClickListener {
        void onItemClick(LyricLink lyricLink);
    }
}
