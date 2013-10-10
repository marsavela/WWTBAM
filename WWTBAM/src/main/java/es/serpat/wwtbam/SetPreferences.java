package es.serpat.wwtbam;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by SergiuDaniel on 2/07/13.
 */
public class SetPreferences extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new Preferences()).commit();
    }
}
