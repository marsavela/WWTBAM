package es.serpat.wwtbam;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by SergiuDaniel on 10/10/13.
 */
public class Preferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}