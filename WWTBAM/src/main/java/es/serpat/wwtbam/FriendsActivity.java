package es.serpat.wwtbam;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by SergiuDaniel on 13/10/13.
 */
public class FriendsActivity extends ListActivity  {

    final Context context = this;
    private String friend_name;
    private String name;
    private FriendList friends;
    private String[] values=new String[]{};
    ArrayAdapter<String> adapter;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.list_friends);

        // We save the Players name. We'll need it later
        name=PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getResources().getString(R.string.SHARED_PREF_NAME_KEY),
                        context.getResources().getString(R.string.default_user_name));

        // If the devices is connected to the Internet we get our friend's names
        if (isConnected()) {
            GetFriendsTask task = new GetFriendsTask();
            task.execute();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friends_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.add_new_friend:

                add_new_friend();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // We add a new friend to our list of friends
    private void add_new_friend() {

//        name=PreferenceManager.getDefaultSharedPreferences(context).
//                getString(context.getResources().getString(R.string.SHARED_PREF_NAME_KEY),
//                        context.getResources().getString(R.string.default_user_name));

        // New dialog to enter the name of our new friend
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(R.string.action_addFriend); //Set Alert dialog title here
        alert.setMessage("Enter Your Friends Name Here"); //Message here

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        // Listener for the "OK" button. If pressed, we save the name of our friend, and post it.
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                friend_name = input.getEditableText().toString();
                Toast.makeText(context, friend_name + " added to your friends!", Toast.LENGTH_LONG).show();
                if (isConnected()) {
                    PostFriendTask task = new PostFriendTask();
                    task.execute();
                }
                else {
                    Toast.makeText(getParent(), getResources().getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
                }

            }
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    // This task posts the name of our new friend and adds it to our list of friends
    private class PostFriendTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
        // TODO Auto-generated method stub

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("name", name));
            pairs.add(new BasicNameValuePair("friend_name", friend_name));

            try {
                URL url = new URL("http://wwtbamandroid.appspot.com/rest/friends");
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                writer.write(URLEncodedUtils.format(pairs, "UTF-8"));
                writer.close();

                connection.getInputStream();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            GetFriendsTask task = new GetFriendsTask();
            task.execute();
            setProgressBarIndeterminateVisibility(false);
        }
    }

    private class GetFriendsTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

                try {
                    URL url = new URL("http://wwtbamandroid.appspot.com/rest/friends?name="+name);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    Gson gson = new Gson();
                    friends = gson.fromJson(reader, FriendList.class);
                    reader.close();

                    values = friends.getFriends().toArray(new String[friends.getFriends().size()]);

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

   //         adapter.notifyDataSetChanged();
            // We add all the names of our friends to the friends layout
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendsActivity.this,
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
            setProgressBarIndeterminateVisibility(false);
        }
    }

    // Here we check if our device is connected to the Internet
    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return ((info != null) && (info.isConnected()));
    }
}

