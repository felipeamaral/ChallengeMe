package com.qmul.project.challengeme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChallengeButtonErrorDialog extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(R.string.choose_challenge_error)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface dialog, int id) {
	        	   ChallengeButtonErrorDialog.this.getDialog().cancel();
	           }
	       });
		return builder.create();
	}
}
