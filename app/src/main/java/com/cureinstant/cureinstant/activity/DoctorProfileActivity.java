package com.cureinstant.cureinstant.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorAchievementsAdapter;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorAlbumAdapter;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorEduAdapter;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorFeedbackAdapter;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorPublicationAdapter;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorSkillsAdapter;
import com.cureinstant.cureinstant.adapter.doctorDetails.DoctorWorkDetailsAdapter;
import com.cureinstant.cureinstant.misc.SimpleDividerItemDecoration;
import com.cureinstant.cureinstant.model.Doctor;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorAchievement;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorEduDetail;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorFeedback;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorPublication;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorSkill;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorWorkDetail;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorWorkPlace;
import com.cureinstant.cureinstant.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

public class DoctorProfileActivity extends AppCompatActivity implements View.OnClickListener {

    View rootView;
    int userID;
    boolean isFollowing;
    Button followButton;
    Button bookButton;
    ProgressDialog progressDialog;
    RecyclerView doctorAlbumList;
    DoctorAlbumAdapter doctorAlbumAdapter;
    RecyclerView doctorWorkDetailsList;
    DoctorWorkDetailsAdapter workDetailsAdapter;
    RecyclerView doctorSkillsList;
    DoctorSkillsAdapter doctorSkillsAdapter;
    RecyclerView doctorEduList;
    DoctorEduAdapter doctorEduAdapter;
    RecyclerView doctorFeedbackList;
    DoctorFeedbackAdapter doctorFeedbackAdapter;
    RecyclerView doctorPublicationList;
    DoctorPublicationAdapter doctorPublicationAdapter;
    RecyclerView doctorAchievementList;
    DoctorAchievementsAdapter doctorAchievementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        String name = null;
        String username = null;
        if (b != null) {
            name = b.getString("name");
            username = b.getString("username");
        }

        setContentView(R.layout.activity_doctor_profile);
        rootView = findViewById(R.id.doctor_profile_root_view);
        rootView.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(name);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        followButton = (Button) findViewById(R.id.doctor_follow_button);
        followButton.setOnClickListener(this);
        bookButton = (Button) findViewById(R.id.doctor_book_button);
        bookButton.setOnClickListener(this);

