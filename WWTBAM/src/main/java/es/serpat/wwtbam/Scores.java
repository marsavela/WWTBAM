package es.serpat.wwtbam;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SergiuDaniel on 13/10/13.
 */
public class Scores extends FragmentActivity implements ActionBar.TabListener {

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scores_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_scores:
                confirmDeletion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle(getString(R.string.delete_scores))
                .setMessage(getString(R.string.delete_scores_confirmation))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

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
                    return new allScores();
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

    /**
     * A fragment that launches other parts of the demo application.
     */
    public class localScores extends ListFragment {

        private ArrayList<Map<String, String>> buildData() {
            ArrayList<Map<String, String>> listScores = new ArrayList<Map<String, String>>();
            listScores.add(putData("Android", "3000"));
            listScores.add(putData("Windows7", "5000"));
            listScores.add(putData("iPhone", "65454"));
            return listScores;
        }

        private HashMap<String, String> putData(String name, String score) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("name", name);
            item.put("score", score);
            return item;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.list_scores, container, false);
            ArrayList<Map<String, String>> list = buildData();
            String[] from = { "name", "score" };
            int[] to = { android.R.id.text1, android.R.id.text2 };

            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list,
                    android.R.layout.simple_list_item_2, from, to);
            setListAdapter(adapter);
            return rootView;
        }

    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public class allScores extends ListFragment {

        private ArrayList<Map<String, String>> buildData() {
            ArrayList<Map<String, String>> listScores = new ArrayList<Map<String, String>>();
            listScores.add(putData("Android", "3000"));
            listScores.add(putData("Windows7", "5000"));
            listScores.add(putData("iPhone", "65454"));
            return listScores;
        }

        private HashMap<String, String> putData(String name, String score) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("name", name);
            item.put("score", score);
            return item;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.list_score_friends, container, false);
            ArrayList<Map<String, String>> list = buildData();
            String[] from = { "name", "score" };
            int[] to = { android.R.id.text1, android.R.id.text2 };

            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list,
                    android.R.layout.simple_list_item_2, from, to);
            setListAdapter(adapter);
            return rootView;
        }
    }

}