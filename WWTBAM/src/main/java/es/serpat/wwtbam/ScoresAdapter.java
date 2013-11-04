package es.serpat.wwtbam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sergiu on 04/11/13.
 */
public class ScoresAdapter extends ArrayAdapter<Score> {

    private ArrayList<Score> scores;

    public ScoresAdapter(Context context, int resource, ArrayList<Score> scores) {
        super(context, resource, scores);
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View rootView = super.getView(position, convertView, parent);
        TextView name = (TextView) rootView.findViewById(R.id.score_name);
        TextView score = (TextView) rootView.findViewById(R.id.score_score);

        name.setText(scores.get(position).getName());
        score.setText(scores.get(position).getScore());

        return rootView;

    }
}
