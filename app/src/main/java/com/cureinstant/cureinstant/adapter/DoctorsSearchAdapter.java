package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.activity.BookActivity;
import com.cureinstant.cureinstant.model.BookDoctor;
import com.cureinstant.cureinstant.util.Utilities;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 11-04-2017.
 */

// Adapter to show list of Searched doctors for booking appointments
public class DoctorsSearchAdapter extends RecyclerView.Adapter<DoctorsSearchAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<BookDoctor> bookDoctors;

    public DoctorsSearchAdapter(Context context, ArrayList<BookDoctor> bookDoctors) {
        this.context = context;
        this.bookDoctors = bookDoctors;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_doctor_profile, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final BookDoctor bookDoctor = bookDoctors.get(position);

        holder.name.setText(bookDoctor.getName());
        holder.speciality.setText(bookDoctor.getSpeciality());
        holder.workPlace.setText(bookDoctor.getDoctorWorkPlace().getName());
        holder.fee.setText(String.format(context.getString(R.string.doctor_fee), bookDoctor.getFee()));
        String imageURL = Utilities.profilePicSmallBaseUrl + bookDoctor.getPicture();
        Glide.with(context).load(imageURL).placeholder(R.drawable.doctor_placeholder).into(holder.picture);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.book_doctor_button:
                        Intent intent = new Intent(context, BookActivity.class);
                        intent.putExtra("BookDoctor", bookDoctor);
                        context.startActivity(intent);
                        break;
                }
            }
        };

        holder.bookButton.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return bookDoctors.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name, speciality, workPlace, fee;
        ImageView picture;
        Button bookButton;

        ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.book_doctor_name);
            speciality = (TextView) itemView.findViewById(R.id.book_doctor_speciality);
            workPlace = (TextView) itemView.findViewById(R.id.book_doctor_work);
            fee = (TextView) itemView.findViewById(R.id.book_doctor_fee);
            picture = (ImageView) itemView.findViewById(R.id.book_doctor_picture);
            bookButton = (Button) itemView.findViewById(R.id.book_doctor_button);
        }
    }
}
