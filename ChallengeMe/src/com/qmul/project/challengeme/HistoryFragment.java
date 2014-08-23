package com.qmul.project.challengeme;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

public class HistoryFragment extends Fragment {
	
	private static final String TAG = "SelectionFragment";
	private ListView listView;
	private List<ChallengeHistoryElement> listElements;
	DialogFragment responseDialog = new ResponseChallengeDialog();
	ChallengeLog challengeLog = new ChallengeLog();
	int idChallenge = 0;
	private ProfilePictureView profilePictureView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.history, container, false);
	    
	 // Find the list view
	    listView = (ListView) view.findViewById(R.id.selection_list);

	    // Set up the list view items, based on a list of
	    // BaseListElement items
	    listElements = new ArrayList<ChallengeHistoryElement>();
	    // Add an item for the friend picker
	    MySQLiteHelper db = new MySQLiteHelper(getActivity());
	    ChallengeLog ch = new ChallengeLog();
	    
	    int numberOfChallenges = db.countRow();
	    
	    if(numberOfChallenges == 0){
	    	listElements.add(new ChallengeElement(0, null ,"No Challenges in history",null, null, null, null));
		    
	    } else {
	    	List<ChallengeLog> list = db.getAllChallenges();
	    	listElements.clear();
	    	for(int i = 0; i <10; i++){
    			System.out.println("*");
    		}
	    	for(int j = 0; j < list.size(); j++){
	    		
	    		ChallengeLog aux = list.get(j);
	    		String sport = aux.getSport();
	    		String challenge = aux.getChallenge();
	    		String fromUser = aux.getFromUser();
	    		String toUser = aux.getToUser();
	    		String winner = aux.getWinner();
	    		String score = aux.getScore();
	    		
	    		if(winner == null && score == null){
	    			winner = "Pending...";
	    			score = "";		
	    		} else if(winner != null && score != null) {
	    			winner = "Winner: " + aux.getWinner().concat(" -- > ");
	    		}
	    		
	    		int id = aux.getChallengeId();
	    		
	    		System.out.println(id +" "+ sport +" "+challenge );
	    		
	    		listElements.add(new ChallengeElement(j, fromUser, toUser,sport, challenge, winner, score));
	    		

	    	}
	
	    }
	    
	    // Set the list view adapter
	    listView.setAdapter(new ActionListAdapter(getActivity(), 
	                        R.id.selection_list, listElements));    
	    return view;
	}
	
	private class ActionListAdapter extends ArrayAdapter<ChallengeHistoryElement> {
	    private List<ChallengeHistoryElement> listElements;

	    public ActionListAdapter(Context context, int resourceId, 
	                             List<ChallengeHistoryElement> listElements) {
	        super(context, resourceId, listElements);
	        this.listElements = listElements;
	        // Set up as an observer for list item changes to
	        // refresh the view.
	        for (int i = 0; i < listElements.size(); i++) {
	            listElements.get(i).setAdapter(this);
	        }
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater =
	                    (LayoutInflater) getActivity()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.listitem, null);
	        }

	        ChallengeHistoryElement listElement = listElements.get(position);
	        if (listElement != null) {
	            view.setOnClickListener(listElement.getOnClickListener());
	            
	            ImageView icon = (ImageView) view.findViewById(R.id.icon);
	            TextView text1 = (TextView) view.findViewById(R.id.text1);
	            TextView text2 = (TextView) view.findViewById(R.id.text2);
	            TextView text3 = (TextView) view.findViewById(R.id.text3);
	            if (icon != null) {
	                icon.setImageDrawable(listElement.getIcon());
	            }
	            if (text1 != null) {
	                text1.setText(listElement.getText1());
	            }
	            if (text2 != null) {
	                text2.setText(listElement.getText2());
	            }
	            if (text3 != null){
	            	text3.setText(listElement.getText3());
	            }
	        }
	        return view;
	    }
	}
	private class ChallengeElement extends ChallengeHistoryElement {
		
	    public ChallengeElement(int requestCode, String fromUser, String toUser,String sport, String challenge, String winner, String score) {    	
	    	super(getActivity().getResources().getDrawable(R.drawable.add_friends),
	    			fromUser.concat(" vs ") + toUser,
	              sport.concat(" --> ") + challenge,
	              winner + score,
	              requestCode);
	    }

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	MySQLiteHelper db = new MySQLiteHelper(getActivity());
	            	int i = db.countRow();
	            	if(i != 0){
	            		responseDialog.show(getFragmentManager(), "ResponseChallengeDialog");	            		
	            	}
	            }
	        };
	    }
	}
}
