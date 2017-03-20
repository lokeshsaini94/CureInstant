package com.cureinstant.cureinstant.fragment;


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

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.MyPreferencesActivity;
import com.cureinstant.cureinstant.activity.ProfileActivity;
import com.cureinstant.cureinstant.model.User;
import com.cureinstant.cureinstant.util.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

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

        userPicture = (ImageView) rootView.findViewById(R.id.user_picture);
        userName = (TextView) rootView.findViewById(R.id.user_name);

        View profileView = rootView.findViewById(R.id.user_details_container);
        profileView.setOnClickListener(this);
        View settings = rootView.findViewById(R.id.more_settings);
        settings.setOnClickListener(this);

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
            case R.id.more_settings:
                Intent settingsIntent = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(settingsIntent);
                break;
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
            String sex = "";
            String email = "";
            String number = "";
            String dob = "";
            String picture = "";

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/user-info")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.e("MoreFragment", "doInBackground: accessTokenValue " + accessTokenValue);
                String s = response.body().string();
                JSONObject profileJson = new JSONObject(s);
                name = profileJson.getString("name");
                username = profileJson.getString("username");
                sex = profileJson.getString("gender");
                email = profileJson.getString("email");
                number = profileJson.getString("mobile");
                dob = profileJson.getString("dob");
                JSONObject pictureData = profileJson.getJSONObject("profile_pic");
                picture = pictureData.getString("pic_name");

                return new User(name, username, sex, email, number, dob, picture);


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null) {
                userName.setText(user.getName());
                Glide.with(getContext()).load(Utilities.profilePicSmallBaseUrl + user.getPicture()).into(userPicture);
                userInfo = user;
            }
        }
    }
}
