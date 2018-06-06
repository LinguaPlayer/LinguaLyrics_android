package habibkazemi.ir.lingualyrics_android.ui.lyric;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wang.avi.AVLoadingIndicatorView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import habibkazemi.ir.lingualyrics_android.R;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricQuery;

/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends Fragment implements MaterialSearchView.OnQueryTextListener{

    private LyricViewModel mLyricViewModel;
    private Unbinder unbinder;
    MaterialSearchView mSearchView;

    @BindView(R.id.lyric_text)
    public TextView lyricTexView;
    @BindView(R.id.progress_loader)
    public AVLoadingIndicatorView loadingIndicator;

    public LyricsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLyricViewModel = ViewModelProviders.of(this).get(LyricViewModel.class);

        mLyricViewModel.getLyric().observe(this, new Observer<Lyric>() {
            @Override
            public void onChanged(@Nullable Lyric lyric) {
                if (lyric == null)
                    Log.d("Lyric", "lyric is currently null");
                if (lyric != null) {
                    Log.d("Lyric", lyric.getResult().getLyricText());
                    Log.d("Lyric", "id" + lyric.getId() + "");
                }
                ((OnLyricListener) getActivity()).onLyricFetchComplete(lyric);
                onLyricDownload(lyric);
            }
        });

        // TODO: Find a way to merge this with above
        mLyricViewModel.getLastLyric().observe(this, new Observer<Lyric>() {
            @Override
            public void onChanged(@Nullable Lyric lyric) {
                ((OnLyricListener) getActivity()).onLyricFetchComplete(lyric);
                onLyricDownload(lyric);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyrics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.lyrics_view_options_menu, menu);
        mSearchView = getActivity().findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);
    }

    private void showSpinner() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                mSearchView.showSearch();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isSearchViewOpen() {
         return mSearchView.isSearchOpen();
    }

    public void closeSearchView() {
        mSearchView.closeSearch();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        LyricQuery lyricQuery = new LyricQuery(query, "");
        mLyricViewModel.setLyricQuery(lyricQuery);
        lyricTexView.setText("");
        showSpinner();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onLyricDownload(Lyric lyric) {
       hideSpinner();
       // TODO: Handle errors in proper way and show proper icon and text for NOT FOUND, NETWORK ERROR , ...

       String lyricText = "Lyric not found or Some error happened";

       if (lyric != null) {
           mLyricViewModel.setLastLyric(lyric);
           lyricText = lyric.getResult().getLyricText();
       }
       lyricTexView.setText(lyricText);
    }

    public interface OnLyricListener {
        void onLyricFetchComplete(Lyric lyric);
    }
}
