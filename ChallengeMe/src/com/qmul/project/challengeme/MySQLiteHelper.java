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
	private static final String KEY_LOGCOUNTER = "logcounter";
	
	
	private static final String[] COLUMNS = {KEY_ID, KEY_FROMUSER, KEY_TOUSER, KEY_SPORT, KEY_CHALLENGE, KEY_LOGCOUNTER };
	
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
				"logcounter INTEGER)";
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
		values.put(KEY_CHALLENGE, challengeLog.getChallenge());
		values.put(KEY_LOGCOUNTER, challengeLog.getLogCounter());
		
		
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
		challengeLog.setLogCounter(Integer.parseInt(cursor.getString(5)));
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
				challengeLog.setLogCounter(Integer.parseInt(cursor.getString(5)));
				
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
		
		int i = db.update(TABLE_CHALLENGES,
				values, KEY_ID+" = ?",
				new String[] {String.valueOf(challengeLog.getChallengeId())});
		
		db.close();
		return i;
	}
	
	public int countRow(){
		
		String query = "SELECT COUNT(*)FROM " + TABLE_CHALLENGES;
		
		
		
		return 0;
	}
	
	public void deleteChallenge(ChallengeLog challengeLog){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_CHALLENGES, KEY_ID+" = ?", 
				new String[] {String.valueOf(challengeLog.getChallengeId())});
		
		db.close();
		
		Log.d("deleteChallenge", challengeLog.toString());
	}
	
	
}
