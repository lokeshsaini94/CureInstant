package com.cureinstant.cureinstant.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    String accessToken;
    String refreshToken;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        ImageButton closeLogin = (ImageButton) findViewById(R.id.close_login);
        closeLogin.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (Utilities.checkConnection()) {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                mAuthTask = new UserLoginTask(username, password);
                mAuthTask.execute((Void) null);
            } else {
                final Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), "Check Internet connection", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                attemptLogin();
                break;
            case R.id.close_login:
                finish();
                break;
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
                    .url("http://www.cureinstant.com/oauth/token")
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
            mAuthTask = null;
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
            mAuthTask = null;
            showProgress(false);
        }
    }
}

