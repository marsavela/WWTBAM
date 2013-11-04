package es.serpat.wwtbam;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class Game extends FragmentActivity {

    //private static final String SHARED_FINISH = "gameShouldFinish";
    //Constants for saved data.
    //private String SHARED_PREF_GAME = "isGameSaved";
    //private String SHARED_PREF_QUESTION_NUMBER = getString(R.string.SHARED_PREF_QUESTION_NUMBER);
    //private String SHARED_PREF_NAME_KEY = "playerName";
    private String SHARED_PREF_AUDIENCE = "isUsedAudienceJoker";
    private String SHARED_PREF_FIFTY_USED = "questionFiftyJokerWereUsed";
    private String SHARED_PREF_FIFTY = "isUsedFiftyJoker";
    private String SHARED_PREF_PHONE = "isUsedPhoneJoker";

    private List<Question> questionList = null;
    private int actualQuestion;

    private boolean isUsedAudienceJoker;
    private boolean isUsedFiftyJoker;
    private int questionFiftyJokerWereUsed;
    private boolean isUsedPhoneJoker;

    private boolean isGameSaved;

    private String playerName;

    private PlayActivity activity;

    private int listLevels[] = {0, 100, 200, 300, 500, 1000, 2000, 4000, 8000,
            16000, 32000, 64000, 125000, 250000, 500000, 1000000};

    public Game(PlayActivity activity) {
        this.activity = activity;

        if (questionList == null)
            try {
                questionList = generateQuestionList();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        isGameSaved = sharedPref.getBoolean(activity.getResources().getString(R.string.SHARED_PREF_GAME), false);
        if (isGameSaved)
            restoreGameData();
        editor.putBoolean(activity.getResources().getString(R.string.SHARED_PREF_GAME), true);
        editor.commit();

    }

    private List<String> getArrayJokers() {
        List<String> list = new ArrayList<String>();

        if (!isUsedPhoneJoker)
            list.add(activity.getResources().getString(R.string.phone));
        if (!isUsedFiftyJoker)
            list.add(activity.getResources().getString(R.string.fifty));
        if (!isUsedAudienceJoker)
            list.add(activity.getResources().getString(R.string.audience));

        return list;
    }

    public String numToLetter(String s) {
        if (s.equals("1"))
            return "A";
        else if (s.equals("2"))
            return "B";
        else if (s.equals("3"))
            return "C";
        else
            return "D";
    }

    /**
     * Test if the given answer is the right answer or not and according to the answer and game level, display a different dialog in the PlayActivity
     *
     * @param answer, the answer chosen by the player
     */
    public void testAnswer(String answer) {
        if (answer.equals(questionList.get(actualQuestion).right)) {
            if (actualQuestion >= questionList.size() - 1) {
                setUnsavedGame();
                saveScore();
                //setShouldFinish();
                activity.questionAnswered("win");
            } else {
                actualQuestion++;
                activity.questionAnswered("right");
            }
        } else {
            setUnsavedGame();
            saveScore();
            //setShouldFinish();
            activity.questionAnswered("wrong");
        }
    }
/*
    public boolean shouldFinish() {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(SHARED_FINISH, false);
    }

    private void setShouldFinish() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putBoolean(SHARED_FINISH, true);
        editor.commit();
    }

    public void setShouldNotFinish() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putBoolean(SHARED_FINISH, false);
        editor.commit();
    }*/

    public void saveScore() {
        int score;
        if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(
                activity.getResources().getString(R.string.SHARED_PREF_GAME), false))
            score = listLevels[actualQuestion];
        else if (actualQuestion > 10)
            score = listLevels[10];
        else score = listLevels[5];

        DAOScores daoScores = new DAOScores(activity);
        daoScores.open();

        daoScores.createScore(PreferenceManager.getDefaultSharedPreferences(activity).
                getString(activity.getResources().getString(R.string.SHARED_PREF_NAME_KEY),
                        activity.getResources().getString(R.string.default_user_name)), score);
        daoScores.close();
    }

    public void askForJoker(String joker) {
        if (joker.equals(activity.getResources().getString(R.string.phone))) {
            isUsedPhoneJoker = true;
            activity.showPhoneCallAnswer(numToLetter(questionList.get(actualQuestion).phone));
        } else if (joker.equals(activity.getResources().getString(R.string.fifty))) {
            isUsedFiftyJoker = true;
            questionFiftyJokerWereUsed = actualQuestion;
            activity.hideButton(numToLetter(questionList.get(actualQuestion).fifty1));
            activity.hideButton(numToLetter(questionList.get(actualQuestion).fifty2));
        } else if (joker.equals(activity.getResources().getString(R.string.audience))) {
            isUsedAudienceJoker = true;
            activity.showAudienceAnswer(numToLetter(questionList.get(actualQuestion).audience));
        }
    }

    public void saveGameData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getResources().getString(R.string.SHARED_PREF_QUESTION_NUMBER), actualQuestion);
        editor.putString(activity.getResources().getString(R.string.SHARED_PREF_NAME_KEY), playerName);
        //editor.putString(SHARED_PREF_NAME_KEY, playerName);
        editor.putBoolean(SHARED_PREF_PHONE, isUsedPhoneJoker);
        editor.putBoolean(SHARED_PREF_FIFTY, isUsedFiftyJoker);
        editor.putInt(SHARED_PREF_FIFTY_USED, questionFiftyJokerWereUsed);
        editor.putBoolean(SHARED_PREF_AUDIENCE, isUsedAudienceJoker);
        editor.commit();
    }

    public void restoreGameData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        actualQuestion = sharedPref.getInt(activity.getResources().getString(R.string.SHARED_PREF_QUESTION_NUMBER), 0);
        playerName = sharedPref.getString(activity.getResources().getString(R.string.SHARED_PREF_NAME_KEY),
                activity.getResources().getString(R.string.default_user_name));
        //playerName = sharedPref.getString(SHARED_PREF_NAME_KEY, activity.getResources().getString(R.string.default_user_name));
        isUsedPhoneJoker = sharedPref.getBoolean(SHARED_PREF_PHONE, false);
        isUsedFiftyJoker = sharedPref.getBoolean(SHARED_PREF_FIFTY, false);
        questionFiftyJokerWereUsed = sharedPref.getInt(SHARED_PREF_FIFTY_USED, -1);
        isUsedAudienceJoker = sharedPref.getBoolean(SHARED_PREF_AUDIENCE, false);
    }

    public String getJokerName(int pos) {
        return getArrayJokers().get(pos);
    }

    public String[] getAllowedJokers() {
        String[] str = new String[getArrayJokers().size()];
        return getArrayJokers().toArray(str);
    }

    public boolean areJokersLeft() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return (3 - getAllowedJokers().length) < Integer.parseInt(sharedPref.getString(activity.getResources().getString(R.string.SHARED_PREF_NUM_JOKERS), "3"));
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public boolean isGameSaved() {
        return isGameSaved;
    }

    public int getActualQuestion() {
        return actualQuestion;
    }

    public int getQuestionFiftyJokerWereUsed() {
        return questionFiftyJokerWereUsed;
    }

    public boolean isUsedFiftyJoker() {
        return isUsedFiftyJoker;
    }

    public void setUnsavedGame() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getResources().getString(R.string.SHARED_PREF_GAME), false);
        editor.commit();
    }

    public List<Question> generateQuestionList() throws XmlPullParserException {
        List<Question> list = new ArrayList<Question>();
        Question q;
        InputStream inputStream = activity.getResources().openRawResource(R.raw.questions0001);

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(inputStream, null);

        int eventType = XmlPullParser.START_DOCUMENT;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals("question")) {
                q = new Question(
                        parser.getAttributeValue(null, "number"),
                        parser.getAttributeValue(null, "text"),
                        parser.getAttributeValue(null, "answer1"),
                        parser.getAttributeValue(null, "answer2"),
                        parser.getAttributeValue(null, "answer3"),
                        parser.getAttributeValue(null, "answer4"),
                        parser.getAttributeValue(null, "right"),
                        parser.getAttributeValue(null, "audience"),
                        parser.getAttributeValue(null, "phone"),
                        parser.getAttributeValue(null, "fifty1"),
                        parser.getAttributeValue(null, "fifty2")
                );
                list.add(q);
            }
            try {
                eventType = parser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}