package com.qmul.project.challengeme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ResponseChallengeDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View view = new View(getActivity());
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.response_challenge, null))
	    // Add action buttons
	           .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	 
	            	   MySQLiteHelper db = new MySQLiteHelper(getActivity());
	            	   ChallengeLog challengeLog;
	            	   
	            	   int aux = ((ChallengeMeApplication) getActivity()
					             .getApplication())
					             .getHistorySelected();
	            	   
	            	   challengeLog = db.getChallenge(aux);
	            	   System.out.println(challengeLog.getFromUser());
	            	   System.out.println(challengeLog.getToUser());
	            	   System.out.println(challengeLog.getSport());
	            	   System.out.println(challengeLog.getChallenge());
	            	  
	            	 
	            	   
	            	   challengeLog.setWinner("Felipe");
	            	   challengeLog.setScore("10 x 0");
	            	   
	            	   db.updateChallenge(challengeLog);
	            	   
	            	   ResponseChallengeDialog.this.getDialog().dismiss();
	            	  
	            	   Intent intent = new Intent(getActivity(),MainActivity.class);
						getActivity().startActivity(intent);
	            	   
	            	   
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   ResponseChallengeDialog.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	
	
	
	
	
}
