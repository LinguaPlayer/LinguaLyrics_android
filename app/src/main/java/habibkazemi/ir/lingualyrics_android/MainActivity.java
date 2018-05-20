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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import habibkazemi.ir.lingualyrics_android.fragments.AboutFragment;
import habibkazemi.ir.lingualyrics_android.fragments.LyricsFragment;
import habibkazemi.ir.lingualyrics_android.fragments.RecentTracksFragment;
import habibkazemi.ir.lingualyrics_android.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnTouchListener{

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

    boolean mAppBarCollapsed = true;
    int mAppBarScrollRange = -1;
    private ActionBarDrawerToggle mDrawerToggle;

    private String mCurrentFragmentName;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mCollapsingToolbarLayout.setTitle(" ");

        prepareNavDrawer();

        mCurrentFragmentName = getResources().getString(R.string.lingua_lyrics);
        //I don't think it's a good idea to use colorPalette generator here
//        generateColorPalette();
    }


    private void prepareNavDrawer() {
        setupDrawerContent(mNVDrawer);
        mDrawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
        mNestedScrollView.setOnTouchListener(this);
        mCoordinatorLayout.setOnTouchListener(this);

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

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer,mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView nvView) {
        // Set default fragment in creation
        selectDrawerItem(nvView.getMenu().getItem(0));
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
        and below code disables that too
         */
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
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
        if (behavior != null) {
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(AppBarLayout appBarLayout) {
                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        boolean wasOpen = false;
        if (mCurrentFragmentName == getResources().getString(R.string.lingua_lyrics))
            wasOpen = ((LyricsFragment)currentFragment).isSearachViewOpen();
        if (wasOpen)
            ((LyricsFragment)currentFragment).closeSearchView();
        else
            super.onBackPressed();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Log.d("selectDrawerItem:", menuItem + "");
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.lyrics_fragment:
                unLockAppBar();
                fragmentClass = LyricsFragment.class;
                mCurrentFragmentName = getResources().getString(R.string.lingua_lyrics);
                break;
            case R.id.recent_tracks_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = RecentTracksFragment.class;
                mCurrentFragmentName = getResources().getString(R.string.recent_lyrics);
                break;
            case R.id.saved_lyrics_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = RecentTracksFragment.class;
                mCurrentFragmentName = getResources().getString(R.string.saved_lyrics);
                break;
            case R.id.settings_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = SettingsFragment.class;
                mCurrentFragmentName = getResources().getString(R.string.settings);
                break;
            case R.id.About_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = AboutFragment.class;
                mCurrentFragmentName = getResources().getString(R.string.about);
                break;
            default:
                fragmentClass = LyricsFragment.class;
                mCurrentFragmentName = getResources().getString(R.string.lingua_lyrics);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            currentFragment = fragment;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
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
            mCollapsingToolbarLayout.setTitle(mCurrentFragmentName);
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
                if (mCurrentFragmentName == getResources().getString(R.string.lingua_lyrics))
                    if (((LyricsFragment)currentFragment).isSearachViewOpen())
                        ((LyricsFragment)currentFragment).closeSearchView();
                break;

        }
        return false;
    }
}
