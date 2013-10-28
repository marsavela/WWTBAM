package es.serpat.wwtbam;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
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
public class PlayActivity extends FragmentActivity implements OnClickAlertDialogFragmentTwoChoices, OnClickAlertDialogFragmentList {

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
        //SharedPreferences preferences = this.getSharedPreferences(getResources().getString(R.string.pref_file), Context.MODE_PRIVATE);

        //if (game.isGameSaved())
            //game.restoreGameData();

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
                //TODO arreglar esto. Ya he implementado los Dialog propios, pero hay que hacer que
                //funcionen con las diferentes pantallas.
                //showDialog();
                if (game.areJokersLeft()) {
                    //showAvailableJokers();
                    DialogFragment fragment = AlertDialogFragmentList.newInstance(this, game.getAllowedJokers());
                    fragment.show(getFragmentManager(), "list_of_jokers");
                } else {
                    DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(getString(R.string.sorry),getString(R.string.sorry_message));
                    fragment.show(getFragmentManager(), "noAvailableJokers");
                    //showAlertNoAvailableJokers();
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

        /*if (game.shouldFinish()) {
            game.setShouldNotFinish();
            finish();
        }*/

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

    private void showAlertNoAvailableJokers() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle(getString(R.string.sorry))
                .setMessage(getString(R.string.sorry_message))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
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
                        game.askForJoker(game.getJokerName(which));
                    }
                }).show();
    }

    public void questionAnswered(String s) {
        if (s.equals("win")) {
            DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(
                    getString(R.string.congratulations),getString(R.string.congratulations_message));
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "winDialog");
            /*new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.star_on)
                    .setCancelable(false)
                    .setTitle(getString(R.string.congratulations))
                    .setMessage(getString(R.string.congratulations_message))
                    .setNeutralButton(getString(R.string.finish), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .show();*/
        } else if (s.equals("right")) {
            //TODO Hacer un Dialog propio para que no pete cuando gira la pantalla.
            DialogFragment fragment = AlertDialogFragmentTwoChoices.newInstance(this,
                    getString(R.string.right),
                    getString(R.string.right_message));
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "rightDialog");
            /*new AlertDialog.Builder(this)
                    .setIcon(R.drawable.btn_check_on)
                    .setCancelable(false)
                    .setTitle(getString(R.string.right))
                    .setMessage(getString(R.string.right_message))
                    .setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drawActualQuestion();
                        }

                    })
                    .setNegativeButton(getString(R.string.give_up), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            game.saveScore();
                            game.setUnsavedGame();
                            finish();
                        }

                    })
                    .show();*/
        } else if (s.equals("wrong")) {
            DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(
                    getString(R.string.you_lost),"wrong");
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "wrongDialog");
            /*new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setCancelable(false)
                    .setTitle(getString(R.string.you_lost))
                    .setMessage(getString(R.string.try_again))
                    .setNeutralButton(getString(R.string.understood), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .show();*/
        }
    }

    /**
     * Called by the game class to hide a false answer when the 50/50 joker is used
     *
     * @param id , the number of the button to hide
     **/
    public void hideButton(String id) {
        Animation out = AnimationUtils.loadAnimation(this,R.anim.fade_out);

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
        /*new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_call)
                .setTitle(getString(R.string.phone))
                .setMessage(getString(R.string.phone_message) + " " + phoneAnswer + "\"")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .show();*/
    }

    public void showAudienceAnswer(String audienceAnswer) {
        DialogFragment fragment = AlertDialogFragmentOneChoice.newInstance(audienceAnswer, getString(R.string.audience_message));
        fragment.show(getFragmentManager(), "jokerAudienceAnswer");
        /*new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_call)
                .setTitle(getString(R.string.audience))
                .setMessage(getString(R.string.audience_message) + " " + audienceAnswer + "\"")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .show();*/
    }

    // Dialog

    private void showDialog() {
        DialogFragment newFragment = AlertDialogFragmentTwoChoices.newInstance(this,
                getString(R.string.ok),
                getString(R.string.app_name));
        newFragment.show(getFragmentManager(), "dialog");
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
