package com.cureinstant.cureinstant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    int[] mResources = {
            R.drawable.cure_instant_logo,
            R.drawable.cure_instant_logo,
            R.drawable.cure_instant_logo
    };
    ViewPager viewPager;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signUpButton = (Button) findViewById(R.id.signup_button);
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.new_user_pager);
        adapter = new CustomPagerAdapter(WelcomeActivity.this, mResources);
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.isLoggedIn(getApplicationContext())) {
            Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_button:
                Intent login = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(login);
                break;
            case R.id.signup_button:
                Intent signup = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(signup);
                break;
        }

    }

    private class CustomPagerAdapter extends PagerAdapter {

        Context context;
        int[] mResources;
        LayoutInflater inflater;

        CustomPagerAdapter(Context context, int[] mResources) {
            this.context = context;
            this.mResources = mResources;
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView image;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.layout_welcome_viewpager, container,
                    false);

            image = (ImageView) itemView.findViewById(R.id.image);
            // Capture position and set to the ImageView
            image.setImageResource(mResources[position]);

            // Add viewpager_item.xml to ViewPager
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            container.removeView((ImageView) object);

        }
    }
}
