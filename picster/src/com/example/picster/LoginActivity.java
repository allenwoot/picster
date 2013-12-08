package com.example.picster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.picster.Models.PicUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFacebookUtils.Permissions;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class LoginActivity extends Activity {

    private Button loginButton;
    private Dialog progressDialog;
    private static String id, name, email;
    private static Map<String, Object> friends = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        setupLoginButton();
        
        // Check if user is logged in and linked to facebook
        if (PicsterApplication.currentUser != null) {
        	startHomeActivity();
        }
    }
        
    // Adds onClick listener for login button
    private void setupLoginButton() {
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    // Gets run when user clicks log in button
    private void onLoginButtonClicked() {
        LoginActivity.this.progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList(Permissions.User.ABOUT_ME, Permissions.User.EMAIL);
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, ParseException err) {
                if (parseUser == null) {
                        Log.d(PicsterApplication.TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (parseUser.isNew()) {
                	Request.executeMyFriendsRequestAsync(Session.getActiveSession(), new Request.GraphUserListCallback() {
						
						@SuppressWarnings({ "unchecked" })
						@Override
						public void onCompleted(List<GraphUser> facebookFriends, Response response) {
                        	// Set friends to intersection of current user's facebook friends and parse users
							try {
								// Hackathon, doesn't matter
								@SuppressWarnings({ "rawtypes", "deprecation" })
								ParseQuery friendsQuery = ParseQuery.getUserQuery();
								List<String>friendIds = new ArrayList<String>();
								for (GraphUser friend : facebookFriends){
									friendIds.add(friend.getId());
								}
								friendsQuery.whereContainedIn("username", friendIds);
								List<ParseObject> queryResults = friendsQuery.find();
								for (ParseObject result : queryResults) {
									LoginActivity.friends.put(result.getString("username"), "");
									HashMap<String, Object> friendsFriendsHash = (HashMap<String, Object>) result.getMap("friends");
									// Log in as friend, modify, log out, log back in as current user
									ParseUser.logIn(result.getString("username"), PicUser.defaultPassword);
									friendsFriendsHash.put(parseUser.getUsername(), "");
									result.put("friends", friendsFriendsHash);
									result.save();
								}
								ParseUser.logIn(parseUser.getUsername(), PicUser.defaultPassword);
								parseUser.put("friends", friends);
								parseUser.save();								
							} catch (ParseException e){
								Log.e(PicsterApplication.TAG, "Error: " + e.toString());
							}
							
							Log.d(PicsterApplication.TAG, "User friends synced with Parse!");

							PicsterApplication.currentUser.setFriends((HashMap<String, Object>)friends);
							LoginActivity.this.progressDialog.dismiss();
                            startHomeActivity();
						}
					});
                    Request.executeMeRequestAsync(Session.getActiveSession(), new Request.GraphUserCallback() {
                        @Override
	                    public void onCompleted(GraphUser graphUser, Response response) {
	                        if (graphUser != null) {	                        	
	                            // Get the parse user info
	                        	LoginActivity.id = graphUser.getId();
	                        	LoginActivity.name = graphUser.getName();
	                        	LoginActivity.email = (String) graphUser.getProperty("email");
	                        	
	                            if (!ParseFacebookUtils.isLinked(parseUser)) {
	                                ParseFacebookUtils.link(parseUser, LoginActivity.this, new SaveCallback() {
	                                    @Override
	                                    public void done(ParseException ex) {
	                                        if (ParseFacebookUtils.isLinked(parseUser)) {
	                                            Log.d(PicsterApplication.TAG, "Woohoo, user logged in with Facebook!");
	                                        }
	                                    }
	                                });
	                            }                    
	                            try {
	                            	parseUser.setPassword(PicUser.defaultPassword);
	                                parseUser.setEmail(email);
	                                parseUser.put("name", name); 
	                                parseUser.setUsername(id);
	        						parseUser.save();
	                            } catch (ParseException e) {
	        						Log.e(PicsterApplication.TAG, "Parse Error: " + e.toString());
	        						e.printStackTrace();
	        					}
	                            
	                            PicsterApplication.currentUser = new PicUser(LoginActivity.id, LoginActivity.name, parseUser);
	                            
	                            Log.d(PicsterApplication.TAG, "User signed up and logged in through Facebook!");
	                        } else {
                                Log.e(PicsterApplication.TAG, "Error: Unable to fetch graph user object for current user");
                                throw new org.apache.http.ParseException();
	                        }
                        }
                    });
                } else {
                	PicsterApplication.currentUser = new PicUser(parseUser.getUsername(), parseUser.getString("name"), parseUser);
                    Log.d(PicsterApplication.TAG, "User logged in through Facebook!");

                    startHomeActivity();
                }
            }
        });
    }
    
    // Starts UserHomeActivity
    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}