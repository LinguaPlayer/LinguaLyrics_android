package habibkazemi.ir.lingualyrics_android.ui.lyriclist


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import habibkazemi.ir.lingualyrics_android.R
import habibkazemi.ir.lingualyrics_android.ui.lyric.LyricViewModel
import habibkazemi.ir.lingualyrics_android.util.Constants
import habibkazemi.ir.lingualyrics_android.vo.LyricLink
import habibkazemi.ir.lingualyrics_android.vo.Status
import kotlinx.android.synthetic.main.fragment_lyric_list.*


class LyricListFragment : Fragment(), LyricRecyclerAdapter.OnItemClickListener {

    private var mLyricViewModel: LyricViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lyric_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Disable the nestedScrolling to disable expanding the
           appBar with dragging the mNestedScrollView below it */
        ViewCompat.setNestedScrollingEnabled(lyric_list_recyclerview, false)

        val lyricRecyclerAdapter = LyricRecyclerAdapter()
        lyricRecyclerAdapter.setOnItemClickListener(this)
        lyric_list_recyclerview?.layoutManager = LinearLayoutManager(activity)
        lyric_list_recyclerview?.adapter = lyricRecyclerAdapter

        mLyricViewModel = ViewModelProviders.of(activity!!).get(LyricViewModel::class.java)

        mLyricViewModel?.lyricList?.observe(this, Observer { listResource ->
            if (listResource?.status === Status.LOADING) {
                showSpinner()
            }

            if (listResource?.status === Status.SUCCESS) {
                hideSpinner()
                if (listResource.data == null || listResource.data?.isEmpty())
                    showMessageError(R.drawable.ic_not_found, resources.getString(R.string.no_lyrics_found))
            }

            if (listResource?.status === Status.ERROR) {
                hideSpinner()
                showMessageError(R.drawable.ic_network_error, resources.getString(R.string.network_error))
            }

            if (listResource?.data != null) {
                lyricRecyclerAdapter.submitList(listResource.data)
            }
        })
    }

    fun showMessageError(@DrawableRes imageResource: Int, message: String) {
        lyric_list_recyclerview?.visibility = View.GONE
        message_group?.visibility = View.VISIBLE
        message_text?.text = message
        message_icon?.setImageResource(imageResource)
    }

    override fun onItemClick(lyricLink: LyricLink?) {
        val bundle = Bundle()
        bundle.putBoolean(Constants.FROM_LYRIC_LIST, true)
        val navController = Navigation.findNavController(activity!!, R.id.nav_host)
        navController.navigate(R.id.action_lyricListFragment_to_nav_lyrics, bundle)
        mLyricViewModel?.queryLyricByUrl(lyricLink?.url)
    }


    private fun showSpinner() {
        progress_loader?.visibility = View.VISIBLE
    }

    private fun hideSpinner() {
        progress_loader?.visibility = View.INVISIBLE
    }
}
