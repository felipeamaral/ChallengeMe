package com.qmul.project.challengeme;

import java.util.ArrayList;
import java.util.List;

import com.facebook.model.GraphUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SportListDialog extends DialogFragment {

	String[] sports = new String[]{"Football", "Tennis"};
	private List<String> selectedSport;
	private List<String> aux;
	private List<GraphUser> selectedFriend;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		selectedFriend = ((ChallengeMeApplication) getActivity()
	             .getApplication())
	             .getSelectedUsers();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		

		
		builder.setTitle(R.string.action_sport)
					.setItems(sports, new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which){
							selectedSport = new ArrayList<String>();
							selectedSport.add(sports[which]);
							System.out.println(selectedSport.get(0));
							
							((ChallengeMeApplication) getActivity()
						             .getApplication())
						             .setSelectedSports(selectedSport);
							
						}
		});
				
		return builder.create();
	}
	
}
