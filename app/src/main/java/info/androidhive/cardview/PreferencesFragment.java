package info.androidhive.cardview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group);
        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0);

        switch (theme) {
            case 1:
                radioGroup.check(R.id.radio_button1);
                break;
            case 2:
                radioGroup.check(R.id.radio_button2);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
                SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

                if (checkedId == R.id.radio_button1) {
                    prefs.edit().putInt("theme", 1).apply();
                } else {
                    prefs.edit().putInt("theme", 2).apply();
                }

                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    Toast toast = Toast.makeText(getActivity(), "Restart application to apply changes", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return rootView;
    }
}
