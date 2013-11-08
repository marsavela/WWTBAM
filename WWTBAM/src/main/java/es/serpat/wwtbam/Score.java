package es.serpat.wwtbam;

/**
 * Created by SergiuDaniel on 24/10/13.
 */
public class Score implements Comparable<Score> {
    private int id;
    private String name;
    private int scoring;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScoring() {
        return scoring;
    }

    public void setScoring(int scoring) {
        this.scoring = scoring;
    }

    @Override
    public int compareTo(Score score) {
        if (this.scoring > score.scoring)
            return -1;
        else if (this.scoring < score.scoring)
            return 1;
        else return 0;
    }
}
