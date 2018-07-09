package ir.habibkazemi.lingualyrics.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import android.view.Menu
import android.view.MenuInflater

import ir.habibkazemi.lingualyrics.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.findItem(R.id.search)?.isVisible = false
    }
}
