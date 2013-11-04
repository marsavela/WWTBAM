package es.serpat.wwtbam;

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
    private String[] allColumns = {SQLHelper.COLUMN_ID,
            SQLHelper.COLUMN_NAME, SQLHelper.COLUMN_SCORE};

    public DAOScores(Context context) {
        dbHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createScore(String name, int score) {
        database.beginTransaction();
        database.execSQL("INSERT INTO " + SQLHelper.TABLE_SCORES + " (name, score) VALUES ('" +
                name + "', " + score + ");");
        database.setTransactionSuccessful();
        database.endTransaction();
        /*ContentValues values = new ContentValues();
        values.put(SQLHelper.COLUMN_NAME, name);
        values.put(SQLHelper.COLUMN_SCORE, score);
        long insertId = database.insert(SQLHelper.TABLE_SCORES, null,
                //values);
        Cursor cursor = database.rawQuery("SELECT _id, name, score FROM " + SQLHelper.TABLE_SCORES,null);
        Cursor cursor = database.query(SQLHelper.TABLE_SCORES,
                //allColumns, SQLHelper.COLUMN_ID + " = " + insertId, null,
                //null, null, null);
        cursor.moveToFirst();
        Score newScore = cursorToScore(cursor);
        return newScore;*/
    }

    public void deleteScore(Score score) {
        long id = score.getId();
        System.out.println("Score deleted with id: " + id);
        database.delete(SQLHelper.TABLE_SCORES, SQLHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Score> getAllScores() {
        ArrayList<Score> scoreList = new ArrayList<Score>();

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

    public void deleteDB(Context context) {
        dbHelper.deleteDB(context);
    }
}