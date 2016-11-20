package info.androidhive.cardview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.CommentListAdapter;

/**
 * Created by Luigi on 20-Nov-16.
 */

public class PreferencesFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);

        return rootView;
    }
}
