package com.qmul.project.challengeme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class SelectionFragment extends Fragment {
	private static final String TAG = "SelectionFragment";
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private ListView listView;
	private ListView sportListView;
	private List<BaseListElement> listElements;
	private Button sendChallengeButton;
	private String requestId;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    	    
	    View view = inflater.inflate(R.layout.selection, 
	            container, false);
	    
	 // Find the user's profile picture custom view
	    profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
	    profilePictureView.setCropped(true);

	    // Find the user's name view
	    userNameView = (TextView) view.findViewById(R.id.selection_user_name);
	    
	 // Find the list view
	    listView = (ListView) view.findViewById(R.id.selection_list);
	   //sportListView = (ListView) view.findViewById(R.id.sport_list);
	    
	    
	    
	    // Set up the list view items, based on a list of
	    // BaseListElement items
	    listElements = new ArrayList<BaseListElement>();
	    
	    // Add an item for the friend picker
	    listElements.add(new PeopleListElement(0));
	    // Add an item for the friend picker
	    listElements.add(new SportListElement(0));
	    // Add an item for the friend picker
	    listElements.add(new ChallengeListElement(0));
	    
	    
	    // Set the list view adapter
	    listView.setAdapter(new ActionListAdapter(getActivity(), 
	                        R.id.selection_list, listElements));

	    if (savedInstanceState != null) {
	        // Restore the state for each list element
	        for (BaseListElement listElement : listElements) {
	            listElement.restoreState(savedInstanceState);
	        }   
	    }
	    
	 // Check for an open session
	    Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	        // Get the user's data
	        makeMeRequest(session);
	    }
	    
	    
	    sendChallengeButton = (Button) view.findViewById(R.id.sendRequestButton);
	    sendChallengeButton.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v){
	    		sendRequestDialog();
	    	}
	    });
	    
	    return view;
	}
	
	

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		Uri intentUri = getActivity().getIntent().getData();
		if(intentUri != null){
			String requestIdParam = intentUri.getQueryParameter("request_id");
			if(requestIdParam != null){
				String array[] = requestIdParam.split(",");
				requestId = array[0];
				Log.i(TAG, "Request id: " + requestId);
			}
		}
	}
	
	private void getRequestData(final String inRequestId){
		
		Request request = new Request(Session.getActiveSession(),
							inRequestId, null, HttpMethod.GET, new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				GraphObject graphObject = response.getGraphObject();
				FacebookRequestError error = response.getError();
				boolean processError = false;
				
				String message = "Incoming request";
				if(graphObject != null){
					if(graphObject.getProperty("data") != null){
						try{
							JSONObject dataObject = 
									new JSONObject((String) graphObject.getProperty("data"));
									String sport = dataObject.getString("Sport");
									String challenge = dataObject.getString("Challenge");
									
									JSONObject fromObject = (JSONObject) graphObject.getProperty("from");
									String sender = fromObject.getString("name");
									String title = sender+ " sent you a challenge";
									message = title + "\n\n" +
											"Sport: " + sport + "\n" + "Challenge: " + challenge;
						} catch (JSONException e){
							processError = true;
							message = "Error getting request info";
							
						}
					} else if (error != null){
						processError = true;
						message = "Error getting request info";
					}
				}
				
				Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
				if(!processError){
					//deleteRequest(inRequestId);
				}
			}
			
		});
		Request.executeBatchAsync(request);
	}
	
	private void sendRequestDialog(){
		Bundle params = new Bundle();
		params.putString("message","You've been challenged!");
		params.putString("data", "{\"Sport\":" +
						 ((ChallengeMeApplication) getActivity()
			   		             .getApplication())
			   		             .getSelectedSports() +","+ "\"Challenge\":" + ((ChallengeMeApplication) getActivity()
			   		             .getApplication())
			   		             .getSelectedChallenges() +"}");
		
		WebDialog requestsDialog = (
				new WebDialog.RequestsDialogBuilder(getActivity(),
						Session.getActiveSession(),
						params))
						.setOnCompleteListener(new OnCompleteListener() {
							@Override
							public void onComplete(Bundle values, FacebookException error){
								if(error != null){
									if(error instanceof FacebookOperationCanceledException){
										Toast.makeText(getActivity().getApplicationContext(),
												"Request cancelled",
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(getActivity().getApplicationContext(),
												"Network Error",
												Toast.LENGTH_SHORT).show();
									}
								} else {
									final String requestId = values.getString("request");
									if(requestId != null){
										Toast.makeText(getActivity().getApplicationContext(),
												"Request sent",
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(getActivity().getApplicationContext(),
												"Request Cancelled",
												Toast.LENGTH_SHORT).show();
									}
								}
							}
						}).build();
						requestsDialog.show();
		
		
	}
	
	
	public void deleteRequest(String inRequestId){
		
		Request request = new Request(Session.getActiveSession(), inRequestId, null, HttpMethod.DELETE, new Request.Callback(){
			@Override
			public void onCompleted(Response response){
				Toast.makeText(getActivity().getApplicationContext(), "Request deleted", Toast.LENGTH_SHORT).show();
			}
		});
		Request.executeBatchAsync(request);
	}
	
	
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                    // Set the id for the ProfilePictureView
	                    // view that in turn displays the profile picture.
	                    profilePictureView.setProfileId(user.getId());
	                    // Set the Textview's text to the user's name.
	                    userNameView.setText(user.getName());
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	} 
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	   
		if(state.isOpened() && requestId != null){
			getRequestData(requestId);
		}
		
		if (session != null && session.isOpened()) {
	        // Get the user's data.
	        makeMeRequest(session);
	    }
	}
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(final Session session, final SessionState state, final Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	private static final int REAUTH_ACTIVITY_CODE = 100;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == REAUTH_ACTIVITY_CODE) {
	      uiHelper.onActivityResult(requestCode, resultCode, data);
	    } else if (resultCode == Activity.RESULT_OK && 
	            requestCode >= 0 && requestCode < listElements.size()) {
	        listElements.get(requestCode).onActivityResult(data);
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	    for (BaseListElement listElement : listElements) {
	        listElement.onSaveInstanceState(bundle);
	    }
	    uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	private void startPickerActivity(Uri data, int requestCode) {
	     Intent intent = new Intent();
	     intent.setData(data);
	     intent.setClass(getActivity(), PickerActivity.class);
	     startActivityForResult(intent, requestCode);
	 }
	

	private class ActionListAdapter extends ArrayAdapter<BaseListElement> {
	    private List<BaseListElement> listElements;

	    public ActionListAdapter(Context context, int resourceId, 
	                             List<BaseListElement> listElements) {
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

	        BaseListElement listElement = listElements.get(position);
	        if (listElement != null) {
	            view.setOnClickListener(listElement.getOnClickListener());
	            ImageView icon = (ImageView) view.findViewById(R.id.icon);
	            TextView text1 = (TextView) view.findViewById(R.id.text1);
	            TextView text2 = (TextView) view.findViewById(R.id.text2);
	            if (icon != null) {
	                icon.setImageDrawable(listElement.getIcon());
	            }
	            if (text1 != null) {
	                text1.setText(listElement.getText1());
	            }
	            if (text2 != null) {
	                text2.setText(listElement.getText2());
	            }
	        }
	        return view;
	    }

	}
	
	private class PeopleListElement extends BaseListElement {
		
		private List<GraphUser> selectedUsers;
		private static final String FRIENDS_KEY = "friends";
		
		private List<GraphUser> restoreByteArray(byte[] bytes) {
		    try {
		        @SuppressWarnings("unchecked")
		        List<String> usersAsString =
		                (List<String>) (new ObjectInputStream
		                                (new ByteArrayInputStream(bytes)))
		                                .readObject();
		        if (usersAsString != null) {
		            List<GraphUser> users = new ArrayList<GraphUser>
		            (usersAsString.size());
		            for (String user : usersAsString) {
		                GraphUser graphUser = GraphObject.Factory
		                .create(new JSONObject(user), 
		                                GraphUser.class);
		                users.add(graphUser);
		            }   
		            return users;
		        }   
		    } catch (ClassNotFoundException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    } catch (IOException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    } catch (JSONException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    }   
		    return null;
		}
		
		@Override
		protected boolean restoreState(Bundle savedState) {
		    byte[] bytes = savedState.getByteArray(FRIENDS_KEY);
		    if (bytes != null) {
		        selectedUsers = restoreByteArray(bytes);
		        setUsersText();
		        return true;
		    }   
		    return false;
		} 
		
		
		
		@Override
		protected void onSaveInstanceState(Bundle bundle) {
		    if (selectedUsers != null) {
		        bundle.putByteArray(FRIENDS_KEY,
		                getByteArray(selectedUsers));
		    }   
		} 
		
		private void setUsersText() {
		    String text = null;
		    if (selectedUsers != null) {
		            // If there is one friend
		        if (selectedUsers.size() == 1) {
		            text = String.format(getResources()
		                    .getString(R.string.single_user_selected),
		                    selectedUsers.get(0).getName());
		        } else if (selectedUsers.size() == 2) {
		            // If there are two friends 
		            text = String.format(getResources()
		                    .getString(R.string.two_users_selected),
		                    selectedUsers.get(0).getName(), 
		                    selectedUsers.get(1).getName());
		        } else if (selectedUsers.size() > 2) {
		            // If there are more than two friends 
		            text = String.format(getResources()
		                    .getString(R.string.multiple_users_selected),
		                    selectedUsers.get(0).getName(), 
		                    (selectedUsers.size() - 1));
		        }   
		    }   
		    if (text == null) {
		        // If no text, use the placeholder text
		        text = getResources()
		        .getString(R.string.action_people_default);
		    }   
		    // Set the text in list element. This will notify the 
		    // adapter that the data has changed to
		    // refresh the list view.
		    setText2(text);
		} 
		
		@Override
		protected void onActivityResult(Intent data) {
		    selectedUsers = ((ChallengeMeApplication) getActivity()
		             .getApplication())
		             .getSelectedUsers();
		    setUsersText();
		    notifyDataChanged();
		}

	    public PeopleListElement(int requestCode) {
	        super(getActivity().getResources().getDrawable(R.drawable.add_friends),
	              getActivity().getResources().getString(R.string.action_people),
	              getActivity().getResources().getString(R.string.action_people_default),
	              requestCode);
	    }
	    
	    
	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	startPickerActivity(PickerActivity.FRIEND_PICKER, getRequestCode());
	            }
	        };
	    }
	}
	
	private byte[] getByteArray(List<GraphUser> users) {
	    // convert the list of GraphUsers to a list of String 
	    // where each element is the JSON representation of the 
	    // GraphUser so it can be stored in a Bundle
	    List<String> usersAsString = new ArrayList<String>(users.size());

	    for (GraphUser user : users) {
	        usersAsString.add(user.getInnerJSONObject().toString());
	    }   
	    try {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        new ObjectOutputStream(outputStream).writeObject(usersAsString);
	        return outputStream.toByteArray();
	    } catch (IOException e) {
	        Log.e(TAG, "Unable to serialize users.", e); 
	    }   
	    return null;
	}   
	
	
	private class SportListElement extends BaseListElement{
		
		DialogFragment sportListDialog = new SportListDialog();
		private List<String> selectedSport;
		private static final String SPORT_KEY = "sport";
		
		public SportListElement(int requestCode) {
	        super(getActivity().getResources()
	              .getDrawable(R.drawable.add_location),
	              getActivity().getResources()
	              .getString(R.string.action_sport),
	              getActivity().getResources()
	              .getString(R.string.action_sport_default),
	              requestCode);
	        
	    }
		
		private byte[] getByteArray(List<String> sports) {
		    
		    List<String> sportAsString = new ArrayList<String>(sports.size());

		    for (String sport : sports) {
		        sportAsString.add(sport.toString());
		    }   
		    try {
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        new ObjectOutputStream(outputStream).writeObject(sportAsString);
		        return outputStream.toByteArray();
		    } catch (IOException e) {
		        Log.e(TAG, "Unable to serialize users.", e); 
		    }   
		    return null;
		}
		
		
		
		
		@Override
		protected void onSaveInstanceState(Bundle bundle) {
		    if (selectedSport != null) {
		        bundle.putByteArray(SPORT_KEY,
		                getByteArray(selectedSport));
		    }   
		} 
		
		private List<String> restoreByteArray(byte[] bytes) {
		    try {
		    	@SuppressWarnings("unchecked")
				List<String> sportAsString =
		    			(List<String>) (new ObjectInputStream
                                (new ByteArrayInputStream(bytes)))
                                .readObject();
		    	if(sportAsString != null){
		    		 List<String> sports = new ArrayList<String>
		             (sportAsString.size());
		             for (String sport : sportAsString) {
		                 String sportString = sport;
		                 sports.add(sportString);
		             }   
		             return sports;
		    	}
		    }  catch (ClassNotFoundException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    } catch (IOException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    }
		    
		    return null;
		}
		
		
		
		@Override
		protected boolean restoreState(Bundle savedState) {
		    byte[] bytes = savedState.getByteArray(SPORT_KEY);
		    if (bytes != null) {
		        selectedSport = restoreByteArray(bytes);
		        setSportText();
		        return true;
		    }   
		    return false;
		} 
		
		private void setSportText(){
			String text = null;
			if(selectedSport != null){
				if(selectedSport.size() == 1){
					text = String.format(getResources()
		                    .getString(R.string.single_user_selected),
		                    selectedSport.get(0));
				}
			}
			
			if(text == null){
				text = getResources()
				        .getString(R.string.action_sport_default);
			}
			
			setText2(text);
		}
		
		@Override
		protected void onActivityResult(Intent data) {
		    selectedSport = ((ChallengeMeApplication) getActivity()
		             .getApplication())
		             .getSelectedSports();
		    setSportText();
		    notifyDataChanged();
		}
		
	    @Override
	    protected View.OnClickListener getOnClickListener() {
	    	
	    	return new View.OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {
	        		selectedSport = ((ChallengeMeApplication) getActivity()
		   		             .getApplication())
		   		             .getSelectedSports();
		   		    setSportText();
		   		    notifyDataChanged();
	        		sportListDialog.show(getFragmentManager(), "SportListDialog");      		
	        	}
	        };
	    }
	}
	
