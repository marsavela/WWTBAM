package es.serpat.wwtbam;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergiuDaniel on 19/10/13.
 */
public class QuestionsXMLParser {
    private String ns = null;

    // We don't use namespaces

    public List<Question> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Question> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Question> questions = new ArrayList<Question>();

        parser.require(XmlPullParser.START_TAG, ns, "quizz");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("question")) {
                questions.add(readQuestion(parser));
            } /*else {
                skip(parser);
            }*/
        }
        return questions;
    }

    private Question readQuestion(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "question");

        Question q = new Question();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            q.number = parser.getAttributeValue(null,"number");
            q.text = parser.getAttributeValue(null,"text");
            q.answer1 = parser.getAttributeValue(null,"answer1");
            q.answer2 = parser.getAttributeValue(null,"answer2");
            q.answer3 = parser.getAttributeValue(null,"answer3");
            q.answer4 = parser.getAttributeValue(null,"answer4");
            q.right = parser.getAttributeValue(null,"right");
            q.audience = parser.getAttributeValue(null,"audience");
            q.phone = parser.getAttributeValue(null,"phone");
            q.fifty1 = parser.getAttributeValue(null,"fifty1");
            q.fifty2 = parser.getAttributeValue(null,"fifty2");

        }
        return q;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
