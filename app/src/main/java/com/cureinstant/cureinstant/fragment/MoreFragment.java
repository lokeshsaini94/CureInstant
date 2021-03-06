package com.cureinstant.cureinstant.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.ChatActivity;
import com.cureinstant.cureinstant.activity.FollowActivity;
import com.cureinstant.cureinstant.activity.MyPreferencesActivity;
import com.cureinstant.cureinstant.activity.MyQueriesActivity;
import com.cureinstant.cureinstant.activity.ProfileActivity;
import com.cureinstant.cureinstant.model.Follow;
import com.cureinstant.cureinstant.model.User;
import com.cureinstant.cureinstant.util.Utilities;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MoreFragment";
    public static int REQUEST_INVITE = 98;
    ProgressDialog progressDialog;

    private User userInfo = null;
    private ImageView userPicture;
    private TextView userName;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading...");

        userPicture = (ImageView) rootView.findViewById(R.id.user_picture);
        userName = (TextView) rootView.findViewById(R.id.user_name);

        View profileView = rootView.findViewById(R.id.user_details_container);
        profileView.setOnClickListener(this);
        View chatView = rootView.findViewById(R.id.more_chat);
        chatView.setOnClickListener(this);
        View settings = rootView.findViewById(R.id.more_settings);
        settings.setOnClickListener(this);
        View myQueries = rootView.findViewById(R.id.more_questions);
        myQueries.setOnClickListener(this);

        View following = rootView.findViewById(R.id.more_following);
        View followers = rootView.findViewById(R.id.more_followers);
        following.setOnClickListener(this);
        followers.setOnClickListener(this);

        View invite = rootView.findViewById(R.id.invite_others);
        invite.setOnClickListener(this);

        RequestUserData requestUserData = new RequestUserData();
        requestUserData.execute();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_details_container:
                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                profileIntent.putExtra("user_info", userInfo);
                startActivity(profileIntent);
                break;
            case R.id.more_chat:
                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                chatIntent.putExtra("user_info", userInfo);
                startActivity(chatIntent);
                break;
            case R.id.more_questions:
                Intent myQueriesIntent = new Intent(getContext(), MyQueriesActivity.class);
                startActivity(myQueriesIntent);
                break;
            case R.id.more_settings:
                Intent settingsIntent = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.more_following:
                RequestFollowData requestFollowingData = new RequestFollowData("followings");
                requestFollowingData.execute();
                break;
            case R.id.more_followers:
                RequestFollowData requestFollowersData = new RequestFollowData("followers");
                requestFollowersData.execute();
                break;
            case R.id.invite_others:
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage(getString(R.string.invitation_message))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                Toast.makeText(getContext(), R.string.text_invitation_cancelled, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Requests followers or Followings data from api call
    private class RequestFollowData extends AsyncTask<Void, Void, ArrayList<Follow>> {

        private String type;

        RequestFollowData(String type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<Follow> doInBackground(Void... params) {
            ArrayList<Follow> follows = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            int followID = 0;
            int userID = 0;
            String name = "";
            String username = "";
            String speciality = "";
            String picture = "";
            boolean isFollowing = false;

            String url = "http://www.cureinstant.com/api/";
            if (type.equals("followers")) {
                url += "followers/fetch";
            } else {
                url += "followings/fetch";
            }

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject followJson = new JSONObject(s);
                JSONArray followArray;
                if (type.equals("followers")) {
                    followArray = followJson.getJSONArray("followers");
                } else {
                    followArray = followJson.getJSONArray("followings");
                }

                for (int i = 0; i < followArray.length(); i++) {
                    followID = 0;
                    userID = 0;
                    isFollowing = false;
                    name = "";
                    username = "";
                    speciality = "";
                    picture = "";

                    JSONObject followObject = followArray.getJSONObject(i);
                    switch (type) {
                        case "followers":
                            followID = followObject.getInt("id");
                            userID = followObject.getInt("f_id");
                            isFollowing = followObject.getBoolean("following");
                            JSONObject followerObject = followObject.getJSONObject("follower");
                            name = followerObject.getString("name");
                            username = followerObject.getString("username");
                            if (!followerObject.isNull("speciality")) {
                                speciality = followerObject.getString("speciality");
                            }
                            if (!followerObject.isNull("profile_pic")) {
                                JSONObject pictureObject = followerObject.getJSONObject("profile_pic");
                                picture = pictureObject.getString("pic_name");
                            }
                            break;
                        default:
                            followID = followObject.getInt("id");
                            userID = followObject.getInt("f_id");
                            JSONObject followingObject = followObject.getJSONObject("following");
                            name = followingObject.getString("name");
                            username = followingObject.getString("username");
                            if (!followingObject.isNull("speciality")) {
                                speciality = followingObject.getString("speciality");
                            }
                            if (!followingObject.isNull("profile_pic")) {
                                JSONObject pictureObject = followingObject.getJSONObject("profile_pic");
                                picture = pictureObject.getString("pic_name");
                            }
                            break;
                    }

                    follows.add(new Follow(followID, userID, name, username, speciality, picture, isFollowing));
                }

                return follows;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Follow> follows) {
            super.onPostExecute(follows);
            progressDialog.dismiss();
            if (follows.size() < 1) {
                Toast.makeText(getContext(), R.string.text_no + " " + type, Toast.LENGTH_SHORT).show();
            } else {
                Intent followersIntent = new Intent(getContext(), FollowActivity.class);
                followersIntent.putExtra("type", type);
                followersIntent.putParcelableArrayListExtra("follows", follows);
                startActivity(followersIntent);
            }
        }
    }


    // Fetches and Sets User data from api call
    private class RequestUserData extends AsyncTask<Void, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            String name = "";
            String username = "";
            String sex = "Male";
            String email = "";
            String number = "";
            String dob = "";
            String picture = "";

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/profile/fetch")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject profileJson = new JSONObject(s);
                name = profileJson.getString("name");
                username = profileJson.getString("username");
//                sex = profileJson.getString("gender");
                JSONObject aboutObject = profileJson.getJSONObject("about");
                email = aboutObject.getString("email");
                number = aboutObject.getString("mobile");
                dob = aboutObject.getString("birthday");
                picture = profileJson.getString("profile_pic");

                return new User(name, username, sex, email, number, dob, picture);


            } catch (IOException | JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                userName.setText(user.getName());
                Glide.with(getContext()).load(Utilities.profilePicSmallBaseUrl + user.getPicture()).placeholder(R.drawable.doctor_placeholder).into(userPicture);
                userInfo = user;
            }
        }
    }
}