private class ChallengeListElement extends BaseListElement{
	DialogFragment challengeListDialog = new ChallengeListDialog();
	private List<String> selectedChallenge;
	private static final String CHALLENGE_KEY = "challenge";
	
		public ChallengeListElement(int requestCode) {
	        super(getActivity().getResources()
	              .getDrawable(R.drawable.add_photo),
	              getActivity().getResources()
	              .getString(R.string.action_challenge),
	              getActivity().getResources()
	              .getString(R.string.action_challenge_default),
	              requestCode);
	    }

		private byte[] getByteArray(List<String> challenges) {
		    
		    List<String> challengeAsString = new ArrayList<String>(challenges.size());

		    for (String challenge : challenges) {
		    	challengeAsString.add(challenge.toString());
		    }   
		    try {
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        new ObjectOutputStream(outputStream).writeObject(challengeAsString);
		        return outputStream.toByteArray();
		    } catch (IOException e) {
		        Log.e(TAG, "Unable to serialize users.", e); 
		    }   
		    return null;
		}
		
		
		
		
		@Override
		protected void onSaveInstanceState(Bundle bundle) {
		    if (selectedChallenge != null) {
		        bundle.putByteArray(CHALLENGE_KEY,
		                getByteArray(selectedChallenge));
		    }   
		} 
		
