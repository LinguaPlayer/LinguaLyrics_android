package habibkazemi.ir.lingualyrics_android.ui.lyric;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import habibkazemi.ir.lingualyrics_android.ui.main.MainActivity;
import habibkazemi.ir.lingualyrics_android.R;
import habibkazemi.ir.lingualyrics_android.util.Constants;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.Resource;
import timber.log.Timber;

public class LyricsFragment extends Fragment{

    private LyricViewModel mLyricViewModel;
    private Unbinder unbinder;

    @BindView(R.id.lyric_text)
    public TextView lyricTexView;
    @BindView(R.id.progress_loader)
    public AVLoadingIndicatorView loadingIndicator;
    @BindView(R.id.message_group)
    public Group messageGroup;
    @BindView(R.id.message_icon)
    public ImageView messageIcon;
    @BindView(R.id.message_text)
    public TextView messageText;

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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.search).setVisible(true);
    }

    public void fetchingLyricFailed(Resource<Lyric> lyricResource) {
        hideSpinner();
        // TODO: Show artwork and icons instead of text
        showMessageError(R.drawable.ic_network_error, getResources().getString(R.string.network_error));
    }

    public void showMessageError(@DrawableRes int imageResource, String message) {
        lyricTexView.setVisibility(View.GONE);
        messageGroup.setVisibility(View.VISIBLE);
        messageText.setText(message);
        messageIcon.setImageResource(imageResource);
    }

    public void fetchingLyricLoading() {
        showSpinner();
        ((OnLyricListener) getActivity()).onLyricFetchLoading();

    }
    public void fetchingLyricSucceed(Resource<Lyric> lyricResource) {
        hideSpinner();
        ((OnLyricListener) getActivity()).onLyricFetchComplete(lyricResource.getData());

        if (lyricResource != null && lyricResource.getData() != null) {
            lyricTexView.setVisibility(View.VISIBLE);
            messageGroup.setVisibility(View.GONE);
            mLyricViewModel.setLastLyric(lyricResource.getData());
            String lyricText = lyricResource.getData().getLyricText();
            lyricTexView.setText(lyricText + "\n\n Source: " + lyricResource.getData().getSource() + "\n");
        } else {
            showMessageError(R.drawable.ic_not_found, getResources().getString(R.string.no_lyrics_found));
        }
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

        mLyricViewModel = ViewModelProviders.of(getActivity()).get(LyricViewModel.class);


        mLyricViewModel.getLyric().observe(this, lyricResource -> {
            switch (lyricResource.getStatus()) {
                case LOADING:
                    fetchingLyricLoading();
                    break;
                case SUCCESS:
                    fetchingLyricSucceed(lyricResource);
                    break;
                case ERROR:
                    fetchingLyricFailed(lyricResource);
                    break;
            }
        });

        SharedPreferences sp = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean expandAppBar = sp.getBoolean(getResources().getString(R.string.settings_key_expand_app_bar), true);
        boolean fromLyricList = false;
        if (getArguments() != null)
            fromLyricList = getArguments().getBoolean(Constants.INSTANCE.getFROM_LYRIC_LIST(), false);
        // If it navigates from lyricListFragments checks the settings
        // else it preserves the previous state
        if (fromLyricList) {
            if (expandAppBar)
                ((MainActivity) getActivity()).expandAppBar();
        } else {
            ((MainActivity) getActivity()).prepareAppBarCollapsedExpandedState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
        Timber.d("LyricsFragments: onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d( "LyricsFragments: onDestroy");

    }

    public interface OnLyricListener {
        void onLyricFetchComplete(Lyric lyric);
        void onLyricFetchLoading();
    }
}
