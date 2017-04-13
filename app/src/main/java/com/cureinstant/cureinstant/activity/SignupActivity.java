package com.cureinstant.cureinstant.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetDatePickerDialog.OnDateSetListener {

    private static final String PASSWORD_PATTERN =
            "(" +                           // Start of group
                    "(?=.*\\d)" +           // must contains one digit from 0-9
                    "(?=.*[a-zA-Z])" +      // must contains one characters
                    "(?=.*[@#$_*!.-])" +    // must contains one special symbols in the list "@#$_*!.-"
                    "." +                   // match anything with previous condition checking
                    "{6,25}" +              // length at least 6 characters and maximum of 25
                    ")";                    // End of group
    String accessToken;
    String refreshToken;

    ProgressDialog progressDialog;
    AlertDialog.Builder otpDialog;
    private String firstName;
    private String lastName;
    private String dob;
    private int sex;
    private String email = "";
    private String number;
    private String otpFinal;
    private String password;
    private boolean isOtpSent = false;

    private EditText dobET;
    private EditText numberET;
    private EditText otpET;
    private View mProgressView;
    private View mSignUpFormView;
    private View emailSignUpForm1;
    private View emailSignUpForm2;
    private View emailSignUpForm3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        otpDialog = new AlertDialog.Builder(SignupActivity.this);
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Please Wait...");

        emailSignUpForm1 = findViewById(R.id.email_signup_form1);
        emailSignUpForm2 = findViewById(R.id.email_signup_form2);
        emailSignUpForm3 = findViewById(R.id.email_signup_form3);
        mProgressView = findViewById(R.id.signup_progress);
        mSignUpFormView = findViewById(R.id.signup_form);

        ImageButton closeSignUp = (ImageButton) findViewById(R.id.close_signup);
        closeSignUp.setOnClickListener(this);

        Button buttonSignUp1 = (Button) findViewById(R.id.sign_up_button1);
        Button buttonSignUp2 = (Button) findViewById(R.id.sign_up_button2);
        Button buttonSignUp3 = (Button) findViewById(R.id.sign_up_button3);
        buttonSignUp1.setOnClickListener(this);
        buttonSignUp2.setOnClickListener(this);
        buttonSignUp3.setOnClickListener(this);

        Button verifyOTP = (Button) findViewById(R.id.otp_button);
        verifyOTP.setOnClickListener(this);
        numberET = (EditText) findViewById(R.id.number);
        dobET = (EditText) findViewById(R.id.dob);
        otpET = (EditText) findViewById(R.id.otp);
        dobET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    openDatePicker();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.close_signup:
                finish();
                break;
            case R.id.sign_up_button1:
                if (validateInput1()) {
                    emailSignUpForm1.setVisibility(View.GONE);
                    emailSignUpForm2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sign_up_button2:
                if (isOtpSent) {
                    CheckEmailAndOtpValid checkEmailAndOtpValid = new CheckEmailAndOtpValid();
                    checkEmailAndOtpValid.execute();
                } else {
                    numberET.setError("Please Verify your number first");
                    numberET.requestFocus();
                }
                break;
            case R.id.sign_up_button3:
                if (validateInput3()) {
                    UserSignUpTask userSignUpTask = new UserSignUpTask();
                    userSignUpTask.execute();
                }
                break;
            case R.id.otp_button:
                numberET.setError(null);
                CheckNumber checkNumber = new CheckNumber();
                checkNumber.execute();
                break;

        }
    }

    // starts the date picker for the date of birth input
    private void openDatePicker() {
        Calendar now = Calendar.getInstance();
        BottomSheetDatePickerDialog date = BottomSheetDatePickerDialog.newInstance(
                SignupActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        date.show(getSupportFragmentManager(), "DOB");
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear += 1;
        String month = String.valueOf(monthOfYear);
        if (monthOfYear < 10) {
            month = "0" + (monthOfYear);
        }
        dobET.setText(year + "-" + month + "-" + dayOfMonth);
    }

    // validates the 1st signup page
    private Boolean validateInput1() {
        EditText mFirstName = (EditText) findViewById(R.id.firstname);
        EditText mLastName = (EditText) findViewById(R.id.lastname);
        EditText mDob = (EditText) findViewById(R.id.dob);
        RadioButton mMale = (RadioButton) findViewById(R.id.radioMale);
        RadioButton mFemale = (RadioButton) findViewById(R.id.radioFemale);
        RadioButton mOther = (RadioButton) findViewById(R.id.radioOther);

        // Reset errors.
        mFirstName.setError(null);
        mLastName.setError(null);
        mDob.setError(null);

        // Store values at the time of validation.
        firstName = mFirstName.getText().toString();
        lastName = mLastName.getText().toString();
        dob = mDob.getText().toString();
        if (mMale.isChecked()) {
            sex = 1;
        } else if (mFemale.isChecked()) {
            sex = 2;
        } else if (mOther.isChecked()) {
            sex = 3;
        }

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered a valid First Name.
        if (TextUtils.isEmpty(firstName)) {
            mFirstName.setError(getString(R.string.error_field_required));
            focusView = mFirstName;
            cancel = true;
        }

        // Check if the user entered a valid Last Name.
        if (TextUtils.isEmpty(lastName)) {
            mLastName.setError(getString(R.string.error_field_required));
            focusView = mLastName;
            cancel = true;
        }

        // Check if the user entered a valid dob.
        if (TextUtils.isEmpty(dob)) {
            mDob.setError(getString(R.string.error_field_required));
            focusView = mDob;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    // validates the 3rd signup page
    private Boolean validateInput3() {
        EditText mPassword = (EditText) findViewById(R.id.password);
        EditText mPasswordConfirm = (EditText) findViewById(R.id.password_confirm);

        // Reset errors.
        mPassword.setError(null);
        mPasswordConfirm.setError(null);

        // Store values at the time of validation.
        password = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered a valid Password.
        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            mPasswordConfirm.setError(getString(R.string.error_field_required));
            focusView = mPasswordConfirm;
            cancel = true;
        } else if (!TextUtils.isEmpty(passwordConfirm) && !password.equals(passwordConfirm)) {
            mPasswordConfirm.setError(getString(R.string.error_invalid_password_not_a_match));
            focusView = mPasswordConfirm;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    // checks if entered email is in valid format
    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // validates the password on the given pattern
    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Shows the progress UI and hides the sign up form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }

    // Validates the entered mobile number and starts RequestOTP task on success
    private class CheckNumber extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            number = numberET.getText().toString();
            progressDialog.show();

            // Check if the user entered a valid Number.
            if (TextUtils.isEmpty(number)) {
                this.cancel(true);
                progressDialog.dismiss();
                numberET.setError(getString(R.string.error_field_required));
                numberET.requestFocus();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody bodyNumber = new FormBody.Builder()
                    .add("mobile", number)
                    .build();

            String urlNumber = "http://www.cureinstant.com/api/registration-query/mobile";

            Request requestNumber = new Request.Builder()
                    .url(urlNumber)
                    .post(bodyNumber)
                    .build();
            Response responseNumber;
            try {
                responseNumber = client.newCall(requestNumber).execute();
                String resultJsonString = responseNumber.body().string();
                JSONObject resultJson = new JSONObject(resultJsonString);
                String result = resultJson.getString("mobile");
                if (result.equals("Available.")) {
                    return true;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                progressDialog.dismiss();
                numberET.setError("Number not available!");
                numberET.requestFocus();
            } else {
                RequestOTP requestOTP = new RequestOTP();
                requestOTP.execute();
            }
        }
    }

    // Requests OTP on the the entered Mobile number
    private class RequestOTP extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody bodyNumber = new FormBody.Builder()
                    .add("mobile", number)
                    .build();

            String urlNumber = "http://www.cureinstant.com/api/registration-query/send-otp";

            Request requestNumber = new Request.Builder()
                    .url(urlNumber)
                    .post(bodyNumber)
                    .build();
            Response responseNumber;
            try {
                responseNumber = client.newCall(requestNumber).execute();
                String resultJsonString = responseNumber.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(SignupActivity.this, "OTP sent.", Toast.LENGTH_SHORT).show();
            isOtpSent = true;
        }
    }

    // Validates email and otp and starts next step on success
    private class CheckEmailAndOtpValid extends AsyncTask<Void, Void, boolean[]> {

        EditText mEmail = (EditText) findViewById(R.id.email);

        CheckEmailAndOtpValid() {
            email = mEmail.getText().toString();
            otpFinal = otpET.getText().toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Reset errors.
            mEmail.setError(null);
            otpET.setError(null);

            boolean cancel = false;
            View focusView = null;

            // Check if the user entered a valid Email.
            if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
                mEmail.setError("Invalid Email");
                focusView = mEmail;
                cancel = true;
            }

            // Check if the user entered a valid Number.
            if (TextUtils.isEmpty(otpFinal)) {
                otpET.setError(getString(R.string.error_field_required));
                focusView = otpET;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt sign up and focus the first
                // form field with an error.
                focusView.requestFocus();
                this.cancel(true);
            }
        }

        @Override
        protected boolean[] doInBackground(Void... params) {
            boolean[] validation = new boolean[2];
            validation[0] = false;
            validation[1] = false;

            OkHttpClient client = new OkHttpClient();

            RequestBody bodyEmail = new FormBody.Builder()
                    .add("email", email)
                    .build();

            String urlEmail = "http://www.cureinstant.com/api/registration-query/email";

            Request requestEmail = new Request.Builder()
                    .url(urlEmail)
                    .post(bodyEmail)
                    .build();
            Response responseEmail;
            try {
                responseEmail = client.newCall(requestEmail).execute();
                String resultJsonString = responseEmail.body().string();
                JSONObject resultJson = new JSONObject(resultJsonString);
                String result = resultJson.getString("email");
                if (result.equals("Available.")) {
                    validation[0] = true;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            RequestBody bodyOtp = new FormBody.Builder()
                    .add("mobile", number)
                    .add("otp", otpFinal)
                    .build();

            String urlOtp = "http://www.cureinstant.com/api/registration-query/invalidate-otp";

            Request requestOtp = new Request.Builder()
                    .url(urlOtp)
                    .post(bodyOtp)
                    .build();
            Response responseOtp;
            try {
                responseOtp = client.newCall(requestOtp).execute();
                String resultJsonString = responseOtp.body().string();
                JSONObject resultJson = new JSONObject(resultJsonString);
                if (resultJson.has("success")) {
                    String status = resultJson.getString("success");
                    if (status.equals("success")) {
                        validation[1] = true;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return validation;
        }

        @Override
        protected void onPostExecute(boolean[] booleen) {
            super.onPostExecute(booleen);
            if (booleen[0] && booleen[1]) {
                emailSignUpForm2.setVisibility(View.GONE);
                emailSignUpForm3.setVisibility(View.VISIBLE);
            } else {
                if (!booleen[0]) {
                    mEmail.setError("Email not available!");
                    mEmail.requestFocus();
                }
                if (!booleen[1]) {
                    otpET.setError("OTP not a match!");
                    otpET.requestFocus();
                }
            }
        }
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    private class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        String genderFinal;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            if (sex == 1) {
                genderFinal = "MALE";
            } else if (sex == 2) {
                genderFinal = "FEMALE";
            } else if (sex == 3) {
                genderFinal = "OTHER";
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody bodyNumber = new FormBody.Builder()
                    .add("first_name", firstName)
                    .add("last_name", lastName)
                    .add("email", email)
                    .add("mobile", number)
                    .add("otp", otpFinal)
                    .add("dob", dob)
                    .add("password", password)
                    .add("gender", genderFinal)
                    .build();

            String urlNumber = "http://www.cureinstant.com/api/user-registration";

            Request requestNumber = new Request.Builder()
                    .url(urlNumber)
                    .post(bodyNumber)
                    .build();
            Response responseNumber;
            try {
                responseNumber = client.newCall(requestNumber).execute();
                String resultJsonString = responseNumber.body().string();
                JSONObject resultObject = new JSONObject(resultJsonString);
                if (resultObject.has("success")) {
                    String status = resultObject.getString("success");
                    if (status.equals("registered")) {
                        return true;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                UserLoginTask userLoginTask = new UserLoginTask(number, password);
                userLoginTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("grant_type", "password")
                    .add("client_id", "3")
                    .add("client_secret", "4RI6m61rJi5XZLjArXSTNogD1qwRn5CVXXYVJxTW")
                    .add("username", mUsername)
                    .add("password", mPassword)
                    .add("scope", "*")
                    .build();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/login")
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject reader = new JSONObject(s);
                accessToken = reader.getString("access_token");
                refreshToken = reader.getString("refresh_token");
                if (!accessToken.isEmpty() && !refreshToken.isEmpty()) {
                    return true;
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Utilities.accessTokenKey, accessToken);
                editor.putString(Utilities.refreshTokenKey, refreshToken);
                editor.commit();
                Utilities.loggedInBool(getApplicationContext(), true);
                finish();
            } else {
                Snackbar.make(getCurrentFocus(), R.string.error_incorrect_username_password, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
