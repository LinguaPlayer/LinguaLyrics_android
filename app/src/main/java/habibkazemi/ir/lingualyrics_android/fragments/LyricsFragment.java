package habibkazemi.ir.lingualyrics_android.fragments;


import android.content.res.Configuration;
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
import habibkazemi.ir.lingualyrics_android.MainActivity;
import habibkazemi.ir.lingualyrics_android.R;
import habibkazemi.ir.lingualyrics_android.model.Lyric;
import habibkazemi.ir.lingualyrics_android.model.Result;
import habibkazemi.ir.lingualyrics_android.remote.LyricsApi;
import habibkazemi.ir.lingualyrics_android.remote.LyricsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LyricsFragment extends Fragment implements MaterialSearchView.OnQueryTextListener {

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
        LyricsService lyricsService = LyricsApi.getLyricService(getContext());
        lyricsService.getLyric(query, "").enqueue(new Callback<Lyric>() {
            @Override
            public void onResponse(Call<Lyric> call, Response<Lyric> response) {
                String text = "";
                if (response.isSuccessful()) {
                    Result result = response.body().getResult();
                    ((MainActivity)getActivity()).onLyricFetch(result.getCoverArtImageUrl(), result.getArtist(), result.getTitle());
                    hideSpinner();
                    text = result.getLyricText();
                    if (text != null)
                        lyricTexView.setText(text);
                }
            }

            @Override
            public void onFailure(Call<Lyric> call, Throwable t) {
                Log.d("response", "failure");
            }
        });

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("onQueryTextChange", newText);
        return false;
    }

    public interface OnLyricDownlod {
        void onLyricFetch(String imageUrl, String artist, String title);
    }

}
