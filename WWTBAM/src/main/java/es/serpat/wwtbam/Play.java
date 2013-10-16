package es.serpat.wwtbam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class Play extends Activity {

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

        if (game.initialComprobations()) {
            Initialization();
            drawActualQuestion();
        }
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
            Question q = game.getQuestionList().get(game.getActualQuestion());

            TextView textQuestion = (TextView) findViewById(R.id.question);
            textQuestion.setText(q.getText());

            animationIn();

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
                    showJoker();
                } else {
                    showAlert();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle(getString(R.string.delete_scores))
                .setMessage(getString(R.string.delete_scores_confirmation))
                .show();
    }

    private void showJoker() {
        new AlertDialog.Builder(this).setTitle(R.string.pick_joker)
                .setAdapter(new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        game.getAllowedJokers()), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                }).show();
    }

    public void questionAnswered(String s) {
        if (s.equals("win")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle(getString(R.string.delete_scores))
                    .setMessage(getString(R.string.delete_scores_confirmation))
                    .show();
        } else if (s.equals("right")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle(getString(R.string.delete_scores))
                    .setMessage(getString(R.string.delete_scores_confirmation))
                    .setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
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

}