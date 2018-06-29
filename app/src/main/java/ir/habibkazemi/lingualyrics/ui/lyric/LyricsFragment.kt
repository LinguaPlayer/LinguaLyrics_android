package ir.habibkazemi.lingualyrics.ui.lyric

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import ir.habibkazemi.lingualyrics.ui.main.MainActivity
import ir.habibkazemi.lingualyrics.R
import ir.habibkazemi.lingualyrics.util.Constants
import ir.habibkazemi.lingualyrics.vo.Lyric
import ir.habibkazemi.lingualyrics.vo.Resource
import ir.habibkazemi.lingualyrics.vo.Status
import timber.log.Timber
import kotlinx.android.synthetic.main.fragment_lyrics.*

class LyricsFragment : Fragment() {

    private var mLyricViewModel: LyricViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.findItem(R.id.search)?.isVisible = true
    }

    private fun fetchingLyricFailed(lyricResource: Resource<Lyric>?) {
        hideSpinner()
        showMessageError(R.drawable.ic_network_error, resources.getString(R.string.network_error))
    }

    private fun showMessageError(@DrawableRes imageResource: Int, message: String) {
        lyric_text.visibility = View.GONE
        message_group.visibility = View.VISIBLE
        message_text.text = message
        message_icon.setImageResource(imageResource)
    }

    private fun fetchingLyricLoading() {
        showSpinner()
        (activity as OnLyricListener).onLyricFetchLoading()

    }

    private fun fetchingLyricSucceed(lyricResource: Resource<Lyric>?) {
        hideSpinner()
        (activity as OnLyricListener).onLyricFetchComplete(lyricResource!!.data)

        if (lyricResource?.data != null) {
            lyric_text?.visibility = View.VISIBLE
            message_group?.visibility = View.GONE
            mLyricViewModel?.setLastLyric(lyricResource.data)
            val lyricText = lyricResource.data.lyricText
            lyric_text?.text = "$lyricText \n\n Source: ${lyricResource.data.source}\n"
        } else {
            showMessageError(R.drawable.ic_not_found, resources.getString(R.string.no_lyrics_found))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyrics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLyricViewModel = ViewModelProviders.of(activity!!).get(LyricViewModel::class.java)

        mLyricViewModel?.lyric?.observe(this, Observer {
            lyricResource ->
                when (lyricResource?.status) {
                    Status.LOADING -> fetchingLyricLoading()
                    Status.SUCCESS -> fetchingLyricSucceed(lyricResource)
                    Status.ERROR -> fetchingLyricFailed(lyricResource)
                }
        })

        val sp = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context!!)
        val expandAppBar = sp.getBoolean(resources.getString(R.string.settings_key_expand_app_bar), true)
        val fromLyricList = arguments?.getBoolean(Constants.FROM_LYRIC_LIST, false) ?: false
        // If it navigates from lyricListFragments checks the settings
        // else it preserves the previous state
        if (fromLyricList) {
            if (expandAppBar)
                (activity as MainActivity).expandAppBar()
        } else {
            (activity as MainActivity).prepareAppBarCollapsedExpandedState()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun showSpinner() {
        progress_loader?.visibility = View.VISIBLE
    }

    private fun hideSpinner() {
        progress_loader?.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("LyricsFragments: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("LyricsFragments: onDestroy")

    }

    interface OnLyricListener {
        fun onLyricFetchComplete(lyric: Lyric?)
        fun onLyricFetchLoading()
    }
}
