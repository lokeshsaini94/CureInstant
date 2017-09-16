package com.cureinstant.cureinstant.adapter.doctorDetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.doctorDetails.DoctorAchievement;
import com.cureinstant.cureinstant.util.Utilities;
import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

// Adapter to show list of Achievements of a doctor
public class DoctorAchievementsAdapter extends RecyclerView.Adapter<DoctorAchievementsAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<DoctorAchievement> doctorAchievement;

    public DoctorAchievementsAdapter(Context context, ArrayList<DoctorAchievement> doctorAchievement) {
        this.context = context;
        this.doctorAchievement = doctorAchievement;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_doctor_achievement, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
        viewHolder.title.setText(doctorAchievement.get(position).getTitle());
        viewHolder.content.setText(doctorAchievement.get(position).getDesc());
        String imageURL = Utilities.doctorAchievementBaseUrl + doctorAchievement.get(position).getImage();
        Glide.with(context).load(imageURL).thumbnail(0.1f).placeholder(R.drawable.doctor_placeholder).into(viewHolder.image);
        final String finalImageURL = imageURL;
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("loading...");

                // Saves image is ExternalCacheDir and opens a viewer intent
                new AsyncTask<Void, Void, Uri>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pd.show();
                    }

                    @Override
                    protected Uri doInBackground(Void... params) {
                        Bitmap theBitmap = null;
                        try {
                            theBitmap = Glide.
                                    with(context)
                                    .load(finalImageURL)
                                    .asBitmap()
                                    .into(-1, -1)
                                    .get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            FirebaseCrash.report(e);
                        }

                        Uri fileUri;

                        // Generating file name
                        String imageName = doctorAchievement.get(viewHolder.getAdapterPosition()).getImage();

                        // Creating image here
                        File image = new File(context.getExternalCacheDir(), imageName);
                        fileUri = Uri.fromFile(image);

                        BufferedOutputStream out;
                        try {
                            out = new BufferedOutputStream(new FileOutputStream(image));
                            if (theBitmap != null) {
                                theBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            }
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            FirebaseCrash.report(e);
                        }

                        return fileUri;
                    }

                    @Override
                    protected void onPostExecute(Uri uri) {
                        super.onPostExecute(uri);
                        pd.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "image/*");
                        context.startActivity(intent);
                    }
                }.execute();
            }
        });

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doctorAchievement.get(viewHolder.getAdapterPosition()).getLink()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorAchievement.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;
        private ImageView image;
        private View rootView;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.doctor_achievement_item_title);
            content = (TextView) itemView.findViewById(R.id.doctor_achievement_item_content);
            image = (ImageView) itemView.findViewById(R.id.doctor_achievement_item_image);
            rootView = itemView.findViewById(R.id.doctor_achievement_item_root_view);
        }
    }
}
