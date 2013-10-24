package es.serpat.wwtbam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergiuDaniel on 24/10/13.
 */
public class DAOScores {

    // Database fields
    private SQLiteDatabase database;
    private SQLHelper dbHelper;
    private String[] allColumns = { SQLHelper.COLUMN_ID,
            SQLHelper.COLUMN_NAME, SQLHelper.COLUMN_SCORE };

    public DAOScores(Context context) {
        dbHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Score createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.COLUMN_COMMENT, comment);
        long insertId = database.insert(SQLHelper.TABLE_SCORES, null,
                values);
        Cursor cursor = database.query(SQLHelper.TABLE_SCORES,
                allColumns, SQLHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Score newScore = cursorToScore(cursor);
        return newScore;
    }

    public void deleteComment(Score comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLHelper.TABLE_SCORES, SQLHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Score> getAllComments() {
        List<Score> scoreList = new ArrayList<Score>();

        Cursor cursor = database.query(SQLHelper.TABLE_SCORES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Score score = cursorToScore(cursor);
            scoreList.add(score);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return scoreList;
    }

    private Score cursorToScore(Cursor cursor) {
        Score score = new Score();
        score.setId(cursor.getInt(0));
        score.setName(cursor.getString(1));
        score.setScore(cursor.getInt(2));
        return score;
    }
}