package habibkazemi.ir.lingualyrics_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import habibkazemi.ir.lingualyrics_android.loaders.LyricAsyncTaskLoader;
import habibkazemi.ir.lingualyrics_android.model.Lyric;
import habibkazemi.ir.lingualyrics_android.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends Fragment implements MaterialSearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Lyric> {

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
        getLoaderManager().initLoader(Constants.LYRIC_LOADER_ID, null, this);
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
        lyricTexView.setText("");
        showSpinner();

        Bundle args = new Bundle();
        args.putString(Constants.SEARCH_QUERY_URL_EXTRA, query);
        getLoaderManager().restartLoader(Constants.LYRIC_LOADER_ID, args, this);
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

       if (lyric != null)
           lyricText = lyric.getResult().getLyricText();
       lyricTexView.setText(lyricText);
    }

    @Override
    public Loader<Lyric> onCreateLoader(int id, Bundle args) {
        return new LyricAsyncTaskLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<Lyric> loader, Lyric lyric) {
        ((OnLyricListener) getActivity()).onLyricFetchComplete(lyric);
        onLyricDownload(lyric);
    }

    @Override
    public void onLoaderReset(Loader<Lyric> loader) {

    }

    public interface OnLyricListener {
        void onLyricFetchComplete(Lyric lyric);
    }
}
