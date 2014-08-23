package com.qmul.project.challengeme;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ChallengeMeDB";
	
	private static final String TABLE_CHALLENGES = "challenges";
	private static final String KEY_ID = "challengeId";
	private static final String KEY_FROMUSER ="fromUser";
	private static final String KEY_TOUSER = "toUser";
	private static final String KEY_SPORT = "sport";
	private static final String KEY_CHALLENGE = "challenge";
	private static final String KEY_WINNER = "winner";
	private static final String KEY_SCORE = "score";
	
	
	
	private static final String[] COLUMNS = {KEY_ID, KEY_FROMUSER, KEY_TOUSER, KEY_SPORT, KEY_CHALLENGE, KEY_WINNER,KEY_SCORE };
	
	public MySQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CHALLENGE_TABLE = "CREATE TABLE challenges (" +
				"challengeId INTEGER PRIMARY KEY AUTOINCREMENT," +
				"fromUser TEXT, "+
				"toUser TEXT, " +
				"sport TEXT, "+
				"challenge TEXT, " +
				"winner TEXT, " +
				"score TEXT)";
		db.execSQL(CREATE_CHALLENGE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS challenges");
		this.onCreate(db);
	}

	
	public void addChallenge(ChallengeLog challengeLog){
		Log.d("addChallenge", challengeLog.toString());
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_FROMUSER, challengeLog.getFromUser());
		values.put(KEY_TOUSER, challengeLog.getToUser());
		values.put(KEY_SPORT, challengeLog.getSport());
		values.put(KEY_CHALLENGE, challengeLog.getChallenge());
		values.put(KEY_WINNER, challengeLog.getWinner());
		values.put(KEY_SCORE, challengeLog.getScore());
		
		
		db.insert(TABLE_CHALLENGES, null, values);
		
		db.close();
	}
	
	public ChallengeLog getChallenge(int id){
		SQLiteDatabase db = this.getReadableDatabase();
	
		Cursor cursor =
				db.query(TABLE_CHALLENGES,
						COLUMNS,
						" challengeId = ?",
						new String[] {String.valueOf(id)},
						null,
						null,
						null,
						null
						);
		
		if(cursor != null){
			cursor.moveToFirst();
		}
			
		ChallengeLog challengeLog = new ChallengeLog();
		challengeLog.setChallengeId(Integer.parseInt(cursor.getString(0)));
		challengeLog.setFromUser(cursor.getString(1));
		challengeLog.setToUser(cursor.getString(2));
		challengeLog.setSport(cursor.getString(3));
		challengeLog.setChallenge(cursor.getString(4));
		challengeLog.setWinner(cursor.getString(5));
		challengeLog.setScore(cursor.getString(6));
		Log.d("getChallenge("+id+")", challengeLog.toString());
		
		return challengeLog;

	}
	
	public List<ChallengeLog> getAllChallenges(){
		List<ChallengeLog> challenges = new LinkedList<ChallengeLog>();
		
		String query = "SELECT * FROM " + TABLE_CHALLENGES;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		ChallengeLog challengeLog = null;
		if(cursor.moveToFirst()){
			do{
				challengeLog = new ChallengeLog();
				challengeLog.setChallengeId(Integer.parseInt(cursor.getString(0)));
				challengeLog.setFromUser(cursor.getString(1));
				challengeLog.setToUser(cursor.getString(2));
				challengeLog.setSport(cursor.getString(3));
				challengeLog.setChallenge(cursor.getString(4));
				challengeLog.setWinner(cursor.getString(5));
				challengeLog.setScore(cursor.getString(6));
				
				challenges.add(challengeLog);
				
			} while(cursor.moveToNext());
		}
		
		Log.d("getAllChallenges()", challengeLog.toString());
		
		return challenges;
	}
	
	public int updateChallenge(ChallengeLog challengeLog){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("fromUser", challengeLog.getFromUser());
		values.put("toUser", challengeLog.getToUser());
		values.put("sport", challengeLog.getSport());
		values.put("challenge", challengeLog.getChallenge());
		values.put("winner", challengeLog.getWinner());
		values.put("score", challengeLog.getChallenge());
		
		int i = db.update(TABLE_CHALLENGES,
				values, KEY_ID+" = ?",
				new String[] {String.valueOf(challengeLog.getChallengeId())});
		
		db.close();
		return i;
	}
	
	public int countRow(){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT COUNT(*)FROM " + TABLE_CHALLENGES;
		Cursor cursor = db.rawQuery(query, null);
		
		cursor.moveToFirst();
		
		int i = Integer.parseInt(cursor.getString(0));
		
		return i;
		
	}
	
	public void deleteChallenge(ChallengeLog challengeLog){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_CHALLENGES, KEY_ID+" = ?", 
				new String[] {String.valueOf(challengeLog.getChallengeId())});
		
		db.close();
		
		Log.d("deleteChallenge", challengeLog.toString());
	}
	
	
}
