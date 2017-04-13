package com.cureinstant.cureinstant.adapter;

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
 * Created by lokeshsaini94 on 22-03-2017.
 */

// Adapter to show list of images in a given feed Item
public class FeedImagesAdapter extends RecyclerView.Adapter<FeedImagesAdapter.ItemViewHolder> {

    private Context context;
    private String type;
    private ArrayList<String> images;

    public FeedImagesAdapter(Context context, String type, ArrayList<String> images) {
        this.context = context;
        this.type = type;
        this.images = images;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_images, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        String imageURL = "";
        switch (type) {
            case "BLOG":
                imageURL = Utilities.blogImageBaseUrl;
                break;
            case "POST":
                imageURL = Utilities.postImageBaseUrl;
                break;
            case "QUERY":
                imageURL = Utilities.questionImageBaseUrl;
                break;
        }
        imageURL += images.get(holder.getAdapterPosition());
        Glide.with(context).load(imageURL).thumbnail(0.1f).placeholder(R.drawable.doctor_placeholder).into(holder.imageView);
        final String finalImageURL = imageURL;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
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
                        }

                        Uri fileUri;

                        // Generating file name
                        String imageName = images.get(holder.getAdapterPosition());

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
        return images.size();
    }

    public void remove(int position) {
        images.remove(position);
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