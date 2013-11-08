package es.serpat.wwtbam;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by SergiuDaniel on 13/10/13.
 */
public class ScoresActivity extends FragmentActivity implements ActionBar.TabListener, OnClickAlertDialogFragmentTwoChoices {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    private static DAOScores daoScores;
    private static ScoresAdapter adapterLocal;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        //actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.scores);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }

    /*private static void deleteDB() {
        daoScores.deleteDB(super.getBaseContext());
    }*/

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new localScores();

                default:
                    // Show all users
                    return new friendsScores();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.local);
                case 1:
                    return getString(R.string.friends);
                default:
                    return "wrong";
            }
        }
    }

    private void confirmDeletion() {
        DialogFragment fragment = AlertDialogFragmentTwoChoices.newInstance(this,
                getString(R.string.delete_scores),
                getString(R.string.delete_scores_confirmation),
                getString(R.string.no),
                getString(R.string.yes));
        fragment.setCancelable(false);
        fragment.show(getFragmentManager(), "rightDialog");
    }

    @Override
    public void doPositiveClick() {
        daoScores.deleteDB(this);
        adapterLocal.updateAdapter();
    }

    @Override
    public void doNegativeClick() {

    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class localScores extends ListFragment implements OnClickAlertDialogFragmentTwoChoices {

        public localScores() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            daoScores = new DAOScores(super.getActivity());
            daoScores.open();
            HighScoreList scores = daoScores.getAllScores();
            Collections.sort(scores.getScores());
            adapterLocal = new ScoresAdapter(getActivity(),scores.getScores());
            setListAdapter(adapterLocal);
            return inflater.inflate(R.layout.list_scores, container, false);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu items for use in the action bar
            inflater.inflate(R.menu.scores_menu, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                /*case android.R.id.home:
                    // This is called when the Home (Up) button is pressed in the action bar.
                    // Create a simple intent that starts the hierarchical parent activity and
                    // use NavUtils in the Support Package to ensure proper handling of Up.
                    NavUtils.navigateUpFromSameTask(this);
                    return true;*/
                case R.id.action_scores:
                    DialogFragment fragment = AlertDialogFragmentTwoChoices.newInstance(this,
                            getActivity().getString(R.string.delete_scores),
                            getActivity().getString(R.string.delete_scores_confirmation),
                            getActivity().getString(R.string.no),
                            getActivity().getString(R.string.yes));
                    fragment.setCancelable(false);
                    fragment.show(getActivity().getFragmentManager(), "rightDialog");
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void doPositiveClick() {
            daoScores.deleteDB(getActivity());
            adapterLocal.updateAdapter();
        }

        @Override
        public void doNegativeClick() {

        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class friendsScores extends ListFragment {

        public friendsScores() {
        }

        public boolean isConnected() {
            ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            return ((info != null) && (info.isConnected()));
        }

        private class GetFriendsAndScores extends AsyncTask<Void, Void, HighScoreList> {

            @Override
            protected HighScoreList doInBackground(Void... params) {
                HighScoreList scores = null;
                try {
                    ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("name", PreferenceManager.getDefaultSharedPreferences(getActivity()).
                            getString(getActivity().getResources().getString(R.string.SHARED_PREF_NAME_KEY),
                                    getActivity().getResources().getString(R.string.default_user_name))));

				/* This is just for GET/DELETE operation */
                    URL url = new URL("http://wwtbamandroid.appspot.com/rest/highscores?" +
                            URLEncodedUtils.format(list, "UTF-8"));

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                /* This was just for GET/DELETE operations */

                    Gson gson = new Gson();
                    scores = gson.fromJson(reader, HighScoreList.class);
                    reader.close();

                    connection.disconnect();

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return scores;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            HighScoreList scoreList = null;
            if (isConnected()) {
                GetFriendsAndScores task = new GetFriendsAndScores();
                try {
                    scoreList=task.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getActivity(), getResources().getString(R.string.not_connected),
                        Toast.LENGTH_SHORT).show();
            }
            ScoresAdapter adapterFriends = new ScoresAdapter(getActivity(), scoreList.getScores());
            setListAdapter(adapterFriends);
            return inflater.inflate(R.layout.list_score_friends, container, false);
        }
    }

}
