package habibkazemi.ir.lingualyrics_android;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import habibkazemi.ir.lingualyrics_android.ui.about.AboutFragment;
import habibkazemi.ir.lingualyrics_android.ui.lyric.LyricsFragment;
import habibkazemi.ir.lingualyrics_android.ui.recenttracks.RecentTracksFragment;
import habibkazemi.ir.lingualyrics_android.ui.savedlyrics.SavedLyricsFragment;
import habibkazemi.ir.lingualyrics_android.ui.settings.SettingsFragment;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.util.Constants;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnTouchListener, LyricsFragment.OnLyricListener{

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nvView)
    NavigationView mNVDrawer;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.track_detail)
    LinearLayout mTrackDetail;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.artist)
    TextView artistTextView;
    @BindView(R.id.music_title)
    TextView musicTitleTextView;
    @BindView(R.id.cover_art)
    ImageView coverArtImageView;


    boolean mAppBarCollapsed = true;
    int mAppBarScrollRange = -1;
    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentFragmentID;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mCollapsingToolbarLayout.setTitle(" ");

        if (savedInstanceState == null)
            mCurrentFragmentID = R.id.nav_lyrics;
        else
            mCurrentFragmentID = savedInstanceState.getInt(Constants.KEY_CURRENT_FRAGMENT_ID, R.id.nav_lyrics);
//        I don't think it's a good idea to use colorPalette generator here
//        generateColorPalette();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
        mNestedScrollView.setOnTouchListener(this);
        mCoordinatorLayout.setOnTouchListener(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.KEY_CURRENT_FRAGMENT_ID, mCurrentFragmentID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    private void generateColorPalette(/* get bitmpa? , or path?*/) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ocean);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor;
                mutedColor = palette.getMutedColor(R.color.colorPrimary);
                mCollapsingToolbarLayout.setContentScrimColor(mutedColor);
            }
        });
    }

    private void prepareNavDrawer() {
        setupDrawerContent(mNVDrawer);
        mDrawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(mDrawerToggle);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer,mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /* I put prepareNavDrawer here because calling it onCreate makes a bug
         when rotation happens and recreates the activity, the behaviour in lockAppBar is null */
        prepareNavDrawer();
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView nvView) {
        // show default fragment in creation
        showFragment(mCurrentFragmentID);
        nvView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void collapseAppBar() {
        // Collapse the AppBarLayout with animation
        mAppBarLayout.setExpanded(false, true);
    }

    private void expandAppBar() {
        // Collapse the AppBarLayout with animation
        mAppBarLayout.setExpanded(true, true);
    }

    private void lockAppBar() {
        /* Disable the nestedScrolling to disable expanding the
           appBar with dragging the mNestedScrollView below it */
        ViewCompat.setNestedScrollingEnabled(mNestedScrollView, false);

        /* But still appBar is expandable with dragging the appBar itself
           and below code disables that too */
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        if (behavior == null)
            return;

        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
    }

    private void unLockAppBar() {
        ViewCompat.setNestedScrollingEnabled(mNestedScrollView, true);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        if (behavior == null)
            return;

        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        boolean wasOpen = false;
        if (mCurrentFragmentID == R.id.nav_lyrics)
            wasOpen = ((LyricsFragment) mCurrentFragment).isSearchViewOpen();
        if (wasOpen)
            ((LyricsFragment) mCurrentFragment).closeSearchView();
        else
            super.onBackPressed();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        int id = menuItem.getItemId();
        showFragment(id);
    }

    public void prepareAppBarForSelectedItem(int id) {
        switch (id) {
            case R.id.nav_recent_tracks:
                collapseAppBar();
                lockAppBar();
                break;
            case R.id.nav_saved_lyrics:
                collapseAppBar();
                lockAppBar();
                break;
            case R.id.nav_settings:
                collapseAppBar();
                lockAppBar();
                break;
            case R.id.nav_About:
                collapseAppBar();
                lockAppBar();
                break;
            default:
                unLockAppBar();
                break;
        }
    }

    private String getTag(int id) {
        switch (id) {
            case R.id.nav_recent_tracks:
                return Constants.ID_RECENT_TRACKS;
            case R.id.nav_saved_lyrics:
                return Constants.ID_SAVED_LYRICS;
            case R.id.nav_settings:
                return Constants.ID_SETTINGS;
            case R.id.nav_About:
                return Constants.ID_ABOUT;
            default:
                return Constants.ID_LYRICS;
        }
    }

    private String getTitle(int id) {
        switch (id) {
            case R.id.nav_recent_tracks:
                return getResources().getString(R.string.recent_lyrics);
            case R.id.nav_saved_lyrics:
                return getResources().getString(R.string.saved_lyrics);
            case R.id.nav_settings:
                return getResources().getString(R.string.settings);
            case R.id.nav_About:
                return getResources().getString(R.string.about);
            default:
                return getResources().getString(R.string.lingua_lyrics);
        }
    }

    private Fragment getNewFragment(int id) {
        switch (id) {
            case R.id.nav_recent_tracks:
                return new RecentTracksFragment();
            case R.id.nav_saved_lyrics:
                return new SavedLyricsFragment();
            case R.id.nav_settings:
                return new SettingsFragment();
            case R.id.nav_About:
                return new AboutFragment();
            default:
                return new LyricsFragment();
        }
    }

    private void showFragment(int id) {
        String tag = getTag(id);
        Fragment fragment = getNewFragment(id);
        showFragment(fragment, id, tag);
    }

    private void showFragment(Fragment fragment, int id, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.flContent, fragment, tag)
          .commit();

        prepareAppBarForSelectedItem(id);
        updateCheckedItem(id);
        mCurrentFragmentID = id;
        mCurrentFragment = fragment;
        setTitle(getTitle(id));
        closeDrawers();
    }

    public void closeDrawers() {
        mDrawer.closeDrawers();
    }

    private void updateCheckedItem(int id) {
        if (mNVDrawer.getMenu().findItem(id) != null) {
            mNVDrawer.getMenu().findItem(id).setChecked(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                expandAppBar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        // TODO: improve this animation and made a transition from mTrackDetail to title
        if (mAppBarScrollRange == -1) {
            mAppBarScrollRange = appBarLayout.getTotalScrollRange();
        }

        if (Math.abs(verticalOffset) > 3 * mAppBarScrollRange / 5) {
            mCollapsingToolbarLayout.setTitle(getTitle(mCurrentFragmentID));
            mTrackDetail.animate().alpha(0.0f).setDuration(200);
            mAppBarCollapsed = true;
        }
        else {
            mCollapsingToolbarLayout.setTitle(" ");
            mTrackDetail.animate().alpha(1f).setDuration(50);
            mAppBarCollapsed = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.coordinator_layout:
            case R.id.nestedScrollView:
                if (mCurrentFragmentID == R.id.nav_lyrics)
                    if (((LyricsFragment) mCurrentFragment).isSearchViewOpen())
                        ((LyricsFragment) mCurrentFragment).closeSearchView();
                break;
        }
        return false;
    }

    @Override
    public void onLyricFetchComplete(Lyric lyric) {
        if (lyric != null) {
            String title = lyric.getTitle();
            String artist = lyric.getArtist();
            String imageUrl = lyric.getCoverArtImageUrl();
            fillToolbarItems(imageUrl, artist, title);
        }
    }

    private void fillToolbarItems(String imageUrl, String artist, String title) {
        musicTitleTextView.setText(title);
        artistTextView.setText(artist);
        Picasso.get().load(imageUrl).into(coverArtImageView);
    }

}
