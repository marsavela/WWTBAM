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

    Game game;
    Integer actualQuestion;
    Integer timesHelped;
    SharedPreferences preferences;

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
        preferences = this.getSharedPreferences("es.serpat.wwtbam_preferences", Context.MODE_PRIVATE);

        Initialization();
        drawActualQuestion(actualQuestion);
    }

    private void Initialization() {
        actualQuestion = 0;
        timesHelped = 0;

        game.setHelp_audience(true);
        game.setHelp_fifty(true);
        game.setHelp_phone(true);

        Button buttonA = (Button) findViewById(R.id.butA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationOut();
                actualQuestion++;
                drawActualQuestion(actualQuestion);
            }
        });
        Button buttonB = (Button) findViewById(R.id.butB);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualQuestion++;
                drawActualQuestion(actualQuestion);
            }
        });
        Button buttonC = (Button) findViewById(R.id.butC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualQuestion++;
                drawActualQuestion(actualQuestion);
            }
        });
        Button buttonD = (Button) findViewById(R.id.butD);
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualQuestion++;
                drawActualQuestion(actualQuestion);
            }
        });

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

    private void drawActualQuestion(Integer actualQuestion) {
        if (actualQuestion>=game.getQuestionList().size()) {
            animationOut();
        } else {
            Question q = game.returnQuestion(actualQuestion);

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

        }
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
            case R.id.action_play_help:
                if(timesHelped<Integer.parseInt(preferences.getString("numberHelps","NULL"))) {
                    showHelpers();
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

    private void showHelpers() {
        String[] helpers = getResources().getStringArray(R.array.helpers_array);
        final List<String> list =  new ArrayList<String>();
        Collections.addAll(list, helpers);
        list.remove(1);
        helpers = list.toArray(new String[list.size()]);
        /*if(!game.isHelp_phone())
            helpers[0] = null;
        //if(!game.isHelp_fifty())
            helpers[1] = null;
        if(!game.isHelp_audience())
            helpers[2] = null;*/
        ArrayAdapter<String> adapter;
        /* Assign the name array to that adapter and
        also choose a simple layout for the list items */
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                helpers);

        new AlertDialog.Builder(this).setTitle(R.string.pick_helper)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                }).show();
    }


}
