package es.serpat.wwtbam;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class Question {

    String number = null;
    String text = null;
    String answer1 = null;
    String answer2 = null;
    String answer3 = null;
    String answer4 = null;
    String right = null;
    String audience = null;
    String phone = null;
    String fifty1 = null;
    String fifty2 = null;

    public Question() {
    }

    public Question(String number, String text, String answer1, String answer2, String answer3, String answer4, String right, String audience, String phone, String fifty1, String fifty2) {
        this.number = number;
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.right = right;
        this.audience = audience;
        this.phone = phone;
        this.fifty1 = fifty1;
        this.fifty2 = fifty2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}