package es.serpat.wwtbam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by sergiu on 04/11/13.
 */
public class ScoresAdapter extends BaseAdapter {

    private final Activity activity;
    private List<Score> list;

    public ScoresAdapter(Activity activity, List<Score> scoreList) {
        super();
        this.activity = activity;
        this.list = scoreList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.score_row, null,true);
        TextView name = (TextView) rootView.findViewById(R.id.score_name);
        TextView score = (TextView) rootView.findViewById(R.id.score_score);

        name.setText(list.get(position).getName());
        score.setText(Integer.toString(list.get(position).getScore()));
        return rootView;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    public long getItemId(int position) {
        return position;
    }

    public void updateAdapter() {
        list.clear();
        notifyDataSetChanged();
    }

    /*private List<Score> scores;
    private Context context;

    public ScoresAdapter(Context context, List<Score> scores) {
        super();
        this.context = context;
        this.scores = scores;
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Object getItem(int i) {
        return scores.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.score_row, null,true);
        TextView name = (TextView) rootView.findViewById(R.id.score_name);
        TextView score = (TextView) rootView.findViewById(R.id.score_score);

        name.setText(scores.get(position).getName());
        score.setText(scores.get(position).getScore());

        return rootView;

    }*/
}
