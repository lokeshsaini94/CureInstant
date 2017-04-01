package com.cureinstant.cureinstant.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Doctor;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorAchievement;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorEduDetail;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorFeedback;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorPublication;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorSkill;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorWorkDetail;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorWorkPlace;

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

public class DoctorProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        String username = null;
        if (b != null)
            username = b.getString("username");

        setContentView(R.layout.activity_doctor_profile);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(R.string.doctor_name);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (username != null) {
            RequestUserData requestUserData = new RequestUserData(username);
            requestUserData.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private Doctor getDoctorDetails(String s) throws JSONException {
        JSONObject feedJson = new JSONObject(s);
        String accountType = feedJson.getString("account_type");
        String name = feedJson.getString("name");
        String username = feedJson.getString("username");
        String profilePicture = feedJson.getString("profile_pic");

        String sex = "", email = "", number = "", address = "", summary = "", speciality = "";
        int followers = 0, followings = 0;
        ArrayList<String> album = new ArrayList<>();
        ArrayList<DoctorEduDetail> doctorEduDetails = new ArrayList<>();
        ArrayList<DoctorSkill> doctorSkills = new ArrayList<>();
        ArrayList<DoctorWorkDetail> doctorWorkDetails = new ArrayList<>();
        ArrayList<DoctorAchievement> doctorAchievements = new ArrayList<>();
        ArrayList<DoctorPublication> doctorPublications = new ArrayList<>();
        ArrayList<DoctorFeedback> doctorFeedbacks = new ArrayList<>();

        if (accountType.equals("P")) {
            speciality = feedJson.getString("speciality");

            if (!feedJson.isNull("about")) {
                JSONObject aboutObject = feedJson.getJSONObject("about");
                number = aboutObject.getString("phone");
                address = aboutObject.getString("address");
                summary = aboutObject.getString("summary");
            }

            JSONObject followObject = feedJson.getJSONObject("followCount");
            if (!feedJson.isNull("followings")) {
                followings = followObject.getInt("followings");
            }
            if (!feedJson.isNull("followers")) {
                followers = followObject.getInt("followers");
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

        return new Doctor(accountType, name, username, sex, email, number, address, summary,
                speciality, profilePicture, followers, followings, album, doctorEduDetails,
                doctorSkills, doctorWorkDetails, doctorAchievements, doctorPublications, doctorFeedbacks);
    }

    private class RequestUserData extends AsyncTask<Void, Void, Doctor> {

        String username;

        public RequestUserData(String username) {
            this.username = username;
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
                Log.e("TAG", "doInBackground: s " + s);
                return getDoctorDetails(s);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            super.onPostExecute(doctor);
            // TODO: 01-04-2017 Process doctor details and display them
        }
    }
}
