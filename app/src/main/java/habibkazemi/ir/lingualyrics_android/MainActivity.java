package habibkazemi.ir.lingualyrics_android;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.MenuItem;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import habibkazemi.ir.lingualyrics_android.fragments.AboutFragment;
import habibkazemi.ir.lingualyrics_android.fragments.LyricsFragment;
import habibkazemi.ir.lingualyrics_android.fragments.RecentTracksFragment;
import habibkazemi.ir.lingualyrics_android.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nvView)
    NavigationView mNVDrawer;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.track_detail)
    LinearLayout trackDetail;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    boolean appBarCollapsed = true;
    int mAppBarScrollRange = -1;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mCollapsingToolbarLayout.setTitle(" ");

        prepareNavDrawer();
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
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
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
        appBarLayout.setExpanded(false, true);
    }
    private void lockAppBar() {
        ViewCompat.setNestedScrollingEnabled(nestedScrollView, false);
    }

    private void unLockAppBar() {
        ViewCompat.setNestedScrollingEnabled(nestedScrollView, true);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Log.d("selectDrawerItem:", menuItem + "");
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.lyrics_fragment:
                unLockAppBar();
                fragmentClass = LyricsFragment.class;
                break;
            case R.id.recent_tracks_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = RecentTracksFragment.class;
                break;
            case R.id.saved_lyrics_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = RecentTracksFragment.class;
                break;
            case R.id.settings_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.About_fragment:
                collapseAppBar();
                lockAppBar();
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = LyricsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        // TODO: improve this animation and made a transition from trackDetail to title
        if (mAppBarScrollRange == -1) {
            mAppBarScrollRange = appBarLayout.getTotalScrollRange();
        }
        if (Math.abs(verticalOffset) > 3 * mAppBarScrollRange / 5) {
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.lingua_lyrics));
            trackDetail.animate().alpha(0.0f).setDuration(200);
            appBarCollapsed = true;
        }
        else {
            mCollapsingToolbarLayout.setTitle(" ");
            trackDetail.animate().alpha(1f).setDuration(50);
            appBarCollapsed = false;
        }
    }
}
