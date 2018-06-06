package habibkazemi.ir.lingualyrics_android.ui.savedlyrics;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import habibkazemi.ir.lingualyrics_android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedLyricsFragment extends Fragment {


    public SavedLyricsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_lyrics, container, false);
    }

}
