package es.serpat.wwtbam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast toast = Toast.makeText(this, "Welcome", Toast.LENGTH_LONG);
        toast.show();

        Button bPlay = (Button) findViewById(R.id.butPlay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGame(null);
            }
        });
        Button bPreferences = (Button) findViewById(R.id.butSettings);
        bPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPreferences(null);
            }
        });
        Button bScores = (Button) findViewById(R.id.butScores);
        bScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchScores(null);
            }
        });
        Button bAbout = (Button) findViewById(R.id.butAbout);
        bAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                launchAbout(null);
            }
        });
    }

    private void launchScores(View view) {
        Intent i = new Intent(this, Credits.class);
        startActivity(i);
    }

    private void launchGame(View view) {
        //Intent i = new Intent(this, Game.class);
        //startActivity(i);
    }

    private void launchPreferences(View view) {
        Intent i = new Intent(this, SetPreferences.class);
        startActivity(i);
    }

    public void launchAbout(View view) {
        Intent i = new Intent(this, Credits.class);
        startActivity(i);
    }
    
}
