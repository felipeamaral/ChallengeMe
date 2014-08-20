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
