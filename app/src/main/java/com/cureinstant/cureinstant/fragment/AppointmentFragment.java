package com.cureinstant.cureinstant.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.AutoCompleteTextViewAdapter;
import com.cureinstant.cureinstant.model.BookDoctor;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {

    AutoCompleteTextView locationAutoComplete, specialityAutoComplete;
    String finalLocality = "", finalCity = "", finalSpeciality = "";
    FetchLocation fetchLocation;
    FetchSpeciality fetchSpeciality;

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        locationAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.appointment_location_edittext);
        specialityAutoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.appointment_speciality_edittext);

        locationAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String term = s.toString();

                if (fetchLocation != null) {
                    //close the current task if it # null
                    fetchLocation.cancel(true);
                    fetchLocation = null;
                }

                if (!term.isEmpty()) {
                    //Start new one
                    fetchLocation = new FetchLocation(term);
                    fetchLocation.execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        specialityAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String term = s.toString();

                if (fetchSpeciality != null) {
                    //close the current task if it # null
                    fetchSpeciality.cancel(true);
                    fetchSpeciality = null;
                }

                if (!term.isEmpty()) {
                    //Start new one
                    fetchSpeciality = new FetchSpeciality(term);
                    fetchSpeciality.execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button searchButton = (Button) rootView.findViewById(R.id.appointment_submit_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalLocality.isEmpty() || finalCity.isEmpty()) {
                    locationAutoComplete.requestFocus();
                    locationAutoComplete.setError(getString(R.string.error_field_required));
                } else if (finalSpeciality.isEmpty()) {
                    specialityAutoComplete.requestFocus();
                    specialityAutoComplete.setError(getString(R.string.error_field_required));
                } else {
                    FetchBookDoctors fetchBookDoctors = new FetchBookDoctors(finalCity, finalLocality, finalSpeciality);
                    fetchBookDoctors.execute();
                }
            }
        });

        return rootView;
    }

    private class FetchLocation extends AsyncTask<Void, Void, ArrayList<String>> {

        String term;

        FetchLocation(String term) {
            this.term = term;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> locations = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();

            String url = "http://www.cureinstant.com/api/search/sugg/location";

            RequestBody body = new FormBody.Builder()
                    .add("term", term)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject locationObject = new JSONObject(s);
                JSONArray suggestionsArray = locationObject.getJSONArray("suggestions");
                for (int i = 0; i < suggestionsArray.length(); i++) {
                    JSONObject suggestion = suggestionsArray.getJSONObject(i);
                    String locality = suggestion.getString("locality");
                    String city = suggestion.getString("city");
                    if (!locality.isEmpty()) {
                        String tempString = locality + ", " + city;
                        locations.add(tempString);
                    }
                }
                return locations;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                locationAutoComplete.setThreshold(0);
                locationAutoComplete.setAdapter(new AutoCompleteTextViewAdapter(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        android.R.id.text1,
                        strings));
                locationAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] locationArray = strings.get(position).split(", ");
                        finalLocality = locationArray[0];
                        finalCity = locationArray[1];
                    }
                });
            }
        }
    }

    private class FetchSpeciality extends AsyncTask<Void, Void, ArrayList<String>> {

        String term;

        FetchSpeciality(String term) {
            this.term = term;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> specialities = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();

            String url = "http://www.cureinstant.com/api/search/sugg/search";

            RequestBody body = new FormBody.Builder()
                    .add("search", term)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject locationObject = new JSONObject(s);
                JSONArray suggestionsArray = locationObject.getJSONArray("suggestions");
                for (int i = 0; i < suggestionsArray.length(); i++) {
                    JSONObject suggestion = suggestionsArray.getJSONObject(i);
                    String speciality = suggestion.getString("search");
                    specialities.add(speciality);
                }
                return specialities;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                specialityAutoComplete.setThreshold(0);
                specialityAutoComplete.setAdapter(new AutoCompleteTextViewAdapter(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        android.R.id.text1,
                        strings));
                specialityAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        finalSpeciality = strings.get(position);
                    }
                });
            }
        }
    }

    private class FetchBookDoctors extends AsyncTask<Void, Void, ArrayList<BookDoctor>> {

        String city, locality, speciality;
        private ProgressDialog progressDialog;
        private Context context;

        FetchBookDoctors(String city, String locality, String speciality) {
            this.context = getContext();
            this.city = city;
            this.locality = locality;
            this.speciality = speciality;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading...");
            progressDialog.show();
        }

        @Override
        protected ArrayList<BookDoctor> doInBackground(Void... params) {
            ArrayList<BookDoctor> bookDoctors = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();

            String url = "http://www.cureinstant.com/api/search";

            RequestBody body = new FormBody.Builder()
                    .add("city", city)
                    .add("locality", locality)
                    .add("search", speciality)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject bookDoctorsObject = new JSONObject(s);
                JSONArray results = bookDoctorsObject.getJSONArray("search_results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject bookDoctorObject = results.getJSONObject(i);
                    int userID = 0, workID = 0;
                    String name = "", username = "", speciality = "", picture = "", fee = "";

                    JSONObject doctorObject = bookDoctorObject.getJSONObject("doctor");
                    userID = doctorObject.getInt("id");
                    name = doctorObject.getString("name");
                    username = doctorObject.getString("username");
                    speciality = doctorObject.getString("speciality");
                    picture = doctorObject.getString("profilePic");

                    JSONObject workDetails = bookDoctorObject.getJSONObject("work_details");
                    workID = workDetails.getInt("work_id");
                    fee = workDetails.getString("fee");

                    JSONObject workObject = bookDoctorObject.getJSONObject("location");
                    String workName = "", longitude = "", latitude = "", locality = "", sublocality = "", city = "", country = "", postalCode = "";
                    workName = workObject.getString("fullname");
                    longitude = workObject.getString("longitude");
                    latitude = workObject.getString("latitude");
                    locality = workObject.getString("locality");
                    sublocality = workObject.getString("sublocality");
                    city = workObject.getString("city");
                    country = workObject.getString("country");
                    postalCode = workObject.getString("postal_code");
                    DoctorWorkPlace doctorWorkPlace = new DoctorWorkPlace(workName, longitude, latitude, locality, sublocality, city, country, postalCode);

                    bookDoctors.add(new BookDoctor(userID, workID, name, username, speciality, picture, fee, doctorWorkPlace));
                }

                return bookDoctors;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BookDoctor> bookDoctors) {
            super.onPostExecute(bookDoctors);
            progressDialog.dismiss();
            if (bookDoctors != null) {
                if (bookDoctors.isEmpty()) {
                    Toast.makeText(getContext(), "No " + speciality + " found in this area", Toast.LENGTH_LONG).show();
                } else {
                    DoctorsBottomSheetFragment doctorsBottomSheetFragment = DoctorsBottomSheetFragment.getInstance();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("doctors", bookDoctors);
                    doctorsBottomSheetFragment.setArguments(bundle);
                    doctorsBottomSheetFragment.show(getFragmentManager(), "BookDoctors");
                }
            } else {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
