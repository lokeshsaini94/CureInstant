package com.cureinstant.cureinstant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.User;
import com.cureinstant.cureinstant.util.Utilities;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetDatePickerDialog.OnDateSetListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView profilePicture;
    private TextView name;
    private TextView dob;
    private TextView sex;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        User userInfo = i.getParcelableExtra("user_info");

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(userInfo.getName());
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profilePicture = (ImageView) findViewById(R.id.user_picture);
        name = (TextView) findViewById(R.id.user_name);
        dob = (TextView) findViewById(R.id.user_dob);
        sex = (TextView) findViewById(R.id.user_sex);
        email = (TextView) findViewById(R.id.user_email);
        profilePicture.setOnClickListener(this);
        name.setOnClickListener(this);
        dob.setOnClickListener(this);
        sex.setOnClickListener(this);
        email.setOnClickListener(this);

        name.setText(userInfo.getName());
        dob.setText(userInfo.getDob());
        sex.setText(userInfo.getSex());
        email.setText(userInfo.getEmail());
        Glide.with(this).load(Utilities.profilePicSmallBaseUrl + userInfo.getPicture()).into(profilePicture);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_picture:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.user_name:
                nameDialog();
                break;
            case R.id.user_dob:
                dateDialog();
                break;
            case R.id.user_sex:
                sexDialog();
                break;
            case R.id.user_email:
                emailDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(imageBitmap);
        }
    }

    private void nameDialog() {
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setTextColor(getResources().getColor(R.color.colorPrimary));
        edittext.setText(name.getText().toString());

        AlertDialog.Builder nameDialog = new AlertDialog.Builder(this);
        nameDialog.setTitle("Enter Your Name");
        nameDialog.setView(edittext);

        nameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextValue = edittext.getText().toString();
                name.setText(editTextValue);
            }
        });

        nameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        nameDialog.show();
    }

    private void emailDialog() {
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setTextColor(getResources().getColor(R.color.colorPrimary));
        edittext.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edittext.setText(email.getText().toString());

        AlertDialog.Builder emailDialog = new AlertDialog.Builder(this);
        emailDialog.setTitle("Enter Your Email");
        emailDialog.setView(edittext);

        emailDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextValue = edittext.getText().toString();
                email.setText(editTextValue);
            }
        });

        emailDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        emailDialog.show();
    }

    private void sexDialog() {
        AlertDialog.Builder sexDialog = new AlertDialog.Builder(this);
        sexDialog.setTitle("Select Your Sex");

        sexDialog.setPositiveButton("Male", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sex.setText("MALE");
            }
        });

        sexDialog.setNegativeButton("Female", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sex.setText("FEMALE");
            }
        });

        sexDialog.show();
    }

    private void dateDialog() {
        Calendar now = Calendar.getInstance();
        BottomSheetDatePickerDialog date = BottomSheetDatePickerDialog.newInstance(
                ProfileActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        date.show(getSupportFragmentManager(), "DOB");
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }
}