		private List<String> restoreByteArray(byte[] bytes) {
		    try {
		    	@SuppressWarnings("unchecked")
				List<String> challengeAsString =
		    			(List<String>) (new ObjectInputStream
                                (new ByteArrayInputStream(bytes)))
                                .readObject();
		    	if(challengeAsString != null){
		    		 List<String> challenges = new ArrayList<String>
		             (challengeAsString.size());
		             for (String challenge : challengeAsString) {
		                 String challengeString = challenge;
		                 challenges.add(challengeString);
		             }   
		             return challenges;
		    	}
		    }  catch (ClassNotFoundException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    } catch (IOException e) {
		        Log.e(TAG, "Unable to deserialize users.", e); 
		    }
		    
		    return null;
		}
		
		
		
		@Override
		protected boolean restoreState(Bundle savedState) {
		    byte[] bytes = savedState.getByteArray(CHALLENGE_KEY);
		    if (bytes != null) {
		        selectedChallenge = restoreByteArray(bytes);
		        setChallengeText();
		        return true;
		    }   
		    return false;
		} 
		
		private void setChallengeText(){
			String text = null;
			if(selectedChallenge != null){
				if(selectedChallenge.size() == 1){
					text = String.format(getResources()
		                    .getString(R.string.single_user_selected),
		                    selectedChallenge.get(0));
				}
			}
			
			if(text == null){
				text = getResources()
				        .getString(R.string.action_challenge_default);
			}
			
			setText2(text);
		}
		
		@Override
		protected void onActivityResult(Intent data) {
		    selectedChallenge = ((ChallengeMeApplication) getActivity()
		             .getApplication())
		             .getSelectedChallenges();
		    setChallengeText();
		    notifyDataChanged();
		}
		

	    @Override
	    protected View.OnClickListener getOnClickListener() {
	        return new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	selectedChallenge = ((ChallengeMeApplication) getActivity()
		   		             .getApplication())
		   		             .getSelectedChallenges();
		   		    setChallengeText();
		   		    notifyDataChanged();
	            	challengeListDialog.show(getFragmentManager(), "ChallengeListDialog");
	            }
	        };
	    }
	}
	

	
}
