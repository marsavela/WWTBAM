package es.serpat.wwtbam;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class Game extends FragmentActivity {

    private List<Question> questionList;
    private boolean help_phone;
    private boolean help_audience;
    private boolean help_fifty;

    Play activity;

    public Game(Play activity) {
        this.activity = activity;

        help_audience = true;
        help_fifty = false;
        help_phone = true;

        questionList = generateQuestionList();
    }

    public int availableHelps() {
        int helps = 0;
        if(help_audience) helps++;
        if(help_fifty) helps++;
        if(help_phone) helps++;
        return helps;
    }

    public Question returnQuestion(int q) {
        return questionList.get(q);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public boolean isHelp_phone() {
        return help_phone;
    }

    public void setHelp_phone(boolean help_phone) {
        this.help_phone = help_phone;
    }

    public boolean isHelp_audience() {
        return help_audience;
    }

    public void setHelp_audience(boolean help_audience) {
        this.help_audience = help_audience;
    }

    public boolean isHelp_fifty() {
        return help_fifty;
    }

    public void setHelp_fifty(boolean help_fifty) {
        this.help_fifty = help_fifty;
    }

    public List<Question> generateQuestionList() {
        List<Question> list = new ArrayList<Question>();
        Question q = null;

        q = new Question(
                "1",
                "Which is the Sunshine State of the US?",
                "North Carolina",
                "Florida",
                "Texas",
                "Arizona",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "2",
                "Which of these is not a U.S. state?",
                "New Hampshire",
                "Washington",
                "Wyoming",
                "Manitoba",
                "4",
                "4",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "3",
                "What is Book 3 in the Pokemon book series?",
                "Charizard",
                "Island of the Giant Pokemon",
                "Attack of the Prehistoric Pokemon",
                "I Choose You!",
                "3",
                "2",
                "3",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "4",
                "Who was forced to sign the Magna Carta?",
                "King John",
                "King Henry VIII",
                "King Richard the Lion-Hearted",
                "King George III",
                "1",
                "3",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "5",
                "Which ship was sunk in 1912 on its first voyage, although people said it would never sink?",
                "Monitor",
                "Royal Caribean",
                "Queen Elizabeth",
                "Titanic",
                "4",
                "4",
                "4",
                "1",
                "2"
        );
        list.add(q);

        q = new Question(
                "6",
                "Who was the third James Bond actor in the MGM films? (Do not include &apos;Casino Royale&apos;.)",
                "Roger Moore",
                "Pierce Brosnan",
                "Timothy Dalton",
                "Sean Connery",
                "1",
                "3",
                "3",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "7",
                "Which is the largest toothed whale?",
                "Humpback Whale",
                "Blue Whale",
                "Killer Whale",
                "Sperm Whale",
                "4",
                "2",
                "2",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "8",
                "In what year was George Washington born?",
                "1728",
                "1732",
                "1713",
                "1776",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "9",
                "Which of these rooms is in the second floor of the White House?",
                "Red Room",
                "China Room",
                "State Dining Room",
                "East Room",
                "2",
                "2",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "10",
                "Which Pope began his reign in 963?",
                "Innocent III",
                "Leo VIII",
                "Gregory VII",
                "Gregory I",
                "2",
                "1",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "11",
                "What is the second longest river in South America?",
                "Parana River",
                "Xingu River",
                "Amazon River",
                "Rio Orinoco",
                "1",
                "1",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "12",
                "What Ford replaced the Model T?",
                "Model U",
                "Model A",
                "Edsel",
                "Mustang",
                "2",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "13",
                "When was the first picture taken?",
                "1860",
                "1793",
                "1912",
                "1826",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "14",
                "Where were the first Winter Olympics held?",
                "St. Moritz, Switzerland",
                "Stockholm, Sweden",
                "Oslo, Norway",
                "Chamonix, France",
                "4",
                "1",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "15",
                "Which of these is not the name of a New York tunnel?",
                "Brooklyn-Battery",
                "Lincoln",
                "Queens Midtown",
                "Manhattan",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        return list;
    }

}
