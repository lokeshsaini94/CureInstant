package com.cureinstant.cureinstant.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cureinstant.cureinstant.R;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, BottomSheetDatePickerDialog.OnDateSetListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView profilePicture;
    private TextView name;
    private TextView dob;
    private TextView sex;
    private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePicture = (ImageView) findViewById(R.id.user_picture);
        name = (TextView) findViewById(R.id.user_name);
        dob = (TextView) findViewById(R.id.user_dob);
        sex = (TextView) findViewById(R.id.user_sex);
        number = (TextView) findViewById(R.id.user_number);
        ImageButton closeProfile = (ImageButton) findViewById(R.id.profile_close);
        profilePicture.setOnClickListener(this);
        name.setOnClickListener(this);
        dob.setOnClickListener(this);
        sex.setOnClickListener(this);
        number.setOnClickListener(this);
        closeProfile.setOnClickListener(this);
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
            case R.id.user_number:
                numberDialog();
                break;
            case R.id.profile_close:
                finish();
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

    private void numberDialog() {
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setTextColor(getResources().getColor(R.color.colorPrimary));
        edittext.setInputType(InputType.TYPE_CLASS_PHONE);
        edittext.setText(number.getText().toString());

        AlertDialog.Builder numberDialog = new AlertDialog.Builder(this);
        numberDialog.setTitle("Enter Your Number");
        numberDialog.setView(edittext);

        numberDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextValue = edittext.getText().toString();
                number.setText(editTextValue);
            }
        });

        numberDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        numberDialog.show();
    }

    private void sexDialog() {
        AlertDialog.Builder sexDialog = new AlertDialog.Builder(this);
        sexDialog.setTitle("Select Your Sex");

        sexDialog.setPositiveButton("Male", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sex.setText("Male");
            }
        });

        sexDialog.setNegativeButton("Female", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sex.setText("Female");
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
