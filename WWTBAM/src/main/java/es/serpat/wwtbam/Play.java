package es.serpat.wwtbam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class Play extends Activity {

    private Game game;

    SharedPreferences preferences;

    //Constants for saved data.
    private String              SHARED_PREF_QUESTION_NUMBER    = "questionNumber";
    private String              SHARED_PREF_NAME_KEY           = "playerName";
    private String              SHARED_PREF_AUDIENCE           = "isUsedAudienceJoker";
    private String              SHARED_PREF_FIFTY_USED         = "questionFiftyJokerWereUsed";
    private String              SHARED_PREF_FIFTY              = "isUsedFiftyJoker";
    private String              SHARED_PREF_PHONE              = "isUsedPhoneJoker";

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

        preferences = this.getSharedPreferences(getResources().getString(R.string.pref_file), Context.MODE_PRIVATE);


        if (game.isGameSaved())
            game.restoreGameData();

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
                if(game.areJokersLeft()) {
                    showAvailableJokers();
                } else {
                    showAlertNoAvailableJokers();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        reDrawActualQuestion();


        if (game.isUsedFiftyJoker() && game.getQuestionFiftyJokerWereUsed()==game.getActualQuestion()) {
            reHideButton(game.numToLetter(game.getQuestionList().get(game.getActualQuestion()).fifty1));
            reHideButton(game.numToLetter(game.getQuestionList().get(game.getActualQuestion()).fifty2));
            //game.askForJoker(getResources().getString(R.string.fifty));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("STOPPING", "Inside of onSaveInstanceState");

        if (game.getActualQuestion()<game.getQuestionList().size())
            game.saveGameData();
/*
        outState.putBoolean(SHARED_PREF_GAME, true);
        outState.putInt(SHARED_PREF_QUESTION_NUMBER, game.getActualQuestion());
        outState.putString(SHARED_PREF_NAME_KEY, game.getPlayerName());
        outState.putBoolean(SHARED_PREF_PHONE, game.isUsedPhoneJoker());
        outState.putBoolean(SHARED_PREF_FIFTY, game.isUsedFiftyJoker());
        outState.putInt(SHARED_PREF_FIFTY_USED, game.getQuestionFiftyJokerWereUsed());
        outState.putBoolean(SHARED_PREF_AUDIENCE, game.isUsedAudienceJoker());*/
        Log.v("STOPPING", "ActualQuestion: " + Integer.toString(game.getActualQuestion()));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("STARTING", "Inside of onRestoreInstanceState");

        Log.v("STARTING", "ActualQuestion: " + Integer.toString(game.getActualQuestion()));
        //isGameSaved = preferences.getBoolean(SHARED_PREF_GAME, false);
        Log.w("STARTING AGAIN", Boolean.toString(game.isGameSaved()));

        /*game.setActualQuestion(savedInstanceState.getInt(SHARED_PREF_QUESTION_NUMBER));
        game.setPlayerName(savedInstanceState.getString(SHARED_PREF_NAME_KEY));
        game.setUsedPhoneJoker(savedInstanceState.getBoolean(SHARED_PREF_PHONE));
        game.setUsedFiftyJoker(savedInstanceState.getBoolean(SHARED_PREF_FIFTY));
        game.setQuestionFiftyJokerWereUsed(savedInstanceState.getInt(SHARED_PREF_FIFTY_USED));
        game.setUsedAudienceJoker(savedInstanceState.getBoolean(SHARED_PREF_AUDIENCE));*/
        Log.v("STARTING", "ActualQuestion: " + Integer.toString(game.getActualQuestion()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("DESTROYYY", "ActualQuestion: " + Integer.toString(game.getActualQuestion()));
        Log.w("DESTROYYYY", Boolean.toString(game.isGameSaved()));
    }

    private void animationOut() {
        Animation out = AnimationUtils.loadAnimation(this,
                R.anim.fade_out);

        Button buttonA = (Button) findViewById(R.id.butA);
        Button buttonB = (Button) findViewById(R.id.butB);
        Button buttonC = (Button) findViewById(R.id.butC);
        Button buttonD = (Button) findViewById(R.id.butD);

        TextView question = (TextView) findViewById(R.id.question);
        TextView a = (TextView) findViewById(R.id.textA);
        TextView b = (TextView) findViewById(R.id.textB);
        TextView c = (TextView) findViewById(R.id.textC);
        TextView d = (TextView) findViewById(R.id.textD);

        question.startAnimation(out);
        a.startAnimation(out);
        b.startAnimation(out);
        c.startAnimation(out);
        d.startAnimation(out);

        buttonA.startAnimation(out);
        buttonB.startAnimation(out);
        buttonC.startAnimation(out);
        buttonD.startAnimation(out);
    }

    private void animationIn() {
        Animation in = AnimationUtils.loadAnimation(this,
                R.anim.fade_in);

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
        Log.v("DRAWING", "ActualQuestion: " + Integer.toString(game.getActualQuestion()));

        animationIn();
        reDrawActualQuestion();

    }

    private void reDrawActualQuestion() {
        Question q = game.getQuestionList().get(game.getActualQuestion());

        TextView textQuestion = (TextView) findViewById(R.id.question);
        textQuestion.setText(q.getText());

        Button buttonA = (Button) findViewById(R.id.butA);
        buttonA.setText(q.answer1);
        Button buttonB = (Button) findViewById(R.id.butB);
        buttonB.setText(q.answer2);
        Button buttonC = (Button) findViewById(R.id.butC);
        buttonC.setText(q.answer3);
        Button buttonD = (Button) findViewById(R.id.butD);
        buttonD.setText(q.answer4);

        // Enable and display all buttons if they had been disabled for the Fifty joker
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);
    }

    private void showAlertNoAvailableJokers() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle(getString(R.string.sorry))
                .setMessage(getString(R.string.sorry_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .show();
    }

    private void showAvailableJokers() {
        new AlertDialog.Builder(this).setTitle(R.string.pick_joker)
                .setAdapter(new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        game.getAllowedJokers()), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Log.w("APRETADO",Integer.toString(which));
                        game.askForJoker(game.getJokerName(which));
                    }
                }).show();
    }

    public void questionAnswered(String s) {
        if (s.equals("win")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.star_on)
                    .setTitle(getString(R.string.congratulations))
                    .setMessage(getString(R.string.congratulations_message))
                    .show();
        } else if (s.equals("right")) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.btn_check_on)
                    .setCancelable(false)
                    .setTitle(getString(R.string.right))
                    .setMessage(getString(R.string.right_message))
                    .setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drawActualQuestion();
                        }

                    })
                    .setNegativeButton(getString(R.string.give_up), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drawActualQuestion();
                        }

                    })
                    .show();
        } else if (s.equals("wrong")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle(getString(R.string.you_lost))
                    .setMessage(getString(R.string.try_again))
                    .show();
        }
    }

    /**
     * Called by the game class to hide a false answer when the 50/50 joker is used
     *
     * @param id
     *            , the number of the button to hide
     */
    public void hideButton(String id) {
        Animation out = AnimationUtils.loadAnimation(this,
                R.anim.fade_out);

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
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_call)
                .setTitle(getString(R.string.phone))
                .setMessage(getString(R.string.phone_message) + " " + phoneAnswer + "\"")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .show();
    }

    public void showAudienceAnswer(String audienceAnswer) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_call)
                .setTitle(getString(R.string.audience))
                .setMessage(getString(R.string.audience_message) + " " + audienceAnswer + "\"")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .show();
    }
}
