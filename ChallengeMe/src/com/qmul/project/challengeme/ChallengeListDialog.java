package com.qmul.project.challengeme;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChallengeListDialog extends DialogFragment {
	String[] football = new String[]{"Full Match", "2 goals", "5 minutes"};
	String[] tennis = new String[]{"3 games","1 set", "3 sets", "5 sets"};
	private List<String> selectedChallenge;
	private List<String> selectedSports;
	private List<String> aux;
	public boolean isSelected;
	public boolean isChallengeSelected = false;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		selectedSports = ((ChallengeMeApplication) getActivity()
	             .getApplication())
	             .getSelectedSports();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		
		isSelected = ((ChallengeMeApplication) getActivity()
	             .getApplication())
	             .isSportSelected();
		
		if(isSelected == true){
			if(selectedSports.get(0) == "Football"){
				selectedChallenge = new ArrayList<String>();
				
				builder.setTitle(R.string.action_challenge)
				.setItems(football, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						selectedChallenge = new ArrayList<String>();
						selectedChallenge.add(football[which]);
						System.out.println(selectedChallenge.get(0));
						
						((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .setSelectedChallenges(selectedChallenge);
						
						aux = ((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .getSelectedChallenges();
						
						if(!aux.isEmpty()){

							isSelected = true;
							
						}

						((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .setIsChallengeSelected(isSelected);
					
					}
				});
			} else if(selectedSports.get(0) == "Tennis") {
				selectedChallenge = new ArrayList<String>();
				
				builder.setTitle(R.string.action_challenge)
				.setItems(tennis, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						selectedChallenge = new ArrayList<String>();
						selectedChallenge.add(tennis[which]);
						System.out.println(selectedChallenge.get(0));
						
						((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .setSelectedChallenges(selectedChallenge);
						
						aux = ((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .getSelectedChallenges();
						
						if(!aux.isEmpty()){

							isSelected = true;
							
						}

						((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .setIsChallengeSelected(isSelected);
						
					
					}
				});
				
			}
			
		} else {
			builder.setTitle(R.string.choose_sport_error)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   ChallengeListDialog.this.getDialog().cancel();
		               }
		           });
		}
		
		 
		
		return builder.create();
	}
	
	
}
