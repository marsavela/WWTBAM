package es.serpat.wwtbam;

import java.util.List;

public class HighScoreList {

	private List<Score> scores;

    public HighScoreList(List<Score> scores) {
        this.scores = scores;
    }

    public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	
}
