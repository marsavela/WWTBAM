package es.serpat.wwtbam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class PlayActivity extends Activity implements OnClickAlertDialogFragmentTwoChoices, OnClickAlertDialogFragmentList {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        game = new Game(this);

        Initialization();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play_menu, menu);
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
            case R.id.action_play_joker:
                if (game.areJokersLeft()) {
                    DialogFragment fragment = AlertDialogFragmentList.newInstance(this, game.getAllowedJokers());
                    fragment.show(getFragmentManager(), "list_of_jokers");
                } else {
                    DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(getString(R.string.sorry), getString(R.string.sorry_message));
                    fragment.show(getFragmentManager(), "noAvailableJokers");
                }
                return true;
            case R.id.action_play_end_game:
                game.saveScore();
                game.setUnsavedGame();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (game.getActualQuestion() == 0 && !game.isGameSaved())
            drawActualQuestion();
        else
            reDrawActualQuestion();

        if (game.isUsedFiftyJoker() && game.getQuestionFiftyJokerWereUsed() == game.getActualQuestion()) {
            reHideButton(game.numToLetter(game.getQuestionList().get(game.getActualQuestion()).fifty1));
            reHideButton(game.numToLetter(game.getQuestionList().get(game.getActualQuestion()).fifty2));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.saveGameData();
    }

    private void animationIn() {
        Animation in = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        Button buttonA = (Button) findViewById(R.id.butA);
        Button buttonB = (Button) findViewById(R.id.butB);
        Button buttonC = (Button) findViewById(R.id.butC);
        Button buttonD = (Button) findViewById(R.id.butD);

        TextView question = (TextView) findViewById(R.id.question);
        TextView a = (TextView) findViewById(R.id.textA);
        TextView b = (TextView) findViewById(R.id.textB);
        TextView c = (TextView) findViewById(R.id.textC);
        TextView d = (TextView) findViewById(R.id.textD);

        question.startAnimation(in);
        a.startAnimation(in);
        b.startAnimation(in);
        c.startAnimation(in);
        d.startAnimation(in);

        buttonA.startAnimation(in);
        buttonB.startAnimation(in);
        buttonC.startAnimation(in);
        buttonD.startAnimation(in);
    }

    private void Initialization() {

        Button buttonA = (Button) findViewById(R.id.butA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.testAnswer("1");
            }
        });
        Button buttonB = (Button) findViewById(R.id.butB);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.testAnswer("2");
            }
        });
        Button buttonC = (Button) findViewById(R.id.butC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.testAnswer("3");
            }
        });
        Button buttonD = (Button) findViewById(R.id.butD);
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.testAnswer("4");
            }
        });

    }

    private void drawActualQuestion() {
        animationIn();
        reDrawActualQuestion();
    }

    private void reDrawActualQuestion() {
        Question q = game.getQuestionList().get(game.getActualQuestion());
        TextView textQuestion = (TextView) findViewById(R.id.question);
        TextView a = (TextView) findViewById(R.id.textA);
        TextView b = (TextView) findViewById(R.id.textB);
        TextView c = (TextView) findViewById(R.id.textC);
        TextView d = (TextView) findViewById(R.id.textD);
        Button buttonA = (Button) findViewById(R.id.butA);
        Button buttonB = (Button) findViewById(R.id.butB);
        Button buttonC = (Button) findViewById(R.id.butC);
        Button buttonD = (Button) findViewById(R.id.butD);

        textQuestion.setText(q.text);

        buttonA.setText(q.answer1);
        buttonB.setText(q.answer2);
        buttonC.setText(q.answer3);
        buttonD.setText(q.answer4);

        // Enable and display all buttons and text if they had been disabled for the Fifty joker
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);
        a.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
        c.setVisibility(View.VISIBLE);
        d.setVisibility(View.VISIBLE);
    }

    public void questionAnswered(String s) {
        if (s.equals("win")) {
            DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(
                    getString(R.string.congratulations), getString(R.string.congratulations_message));
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "winDialog");
        } else if (s.equals("right")) {
            DialogFragment fragment = AlertDialogFragmentTwoChoices.newInstance(this,
                    getString(R.string.right),
                    getString(R.string.right_message),
                    getString(R.string.give_up),
                    getString(R.string.next));
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "rightDialog");
        } else if (s.equals("wrong")) {
            DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(
                    getString(R.string.you_lost), "wrong");
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "wrongDialog");
        }
    }

    /**
     * Called by the game class to hide a false answer when the 50/50 joker is used
     *
     * @param id , the number of the button to hide
     */
    public void hideButton(String id) {
        Animation out = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        Button button = reHideButton(id);
        TextView text = (TextView) findViewById(getResources().getIdentifier("text" + id, "id",
                this.getBaseContext().getPackageName()));

        text.setAnimation(out);
        button.setAnimation(out);
    }

    public Button reHideButton(String id) {
        Button button = (Button) findViewById(getResources().getIdentifier("but" + id, "id",
                this.getBaseContext().getPackageName()));
        TextView text = (TextView) findViewById(getResources().getIdentifier("text" + id, "id",
                this.getBaseContext().getPackageName()));

        text.setVisibility(View.INVISIBLE);
        button.setEnabled(false);
        button.setVisibility(View.INVISIBLE);

        return button;
    }

    public void showPhoneCallAnswer(String phoneAnswer) {
        DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(phoneAnswer, getString(R.string.phone_message));
        fragment.show(getFragmentManager(), "jokerPhoneAnswer");
    }

    public void showAudienceAnswer(String audienceAnswer) {
        DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(audienceAnswer, getString(R.string.audience_message));
        fragment.show(getFragmentManager(), "jokerAudienceAnswer");
    }

    public void sendScore(int score) {
        if (isConnected()) {
            ScoreTask task = new ScoreTask(score);
            task.execute();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private class ScoreTask extends AsyncTask<Void, Void, Void>{

        Context context;

        int score;

        public ScoreTask(int score) {
            this.score = score;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {/*
                PreferenceManager.getDefaultSharedPreferences(context).
                        getString(context.getResources().getString(R.string.SHARED_PREF_NAME_KEY),
                                context.getResources().getString(R.string.default_user_name));*/
                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "Sergiu"));
                list.add(new BasicNameValuePair("score", Integer.toString(32001)));

				/* This is just for POST/PUT operations */

				URL url = new URL("http://wwtbamandroid.appspot.com/rest/highscores");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("PUT");
				connection.setDoOutput(true);
                connection.setDoInput(true);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
				writer.write(URLEncodedUtils.format(list, "UTF-8"));
				writer.close();
                connection.connect();

                Log.v("NOSEEEEE", "Ya he anviado.");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				/* This was just for POST/PUT operations */

                StringBuffer buffer = new StringBuffer();
                String s;
                while ((s = reader.readLine()) != null) {
                    buffer.append(s);
                    Log.v("NOSEEEEE", s);
                }
                reader.close();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return ((info != null) && (info.isConnected()));
    }

    @Override
    public void doPositiveClick() {
        drawActualQuestion();
    }

    @Override
    public void doNegativeClick() {
        game.saveScore();
        game.setUnsavedGame();
        finish();
    }

    @Override
    public void doAskForJoker(int which) {
        game.askForJoker(game.getJokerName(which));
    }

}