        doctorWorkDetailsList = (RecyclerView) findViewById(R.id.doctor_work_details_item);
        doctorWorkDetailsList.setLayoutManager(new LinearLayoutManager(DoctorProfileActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        doctorWorkDetailsList.setAdapter(workDetailsAdapter);
        doctorWorkDetailsList.setItemAnimator(new DefaultItemAnimator());
        doctorWorkDetailsList.addItemDecoration(new SimpleDividerItemDecoration(this));

        doctorSkillsList = (RecyclerView) findViewById(R.id.doctor_specialisation_item);
        doctorSkillsList.setLayoutManager(new LinearLayoutManager(DoctorProfileActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        doctorSkillsList.setAdapter(doctorSkillsAdapter);
        doctorSkillsList.setItemAnimator(new DefaultItemAnimator());

        doctorEduList = (RecyclerView) findViewById(R.id.doctor_edu_item);
        doctorEduList.setLayoutManager(new LinearLayoutManager(DoctorProfileActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        doctorEduList.setAdapter(doctorEduAdapter);
        doctorEduList.setItemAnimator(new DefaultItemAnimator());
        doctorEduList.addItemDecoration(new SimpleDividerItemDecoration(this));

        doctorFeedbackList = (RecyclerView) findViewById(R.id.doctor_feedback_item);
        doctorFeedbackList.setLayoutManager(new LinearLayoutManager(DoctorProfileActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        doctorFeedbackList.setAdapter(doctorFeedbackAdapter);
        doctorFeedbackList.setItemAnimator(new DefaultItemAnimator());
        doctorFeedbackList.addItemDecoration(new SimpleDividerItemDecoration(this));

        doctorPublicationList = (RecyclerView) findViewById(R.id.doctor_publication_item);
        doctorPublicationList.setLayoutManager(new LinearLayoutManager(DoctorProfileActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        doctorPublicationList.setAdapter(doctorPublicationAdapter);
        doctorPublicationList.setItemAnimator(new DefaultItemAnimator());
        doctorPublicationList.addItemDecoration(new SimpleDividerItemDecoration(this));

        doctorAchievementList = (RecyclerView) findViewById(R.id.doctor_achievement_item);
        doctorAchievementList.setLayoutManager(new LinearLayoutManager(DoctorProfileActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        doctorAchievementList.setAdapter(doctorAchievementsAdapter);
        doctorAchievementList.setItemAnimator(new DefaultItemAnimator());
        doctorAchievementList.addItemDecoration(new SimpleDividerItemDecoration(this));

        doctorAlbumList = (RecyclerView) findViewById(R.id.doctor_images_list);
        doctorAlbumList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        doctorAlbumList.setItemAnimator(new DefaultItemAnimator());

        if (username != null) {
            RequestUserData requestUserData = new RequestUserData(username);
            requestUserData.execute();
        } else {
            Toast.makeText(DoctorProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private Doctor getDoctorDetails(String s) throws JSONException {
        int userID = 0;
        String name = "", username = "", profilePicture = "", accountType = "", subAccountType = "";
        JSONObject feedJson = new JSONObject(s);
        accountType = feedJson.getString("account_type");
        subAccountType = feedJson.getString("account_subtype");
        name = feedJson.getString("name");
        username = feedJson.getString("username");
        profilePicture = feedJson.getString("profile_pic");

        String sex = "", email = "", number = "", address = "", summary = "", speciality = "";
        int followers = 0, followings = 0;
        boolean following = false;
        ArrayList<String> album = new ArrayList<>();
        ArrayList<DoctorEduDetail> doctorEduDetails = new ArrayList<>();
        ArrayList<DoctorSkill> doctorSkills = new ArrayList<>();
        ArrayList<DoctorWorkDetail> doctorWorkDetails = new ArrayList<>();
        ArrayList<DoctorAchievement> doctorAchievements = new ArrayList<>();
        ArrayList<DoctorPublication> doctorPublications = new ArrayList<>();
        ArrayList<DoctorFeedback> doctorFeedbacks = new ArrayList<>();

        if (accountType.equals("P") && subAccountType.equals("Doctor")) {
            if (!feedJson.isNull("speciality")) {
                speciality = feedJson.getString("speciality");
            }
            userID = feedJson.getInt("profile_id");

            if (!feedJson.isNull("about")) {
                JSONObject aboutObject = feedJson.getJSONObject("about");
                number = aboutObject.getString("phone");
                address = aboutObject.getString("address");
                summary = aboutObject.getString("summary");
            }

            JSONObject followObject = feedJson.getJSONObject("followCount");
            if (!followObject.isNull("followings")) {
                followings = followObject.getInt("followings");
            }
            if (!followObject.isNull("followers")) {
                followers = followObject.getInt("followers");
            }
            if (!feedJson.isNull("followed")) {
                following = true;
            }

            JSONArray albumArray = feedJson.getJSONArray("album");
            for (int i = 0; i < albumArray.length(); i++) {
                JSONObject imageObject = albumArray.getJSONObject(i);
                album.add(imageObject.getString("image"));
            }

            String courseName, instituteName, startDate, endDate;
            int courseID, instituteID;
            JSONArray doctorEduArray = feedJson.getJSONArray("edu_details");
            for (int i = 0; i < doctorEduArray.length(); i++) {
                JSONObject eduObject = doctorEduArray.getJSONObject(i);
                courseName = eduObject.getString("course_name");
                instituteName = eduObject.getString("institute_name");
                startDate = eduObject.getString("started");
                endDate = eduObject.getString("ended");
                if (endDate.equals("null")) {
                    endDate = "Present";
                }
                courseID = eduObject.getInt("course_id");
                instituteID = eduObject.getInt("institution_id");
                doctorEduDetails.add(new DoctorEduDetail(courseName, instituteName, startDate, endDate, courseID, instituteID));
            }

            String skill, description;
            JSONArray doctorSkillsArray = feedJson.getJSONArray("skills_det");
            for (int i = 0; i < doctorSkillsArray.length(); i++) {
                JSONObject skillObject = doctorSkillsArray.getJSONObject(i);
                skill = skillObject.getString("skill");
                description = skillObject.getString("description");
                doctorSkills.add(new DoctorSkill(skill, description));
            }

            int workID;
            String workSpeciality, position, fee, workStartDate, workEndDate;
            String workPlaceName, longitude, latitude, locality, sublocality, city, country, postalCode;
            JSONArray doctorWorkArray = feedJson.getJSONArray("work_details");
            for (int i = 0; i < doctorWorkArray.length(); i++) {
                JSONObject workObject = doctorWorkArray.getJSONObject(i);
                workID = workObject.getInt("work_id");
                workSpeciality = workObject.getString("speciality");
                position = workObject.getString("position");
                fee = workObject.getString("fee");
                workStartDate = workObject.getString("started");
                workEndDate = workObject.getString("ended");

                JSONObject workPlaceObject = workObject.getJSONObject("work_place");
                workPlaceName = workPlaceObject.getString("fullname");
                longitude = workPlaceObject.getString("longitude");
                latitude = workPlaceObject.getString("latitude");
                locality = workPlaceObject.getString("locality");
                sublocality = workPlaceObject.getString("sublocality");
                city = workPlaceObject.getString("city");
                country = workPlaceObject.getString("country");
                postalCode = workPlaceObject.getString("postal_code");
                DoctorWorkPlace doctorWorkPlace = new DoctorWorkPlace(workPlaceName, longitude,
                        latitude, locality, sublocality, city, country, postalCode);

                doctorWorkDetails.add(new DoctorWorkDetail(workID, workSpeciality, position, fee,
                        workStartDate, workEndDate, doctorWorkPlace));
            }

            String title, desc, link, image;
            JSONArray doctorAchievementArray = feedJson.getJSONArray("achievements_det");
            for (int i = 0; i < doctorAchievementArray.length(); i++) {
                JSONObject achievementObject = doctorAchievementArray.getJSONObject(i);
                title = achievementObject.getString("title");
                desc = achievementObject.getString("description");
                link = achievementObject.getString("link");
                image = achievementObject.getString("image");

                if (!link.startsWith("www.") && !link.startsWith("http://") && !link.startsWith("https://")) {
                    link = "www." + link;
                }
                if (!link.startsWith("http://") && !link.startsWith("https://")) {
                    link = "http://" + link;
                }

                doctorAchievements.add(new DoctorAchievement(title, desc, link, image));
            }

            // TODO: 01-04-2017 publication and feedback not in json. add when in json

            if (feedJson.has("publications_det")) {
                String publicationTitle, publicationContent;
                JSONArray doctorPublicationsArray = feedJson.getJSONArray("publications_det");
                for (int i = 0; i < doctorPublicationsArray.length(); i++) {
                    JSONObject publicationObject = doctorPublicationsArray.getJSONObject(i);
                    publicationTitle = publicationObject.getString("title");
                    publicationContent = publicationObject.getString("content");

                    doctorPublications.add(new DoctorPublication(publicationTitle, publicationContent));
                }
            }

            if (feedJson.has("feedback_det")) {
                String feedback, feedbackName, feedbackUsername, feedbackPicture;
                JSONArray doctorFeedbackArray = feedJson.getJSONArray("feedback_det");
                for (int i = 0; i < doctorFeedbackArray.length(); i++) {
                    JSONObject feedbackObject = doctorFeedbackArray.getJSONObject(i);
                    feedback = feedbackObject.getString("feedback");
                    feedbackName = feedbackObject.getString("name");
                    feedbackUsername = feedbackObject.getString("username");
                    feedbackPicture = feedbackObject.getString("picture");

                    doctorFeedbacks.add(new DoctorFeedback(feedback, feedbackName, feedbackUsername, feedbackPicture));
                }
            }
        }

        return new Doctor(userID, accountType, name, username, sex, email, number, address, summary,
                speciality, profilePicture, followers, followings, following, album, doctorEduDetails,
                doctorSkills, doctorWorkDetails, doctorAchievements, doctorPublications, doctorFeedbacks);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doctor_follow_button:
                if (!isFollowing) {
                    Utilities.FollowDoctor followDoctor = new Utilities.FollowDoctor(true, userID);
                    followDoctor.execute();
                    followButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    followButton.setTextColor(getResources().getColor(R.color.white));
                    followButton.setText("Unfollow");
                    isFollowing = true;
                } else {
                    Utilities.FollowDoctor followDoctor = new Utilities.FollowDoctor(false, userID);
                    followDoctor.execute();
                    followButton.setBackgroundColor(getResources().getColor(R.color.white));
                    followButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    followButton.setText("Follow");
                    isFollowing = false;
                }
                break;
            case R.id.doctor_book_button:
                Toast.makeText(DoctorProfileActivity.this, "This feature is coming soon!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class RequestUserData extends AsyncTask<Void, Void, Doctor> {

        String username;

        RequestUserData(String username) {
            this.username = username;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            rootView.setVisibility(View.GONE);
        }

        @Override
        protected Doctor doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .build();

            String url = "http://www.cureinstant.com/api/profile/";
            url += username;
            url += "/fetch";

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                return getDoctorDetails(s);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            super.onPostExecute(doctor);
            progressDialog.dismiss();
            rootView.setVisibility(View.VISIBLE);

            userID = doctor.getUserID();

            TextView name = (TextView) findViewById(R.id.doctor_name);
            name.setText(doctor.getName());
            TextView sex = (TextView) findViewById(R.id.doctor_sex);
            if (!doctor.getSex().isEmpty()) {
                sex.setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_sex_title).setVisibility(View.VISIBLE);
                sex.setText(doctor.getSex());
            }
            TextView location = (TextView) findViewById(R.id.doctor_location);
            if (!doctor.getAddress().isEmpty() && !doctor.getAddress().equals("null")) {
                location.setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_location_title).setVisibility(View.VISIBLE);
                location.setText(doctor.getAddress());
            }
            TextView number = (TextView) findViewById(R.id.doctor_number);
            if (!doctor.getNumber().isEmpty() && !doctor.getNumber().equals("null")) {
                number.setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_number_title).setVisibility(View.VISIBLE);
                number.setText(doctor.getNumber());
            }
            TextView email = (TextView) findViewById(R.id.doctor_email);
            if (!doctor.getEmail().isEmpty()) {
                email.setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_email_title).setVisibility(View.VISIBLE);
                email.setText(doctor.getEmail());
            }
            TextView followings = (TextView) findViewById(R.id.doctor_followings);
            followings.setText(String.format(getString(R.string.following_count), doctor.getFollowings()));
            TextView followers = (TextView) findViewById(R.id.doctor_followers);
            followers.setText(String.format(getString(R.string.followers_count), doctor.getFollowers()));

            isFollowing = doctor.isFollowing();
            if (doctor.isFollowing()) {
                followButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                followButton.setTextColor(getResources().getColor(R.color.white));
                followButton.setText("Unfollow");
            } else {
                followButton.setBackgroundColor(getResources().getColor(R.color.white));
                followButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                followButton.setText("Follow");
            }

            if (!doctor.getAlbum().isEmpty()) {
                doctorAlbumList.setVisibility(View.VISIBLE);
                doctorAlbumAdapter = new DoctorAlbumAdapter(DoctorProfileActivity.this, doctor.getAlbum());
                doctorAlbumList.setAdapter(doctorAlbumAdapter);
            }

            TextView summary = (TextView) findViewById(R.id.doctor_summary_tv);
            if (!doctor.getSummary().isEmpty() && !doctor.getSummary().equals("null")) {
                findViewById(R.id.doctor_summary).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_summary_title).setVisibility(View.VISIBLE);
                summary.setText(doctor.getSummary());
            }

            ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
            String imageURL = Utilities.profilePicSmallBaseUrl + doctor.getProfilePicture();
            Glide.with(DoctorProfileActivity.this).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(userPicture);

            if (!doctor.getDoctorWorkDetails().isEmpty()) {
                findViewById(R.id.doctor_work_details).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_work_details_title).setVisibility(View.VISIBLE);
                workDetailsAdapter = new DoctorWorkDetailsAdapter(DoctorProfileActivity.this, doctor.getDoctorWorkDetails());
                doctorWorkDetailsList.setAdapter(workDetailsAdapter);
            }

            if (!doctor.getDoctorSkills().isEmpty()) {
                findViewById(R.id.doctor_specialisation).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_specialisation_title).setVisibility(View.VISIBLE);
                doctorSkillsAdapter = new DoctorSkillsAdapter(DoctorProfileActivity.this, doctor.getDoctorSkills());
                doctorSkillsList.setAdapter(doctorSkillsAdapter);
            }

            if (!doctor.getDoctorEduDetails().isEmpty()) {
                findViewById(R.id.doctor_edu).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_edu_title).setVisibility(View.VISIBLE);
                doctorEduAdapter = new DoctorEduAdapter(DoctorProfileActivity.this, doctor.getDoctorEduDetails());
                doctorEduList.setAdapter(doctorEduAdapter);
            }

            if (!doctor.getDoctorFeedbacks().isEmpty()) {
                findViewById(R.id.doctor_feedback).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_feedback_title).setVisibility(View.VISIBLE);
                doctorFeedbackAdapter = new DoctorFeedbackAdapter(DoctorProfileActivity.this, doctor.getDoctorFeedbacks());
                doctorFeedbackList.setAdapter(doctorFeedbackAdapter);
            }

            if (!doctor.getDoctorPublications().isEmpty()) {
                findViewById(R.id.doctor_publication).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_publication_title).setVisibility(View.VISIBLE);
                doctorPublicationAdapter = new DoctorPublicationAdapter(DoctorProfileActivity.this, doctor.getDoctorPublications());
                doctorPublicationList.setAdapter(doctorPublicationAdapter);
            }

            if (!doctor.getDoctorAchievements().isEmpty()) {
                findViewById(R.id.doctor_achievements).setVisibility(View.VISIBLE);
                findViewById(R.id.doctor_achievements_title).setVisibility(View.VISIBLE);
                doctorAchievementsAdapter = new DoctorAchievementsAdapter(DoctorProfileActivity.this, doctor.getDoctorAchievements());
                doctorAchievementList.setAdapter(doctorAchievementsAdapter);
            }
        }
    }
}
