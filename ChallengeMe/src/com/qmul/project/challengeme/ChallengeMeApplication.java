package com.qmul.project.challengeme;

import java.util.List;

import android.app.Application;

import com.facebook.model.GraphUser;

public class ChallengeMeApplication extends Application {
	private List<GraphUser> selectedUsers;
	private List<String> selectedSports;
	private List<String> selectedChallenges;
	private List<String> selectedSportsRequested;
	private List<String> selectedChallengesRequested;
	private List<String> fromUser;
	private List<String> toUser;
	private boolean isSportSelected;
	private boolean isChallengeSelected;
	private int historySelected;
	
	
	
	
	
	

	public int getHistorySelected() {
		return historySelected;
	}

	public void setHistorySelected(int historySelected) {
		this.historySelected = historySelected;
	}

	public boolean isSportSelected() {
		return isSportSelected;
	}

	public void setIsSportSelected(boolean isSportSelected) {
		this.isSportSelected = isSportSelected;
	}

	public boolean isChallengeSelected() {
		return isChallengeSelected;
	}

	public void setIsChallengeSelected(boolean isChallengeSelected) {
		this.isChallengeSelected = isChallengeSelected;
	}

	public List<String> getFromUser() {
		return fromUser;
	}

	public void setFromUser(List<String> fromUser) {
		this.fromUser = fromUser;
	}

	public List<String> getToUser() {
		return toUser;
	}

	public void setToUser(List<String> toUser) {
		this.toUser = toUser;
	}

	public List<String> getSelectedSportsRequested() {
		return selectedSportsRequested;
	}

	public void setSelectedSportsRequested(List<String> selectedSportsRequested) {
		this.selectedSportsRequested = selectedSportsRequested;
	}

	public List<String> getSelectedChallengesRequested() {
		return selectedChallengesRequested;
	}

	public void setSelectedChallengesRequested(
			List<String> selectedChallengesRequested) {
		this.selectedChallengesRequested = selectedChallengesRequested;
	}

	public List<GraphUser> getSelectedUsers() {
	    return selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> users) {
	    selectedUsers = users;
	}

	public List<String> getSelectedSports() {
		return selectedSports;
	}

	public void setSelectedSports(List<String> selectedSports) {
		this.selectedSports = selectedSports;
	}

	public List<String> getSelectedChallenges() {
		return selectedChallenges;
	}

	public void setSelectedChallenges(List<String> selectedChallenges) {
		this.selectedChallenges = selectedChallenges;
	}
	
	
	
}
