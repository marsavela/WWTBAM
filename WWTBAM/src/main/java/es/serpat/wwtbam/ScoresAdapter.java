package es.serpat.wwtbam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by sergiu on 04/11/13.
 */
public class ScoresAdapter extends BaseAdapter {

    private List<Score> scores = Collections.emptyList();

    private final Context context;

    public ScoresAdapter(Context context) {
        this.context = context;
    }

    public void updateScores(List<Score> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
