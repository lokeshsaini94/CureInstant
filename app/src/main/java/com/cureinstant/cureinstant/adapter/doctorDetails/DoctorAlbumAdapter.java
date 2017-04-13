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

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.util.Utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by lokeshsaini94 on 03-04-2017.
 */

// Adapter to show list of Photos(album) of a doctor
public class DoctorAlbumAdapter extends RecyclerView.Adapter<DoctorAlbumAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<String> album;

    public DoctorAlbumAdapter(Context context, ArrayList<String> album) {
        this.context = context;
        this.album = album;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_images, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        String imageURL = Utilities.doctorAlbumBaseUrl;
        imageURL += album.get(holder.getAdapterPosition());
        Glide.with(context).load(imageURL).thumbnail(0.1f).placeholder(R.drawable.doctor_placeholder).into(holder.imageView);
        final String finalImageURL = imageURL;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("loading...");
                pd.show();

                // Saves image is ExternalCacheDir and opens a viewer intent
                new AsyncTask<Void, Void, Uri>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
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
                        }

                        Uri fileUri;

                        // Generating file name
                        String imageName = album.get(holder.getAdapterPosition());

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
    }

    @Override
    public int getItemCount() {
        return album.size();
    }

    public void remove(int position) {
        album.remove(position);
        notifyItemRemoved(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.post_image);
        }
    }
}