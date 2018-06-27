package habibkazemi.ir.lingualyrics_android.ui.lyriclist;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import habibkazemi.ir.lingualyrics_android.R;
import habibkazemi.ir.lingualyrics_android.ui.lyric.LyricViewModel;
import habibkazemi.ir.lingualyrics_android.util.Constants;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;
import habibkazemi.ir.lingualyrics_android.vo.Status;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class LyricListFragment extends Fragment implements LyricRecyclerAdapter.OnItemClickListener {


    @BindView(R.id.lyric_list_recyclerview)
    public RecyclerView mLyricListRecyclerView;

    @BindView(R.id.progress_loader)
    public AVLoadingIndicatorView loadingIndicator;

    @BindView(R.id.message_group)
    public Group messageGroup;
    @BindView(R.id.message_icon)
    public ImageView messageIcon;
    @BindView(R.id.message_text)
    public TextView messageText;

    private Unbinder unbinder;
    private LyricViewModel mLyricViewModel;


    public LyricListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyric_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        /* Disable the nestedScrolling to disable expanding the
           appBar with dragging the mNestedScrollView below it */
        ViewCompat.setNestedScrollingEnabled(mLyricListRecyclerView, false);

        LyricRecyclerAdapter lyricRecyclerAdapter = new LyricRecyclerAdapter();
        lyricRecyclerAdapter.setOnItemClickListener(this);
        mLyricListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLyricListRecyclerView.setAdapter(lyricRecyclerAdapter);

        mLyricViewModel = ViewModelProviders.of(getActivity()).get(LyricViewModel.class);

        mLyricViewModel.getLyricQueryInDatabaseLiveData().observe(this, listResource -> {
            if (listResource.status == Status.LOADING) {
                showSpinner();
            }

            if (listResource.status == Status.SUCCESS) {
                hideSpinner();
                if (listResource.data == null || listResource.data.isEmpty())
                    showMessageError(R.drawable.ic_not_found, getResources().getString(R.string.no_lyrics_found));
            }

            if (listResource.status == Status.ERROR) {
                hideSpinner();
                showMessageError(R.drawable.ic_network_error, getResources().getString(R.string.network_error));
            }

            if (listResource.data != null) {
                lyricRecyclerAdapter.submitList(listResource.data);
            }
        });
    }

    public void showMessageError(@DrawableRes int imageResource, String message) {
        mLyricListRecyclerView.setVisibility(View.GONE);
        messageGroup.setVisibility(View.VISIBLE);
        messageText.setText(message);
        messageIcon.setImageResource(imageResource);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemClick(LyricLink lyricLink) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.INSTANCE.getFROM_LYRIC_LIST(), true);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host);
        navController.navigate(R.id.action_lyricListFragment_to_nav_lyrics, bundle);
        mLyricViewModel.queryLyricByUrl(lyricLink.getUrl());
    }


    private void showSpinner() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        loadingIndicator.setVisibility(View.INVISIBLE);
    }
}
