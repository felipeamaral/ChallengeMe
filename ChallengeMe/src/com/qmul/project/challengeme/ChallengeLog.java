package com.qmul.project.challengeme;

public class ChallengeLog {
	
	private int challengeId;
	private String fromUser;
	private String toUser;
	private String sport;
	private String challenge;
	private int logCounter;
	
	public ChallengeLog(){}
	
	public ChallengeLog(String fromUser, String toUser, String sport, String challenge, int logCounter){
		super();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.sport = sport;
		this.challenge = challenge;
		this.logCounter = logCounter;
	}
	
	
	
	public int getLogCounter() {
		return logCounter;
	}

	public void setLogCounter(int logCounter) {
		this.logCounter = logCounter;
	}

	public int getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
	
	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	@Override
	public String toString(){
		return "Challenge [id = " + challengeId +", from: " + fromUser +", toUser: " + toUser +", Sport: " + sport +", Challenge: "+ challenge + "logCounter = "+ logCounter+"]";
	}
	

}
