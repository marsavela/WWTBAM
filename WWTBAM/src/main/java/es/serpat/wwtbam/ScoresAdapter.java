package es.serpat.wwtbam;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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
        score.setText(Integer.toString(list.get(position).getScoring()));
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
}
