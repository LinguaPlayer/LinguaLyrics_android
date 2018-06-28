package habibkazemi.ir.lingualyrics_android.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import habibkazemi.ir.lingualyrics_android.R
import habibkazemi.ir.lingualyrics_android.api.LyricsApi
import habibkazemi.ir.lingualyrics_android.ui.lyric.LyricViewModel
import habibkazemi.ir.lingualyrics_android.ui.lyric.LyricsFragment
import habibkazemi.ir.lingualyrics_android.vo.Lyric
import habibkazemi.ir.lingualyrics_android.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class MainActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener, View.OnTouchListener, LyricsFragment.OnLyricListener, MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {

    internal var mAppBarCollapsed = true
    internal var mAppBarScrollRange = -1
    private var mDrawerToggle: ActionBarDrawerToggle? = null

    private var mCurrentFragmentID: Int = 0

    private var mSearchView: MaterialSearchView? = null
    private var mLyricViewModel: LyricViewModel? = null

    val isSearchViewOpen: Boolean?
        get() = mSearchView?.isSearchOpen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        //mCollapsingToolbarLayout.setTitle(" ");

        mCurrentFragmentID = savedInstanceState?.getInt(Constants.KEY_CURRENT_FRAGMENT_ID, R.id.nav_lyrics) ?: R.id.nav_lyrics

        mLyricViewModel = ViewModelProviders.of(this).get(LyricViewModel::class.java)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.lyrics_view_options_menu, menu)
        mSearchView = findViewById(R.id.search_view)
        (mSearchView as MaterialSearchView).setOnQueryTextListener(this)
        (mSearchView as MaterialSearchView).setOnSearchViewListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        app_bar_layout?.addOnOffsetChangedListener(this)
        nestedScrollView.setOnTouchListener(this)
        coordinator_layout.setOnTouchListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.KEY_CURRENT_FRAGMENT_ID, mCurrentFragmentID)
    }

    override fun onPause() {
        super.onPause()
        app_bar_layout?.removeOnOffsetChangedListener(this)
    }

    private fun prepareNavDrawer() {
        val navController = Navigation.findNavController(this, R.id.nav_host)
        NavigationUI.setupWithNavController(nvView, navController)
        navController.addOnNavigatedListener { controller, destination ->
            prepareAppBarForSelectedItem(destination.id)
            collapsing_toolbar_layout?.title = getTitle(destination.id)
        }
        mDrawerToggle = setupDrawerToggle()
        drawer_layout?.addDrawerListener(mDrawerToggle!!)
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        /* I put prepareNavDrawer here because calling it onCreate makes a bug
         when rotation happens and recreates the activity, the behaviour in lockAppBar is null */
        prepareNavDrawer()
        mDrawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)
    }

    fun collapseAppBar() {
        // Collapse the AppBarLayout with animation
        app_bar_layout?.setExpanded(false, true)
    }

    fun expandAppBar() {
        // Collapse the AppBarLayout with animation
        app_bar_layout?.setExpanded(true, true)
    }

    fun lockAppBar() {
        /* Disable the nestedScrolling to disable expanding the
           appBar with dragging the mNestedScrollView below it */
        ViewCompat.setNestedScrollingEnabled(nestedScrollView, false)

        /* But still appBar is expandable with dragging the appBar itself
           and below code disables that too */
        val params = app_bar_layout?.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior? ?: return

        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }

    private fun unLockAppBar() {
        ViewCompat.setNestedScrollingEnabled(nestedScrollView, true)

        val params = app_bar_layout?.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior? ?: return

        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return true
            }
        })
    }

    override fun onBackPressed() {
        if (isSearchViewOpen == true)
            closeSearchView()
        else
            super.onBackPressed()

    }

    fun prepareAppBarCollapsedExpandedState() {
        if (mLyricViewModel?.appBarCollapsed == false)
            expandAppBar()
    }

    fun prepareAppBarForSelectedItem(id: Int) {
        if (mCurrentFragmentID == R.id.nav_lyrics)
            mLyricViewModel?.appBarCollapsed = app_bar_layout.bottom < app_bar_layout.height * 2 / 3

        mCurrentFragmentID = id

        when (id) {
            R.id.nav_saved_lyrics -> {
                collapseAppBar()
                lockAppBar()
            }
            R.id.nav_settings -> {
                collapseAppBar()
                lockAppBar()
            }
            R.id.nav_About -> {
                collapseAppBar()
                lockAppBar()
            }
            R.id.lyricListFragment -> {
                collapseAppBar()
                lockAppBar()
            }
            else -> unLockAppBar()
        }
    }

    private fun getTitle(id: Int): String {
        when (id) {
            R.id.nav_saved_lyrics -> return resources.getString(R.string.saved_lyrics)
            R.id.nav_settings -> return resources.getString(R.string.settings)
            R.id.nav_About -> {
                var versionName = "0"
                try {
                    versionName = packageManager
                            .getPackageInfo(packageName, 0).versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

                return versionName
            }
            else -> return resources.getString(R.string.lingua_lyrics)
        }
    }

    fun closeDrawers() {
        drawer_layout?.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle!!.onOptionsItemSelected(item))
            return true

        when (item.itemId) {
            android.R.id.home -> drawer_layout?.openDrawer(GravityCompat.START)
            R.id.search -> mSearchView?.showSearch()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        // TODO: improve this animation and made a transition from mTrackDetail to title
        if (mAppBarScrollRange == -1) {
            mAppBarScrollRange = appBarLayout.totalScrollRange
        }

        if (Math.abs(verticalOffset) > 3 * mAppBarScrollRange / 5) {
            collapsing_toolbar_layout?.title = getTitle(mCurrentFragmentID)
            track_detail.animate().alpha(0.0f).duration = 200
            mAppBarCollapsed = true
        } else {
            collapsing_toolbar_layout?.title = " "
            track_detail.animate()?.alpha(1f)?.duration = 50
            mAppBarCollapsed = false
        }
    }

    fun closeSearchView() {
        mSearchView?.closeSearch()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host)
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.nav_lyrics, false).build()
        navController.navigate(R.id.lyricListFragment, null, navOptions)

        mLyricViewModel?.setLyricQuery(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (v.id) {
            R.id.coordinator_layout, R.id.nestedScrollView -> if (isSearchViewOpen == true)
                closeSearchView()
        }
        return false
    }

    override fun onLyricFetchComplete(lyric: Lyric?) {
        if (lyric != null) {
            val title = lyric.title
            val artist = lyric.artist
            val imageUrl = lyric.coverArtImageUrl
            fillToolbarItems(imageUrl, artist, title)
        }
    }

    override fun onLyricFetchLoading() {
        fillToolbarItems(null, resources.getString(R.string.artist), resources.getString(R.string.title))
    }

    private fun fillToolbarItems(imageUrl: String?, artist: String?, title: String?) {
        var imageUrl = imageUrl
        music_title.text = title
        artist_text.text = artist
        val sp = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val artWork = sp.getString(resources.getString(R.string.settings_key_album_art), "0")
        val imageProxy = LyricsApi.BASE_URL + "api/v1/image/?image=" + imageUrl
        when (artWork) {
            "0" -> Picasso.get().load(imageProxy).into(cover_art)
            "1" -> Picasso.get().load(imageProxy).networkPolicy(NetworkPolicy.OFFLINE).into(cover_art)
            "2" -> {
                imageUrl = null
                Picasso.get().load(imageUrl as String).into(cover_art)
            }
        }
    }

    override fun onSearchViewShown() {
        // Hack: the collapsing toolbar title is shown on top of searchView
        // to fix that i make it transparent and reverse it when closed
        collapsing_toolbar_layout?.setCollapsedTitleTextColor(resources.getColor(R.color.transparent_white))
        val sp = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val showPreviousSearch = sp.getBoolean(resources.getString(R.string.settings_key_show_previous_search), true)
        if (showPreviousSearch)
            mSearchView?.setQuery(mLyricViewModel?.lastQuery, false)
    }

    override fun onSearchViewClosed() {
        collapsing_toolbar_layout?.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }
}
