package com.cureinstant.cureinstant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.cureinstant.cureinstant.util.Utilities.accessTokenValue;

public class NewQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("image/jpeg");
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;
    private static boolean isUploading = false;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private EditText questionTitle;
    private EditText questionDesc;
    private Switch questionDescSwitch;
    private Button questionButton;
    private View attachmentContainer;
    private TextView attachmentCount;
    private ArrayList<String> images = new ArrayList<>();

    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(R.string.ask_question);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bottomSheetLayout.setPeekOnDismiss(true);
        bottomSheetLayout.setPeekSheetTranslation(Utilities.pxFromDp(getApplicationContext(), 275));
        questionTitle = (EditText) findViewById(R.id.question_title);
        questionDesc = (EditText) findViewById(R.id.question_desc);
        questionDescSwitch = (Switch) findViewById(R.id.question_desc_switch);
        questionButton = (Button) findViewById(R.id.question_button);
        attachmentContainer = findViewById(R.id.attachment_container);
        attachmentCount = (TextView) findViewById(R.id.attachment_tv);

        questionButton.setOnClickListener(this);
        attachmentContainer.setOnClickListener(this);

        questionDescSwitch.setChecked(false);
        questionDescSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    questionDesc.setVisibility(View.VISIBLE);
                } else {
                    questionDesc.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_button:
                if (isUploading) {
                    Toast.makeText(this, "Please wait! Uploading Image", Toast.LENGTH_SHORT).show();
                } else {
                    String title = questionTitle.getText().toString();
                    String desc = questionDesc.getText().toString();
                    if (title.isEmpty()) {
                        questionTitle.setError(getString(R.string.error_field_required));
                    } else {
                        PostQuestion postQuestion = new PostQuestion(title, desc, images);
                        postQuestion.execute();
                    }
                }
                break;
            case R.id.attachment_container:
                showSheetView();
                break;
        }
    }

    // TODO: 30-03-2017 Handle WRITE_EXTERNAL_STORAGE and CAMERA permissions
    // Displays ImagePicker Bottom Sheet
    private void showSheetView() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(30)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(NewQuestionActivity.this)
                                .load(imageUri)
                                .centerCrop()
                                .crossFade()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        bottomSheetLayout.dismissSheet();
                        if (selectedTile.isCameraTile()) {
                            dispatchTakePictureIntent();
                        } else if (selectedTile.isPickerTile()) {
                            startActivityForResult(createPickIntent(), REQUEST_LOAD_IMAGE);
                        } else if (selectedTile.isImageTile()) {
                            UploadImage uploadImage = new UploadImage(selectedTile.getImageUri());
                            uploadImage.execute();
                        } else {
                            genericError();
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        bottomSheetLayout.showWithSheetView(sheetView);
    }

    /**
     * For images captured from the camera, we need to create a File first to tell the camera
     * where to store the image.
     *
     * @return the File created for the image to be store under.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }

    /**
     * This checks to see if there is a suitable activity to handle the `ACTION_PICK` intent
     * and returns it if found. {@link Intent#ACTION_PICK} is for picking an image from an external app.
     *
     * @return A prepared intent if found.
     */
    @Nullable
    private Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    /**
     * This checks to see if there is a suitable activity to handle the {@link MediaStore#ACTION_IMAGE_CAPTURE}
     * intent and returns it if found. {@link MediaStore#ACTION_IMAGE_CAPTURE} is for letting another app take
     * a picture from the camera and store it in a file that we specify.
     *
     * @return A prepared intent if found.
     */
    @Nullable
    private Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    /**
     * This utility function combines the camera intent creation and image file creation, and
     * ultimately fires the intent.
     *
     * @see {@link #createCameraIntent()}
     * @see {@link #createImageFile()}
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = createCameraIntent();
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent != null) {
            // Create the File where the photo should go
            try {
                File imageFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                // Error occurred while creating the File
                genericError("Could not create imageFile for camera");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;
            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {
                selectedImage = Uri.parse(getRealPathFromUri(this, data.getData()));
                if (selectedImage == null) {
                    genericError();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Do something with imagePath
                selectedImage = cameraImageUri;
            }

            if (selectedImage != null) {
                UploadImage uploadImage = new UploadImage(selectedImage);
                uploadImage.execute();
            } else {
                genericError();
            }
        }
    }

    private File getFileFrom(Uri selectedImageUri) {
        return new File(selectedImageUri.getPath());
    }

    private void genericError() {
        genericError(null);
    }

    private void genericError(String message) {
        Toast.makeText(this, message == null ? "Something went wrong!" : message, Toast.LENGTH_SHORT).show();
    }

    private void returnIntent() {
        Intent data = new Intent();
        data.putExtra("newQuestionDone", true);
        setResult(RESULT_OK, data);
        finish();
    }

    // AsyncTask to upload images for a new question
    private class UploadImage extends AsyncTask<Void, Void, String> {

        private File file;

        UploadImage(Uri selectedImageUri) {
            file = getFileFrom(selectedImageUri);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (file.exists()) {
                isUploading = true;
                Toast.makeText(getApplicationContext(), "Uploading Image", Toast.LENGTH_SHORT).show();
                attachmentCount.setText("Uploading Image");
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Image", Toast.LENGTH_SHORT).show();
                this.cancel(true);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "androidAppUpload.jpeg", RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                    .build();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/question/upload/image")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(requestBody)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject resultsObject = new JSONObject(result);
                String status = resultsObject.getString("success");
                if (status.equals("success")) {
                    return resultsObject.getString("file");
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                images.add(s);
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
            isUploading = false;
            attachmentCount.setText(String.format(getString(R.string.attachments_count), images.size()));
        }
    }

    // AsyncTask to post a new question
    private class PostQuestion extends AsyncTask<Void, Void, Boolean> {

        String title;
        String desc;
        ArrayList<String> images;

        PostQuestion(String title, String desc, ArrayList<String> images) {
            this.title = title;
            this.desc = desc;
            this.images = images;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("question", title)
                    .add("question_description", desc);

            if (!images.isEmpty()) {
                for (int i = 0; i < images.size(); i++) {
                    formBuilder.add("image[" + (i) + "]", images.get(i));
                }
            }

            RequestBody body = formBuilder.build();

            Request request = new Request.Builder()
                    .url("http://www.cureinstant.com/api/question/submit")
                    .header("Authorization", "Bearer " + accessTokenValue)
                    .post(body)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject feedJson = new JSONObject(result);
                String status = feedJson.getString("success");
                return status.equals("success");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                returnIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
