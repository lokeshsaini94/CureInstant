package com.cureinstant.cureinstant.activity;

import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetDatePickerDialog.OnDateSetListener {

    String firstName;
    String lastName;
    String dob;
    String email;
    String number;
    String username;
    String password;
    Boolean isMale;
    EditText dobET;
    // UI references.
    private View mProgressView;
    private View mSignUpFormView;
    private View emailSignUpForm1;
    private View emailSignUpForm2;
    private View emailSignUpForm3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        dobET = (EditText) findViewById(R.id.dob);
//        dobET.setOnClickListener(this);
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
                if (validateInput2()) {
                    emailSignUpForm2.setVisibility(View.GONE);
                    emailSignUpForm3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sign_up_button3:
                if (validateInput3()) {
                    UserSignUpTask userSignUpTask = new UserSignUpTask();
                    userSignUpTask.execute((Void) null);
                }
                break;
        }
    }

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
        dobET.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    private Boolean validateInput1() {
        EditText mFirstName = (EditText) findViewById(R.id.firstname);
        EditText mLastName = (EditText) findViewById(R.id.lastname);
        EditText mDob = (EditText) findViewById(R.id.dob);
        RadioButton mMale = (RadioButton) findViewById(R.id.radioMale);
        RadioButton mFemale = (RadioButton) findViewById(R.id.radioFemale);

        // Reset errors.
        mFirstName.setError(null);
        mLastName.setError(null);
        mDob.setError(null);

        // Store values at the time of validation.
        firstName = mFirstName.getText().toString();
        lastName = mLastName.getText().toString();
        dob = mDob.getText().toString();
        if (mMale.isChecked()) {
            isMale = true;
        } else if (mFemale.isChecked()) {
            isMale = false;
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

    private Boolean validateInput2() {
        EditText mEmail = (EditText) findViewById(R.id.email);
        EditText mNumber = (EditText) findViewById(R.id.number);

        // Reset errors.
        mEmail.setError(null);
        mNumber.setError(null);

        // Store values at the time of validation.
        email = mEmail.getText().toString();
        number = mNumber.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered a valid Email.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            mEmail.setError("Invalid Email");
            focusView = mEmail;
            cancel = true;
        }

        // Check if the user entered a valid Number.
        if (TextUtils.isEmpty(number)) {
            mNumber.setError(getString(R.string.error_field_required));
            focusView = mNumber;
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

    private Boolean validateInput3() {
        EditText mUsername = (EditText) findViewById(R.id.username);
        EditText mPassword = (EditText) findViewById(R.id.password);

        // Reset errors.
        mUsername.setError(null);
        mPassword.setError(null);

        // Store values at the time of validation.
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered a valid Username.
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }

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

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the sign up form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    private class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

//        private final String firstName;
//        private final String LastName;
//        private final String dob;
//        private final String email;
//        private final String number;
//        private final String username;
//        private final String password;
//        private final Boolean isMale;
//
//        public UserSignUpTask(String firstName, String lastName, String dob, String email, String number, String username, String password, Boolean isMale) {
//            this.firstName = firstName;
//            LastName = lastName;
//            this.dob = dob;
//            this.email = email;
//            this.number = number;
//            this.username = username;
//            this.password = password;
//            this.isMale = isMale;
//        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);

                // Sign up and return a boolean.
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Utilities.loggedInBool(getApplicationContext(), true);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
