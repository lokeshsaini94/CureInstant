package com.cureinstant.cureinstant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class AddRecordActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private int iconSelected = R.drawable.ic_appointments;
    private TextView imageAttached;
    private ImageButton imageButton;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private EditText recordTitle;
    private Button addRecord;
    private ImageButton closeAddRecord;
    private File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        // TODO: 28-02-2017 Handle permission requests for storing records.

        imageAttached = (TextView) findViewById(R.id.add_record_image_text);
        imageButton = (ImageButton) findViewById(R.id.add_record_image_button);
        icon1 = (ImageView) findViewById(R.id.add_record_icon1);
        icon2 = (ImageView) findViewById(R.id.add_record_icon2);
        icon3 = (ImageView) findViewById(R.id.add_record_icon3);
        iconSelected = R.drawable.ic_appointments;
        recordTitle = (EditText) findViewById(R.id.add_record_title_et);
        addRecord = (Button) findViewById(R.id.add_record_button);
        closeAddRecord = (ImageButton) findViewById(R.id.close_add_record);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.add_record_icon1:
                        iconSelected = R.drawable.ic_appointments;
                        icon1.setAlpha((float) 1);
                        icon2.setAlpha((float) 0.5);
                        icon3.setAlpha((float) 0.5);
                        break;
                    case R.id.add_record_icon2:
                        iconSelected = R.drawable.ic_records;
                        icon1.setAlpha((float) 0.5);
                        icon2.setAlpha((float) 1);
                        icon3.setAlpha((float) 0.5);
                        break;
                    case R.id.add_record_icon3:
                        iconSelected = R.drawable.ic_notifications;
                        icon1.setAlpha((float) 0.5);
                        icon2.setAlpha((float) 0.5);
                        icon3.setAlpha((float) 1);
                        break;
                    case R.id.add_record_image_button:

                        // TODO: 28-02-2017 Add camera intent with thumbnail
                        // http://stackoverflow.com/questions/19648957/take-photo-w-camera-intent-and-display-in-imageview-or-textview

                        file = Utilities.createNewRecordFile();
                        Uri outputFileUri = Uri.fromFile(file);

                        Intent pickIntent = new Intent();
                        pickIntent.setType("image/*");
                        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                        pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
                        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                        chooserIntent.putExtra
                                (
                                        Intent.EXTRA_INITIAL_INTENTS,
                                        new Intent[]{takePhotoIntent}
                                );

                        startActivityForResult(chooserIntent, SELECT_PICTURE);
                        break;
                    case R.id.add_record_button:
                        String title = recordTitle.getText().toString();
                        if (title.isEmpty()) {
                            recordTitle.setError(getString(R.string.error_field_required));
                        } else {
                            int icon = iconSelected;
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("title", title);
                            returnIntent.putExtra("icon", icon);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                        break;
                    case R.id.close_add_record:
                        finish();
                        break;

                }
            }
        };

        addRecord.setOnClickListener(onClickListener);
        icon1.setOnClickListener(onClickListener);
        icon2.setOnClickListener(onClickListener);
        icon3.setOnClickListener(onClickListener);
        imageButton.setOnClickListener(onClickListener);
        closeAddRecord.setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else if (requestCode == SELECT_PICTURE) {
            imageAttached.setText("Record attached");
        }
    }

}
