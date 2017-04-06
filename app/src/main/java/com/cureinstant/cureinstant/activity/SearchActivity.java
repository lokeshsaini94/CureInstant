package com.cureinstant.cureinstant.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.adapter.SearchAdapter;
import com.cureinstant.cureinstant.misc.OnLoadMoreListener;
import com.cureinstant.cureinstant.misc.SimpleDividerItemDecoration;
import com.cureinstant.cureinstant.model.SearchProfile;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    private int currentPage = 1, lastPage = 1;
    private RecyclerView searchRecyclerView;
    private SearchAdapter searchAdapter;
    private ArrayList<SearchProfile> searchProfiles = new ArrayList<>();
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");

        ImageButton closeSearch = (ImageButton) findViewById(R.id.search_close);
        closeSearch.setOnClickListener(this);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        searchEditText = (EditText) findViewById(R.id.search_edittext);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        searchRecyclerView.setLayoutManager(mLayoutManager);
        searchAdapter = new SearchAdapter(this, searchProfiles, searchRecyclerView);
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        searchAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage <= lastPage) {
                    if (Utilities.checkConnection()) {
                        RequestSearchData requestSearchData = new RequestSearchData();
                        requestSearchData.execute();
                    }
                }
            }
        });
    }

    private void performSearch() {
        String searchString = searchEditText.getText().toString();
        if (searchString.isEmpty()) {
            searchEditText.setError(getString(R.string.error_field_required));
        } else {
            Utilities.hideSoftKeyboard(SearchActivity.this);
            progressDialog.show();
            currentPage = 1;
            if (!searchProfiles.isEmpty()) {
                searchProfiles.clear();
                searchAdapter.notifyDataSetChanged();
            }
            RequestSearchData requestSearchData = new RequestSearchData();
            requestSearchData.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_close:
                finish();
                break;
            case R.id.search_button:
                performSearch();
                break;
        }
    }

    // Requests search data
    private class RequestSearchData extends AsyncTask<Void, Void, ArrayList<SearchProfile>> {

        ArrayList<SearchProfile> tempSearchProfiles = new ArrayList<>();
        private String searchString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchString = searchEditText.getText().toString();
            tempSearchProfiles.addAll(searchProfiles);
            if (currentPage != 1) {
                //add null , so the adapter will check view_type and show progress bar at bottom
                searchProfiles.add(null);
                searchAdapter.notifyItemInserted(searchProfiles.size() - 1);
            }
        }

        @Override
        protected ArrayList<SearchProfile> doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("page", String.valueOf(currentPage))
                    .add("term", searchString)
                    .build();

            String url = "http://www.cureinstant.com/api/search/general";

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                JSONObject searchObject = new JSONObject(s);
                JSONObject suggestionsObject = searchObject.getJSONObject("suggestions");
                lastPage = suggestionsObject.getInt("last_page");
                JSONArray dataArray = suggestionsObject.getJSONArray("data");
                if (dataArray.length() < 1) {
                    return null;
                } else {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject suggestion = dataArray.getJSONObject(i);
                        int id = 0;
                        String name = "", username = "", speciality = "", picture = "";

                        id = suggestion.getInt("id");
                        name = suggestion.getString("name");
                        username = suggestion.getString("username");
                        if (!suggestion.isNull("speciality")) {
                            speciality = suggestion.getString("speciality");
                        }
                        if (!suggestion.isNull("profile_pic")) {
                            JSONObject pictureObject = suggestion.getJSONObject("profile_pic");
                            picture = pictureObject.getString("pic_name");
                        }

                        tempSearchProfiles.add(new SearchProfile(id, name, username, speciality, picture));
                    }
                    return tempSearchProfiles;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SearchProfile> newSearchProfiles) {
            super.onPostExecute(newSearchProfiles);
            progressDialog.dismiss();
            if (currentPage != 1) {
                searchProfiles.remove(searchProfiles.size() - 1); // Remove progressView
                searchAdapter.notifyItemRemoved(searchProfiles.size());
            }
            currentPage += 1;
            if (newSearchProfiles == null) {
                final Snackbar snackBar = Snackbar.make(findViewById(R.id.search_root_view), "No results found", Snackbar.LENGTH_LONG);
                snackBar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            } else {
                searchProfiles.clear();
                searchProfiles.addAll(newSearchProfiles);
                searchRecyclerView.getRecycledViewPool().clear();
                searchAdapter.setLoaded();
            }
        }
    }
}
