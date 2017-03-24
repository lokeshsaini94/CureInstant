package com.cureinstant.cureinstant.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cureinstant.cureinstant.R;
import com.cureinstant.cureinstant.model.Record;

import java.util.List;

/**
 * Created by lokeshsaini94 on 27-02-2017.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {

    private Context context;

    private List<Record> recordList;

    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_record, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.title.setText(record.getTitle());
        holder.icon.setImageResource(record.getIcon());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public void remove(int position) {
        recordList.remove(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView icon;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.record_title);
            icon = (ImageView) itemView.findViewById(R.id.record_icon);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            // TODO: 02-03-2017 OnClick not working!
            Log.e("AHG", "onClick: ");
            int position = getPosition();
            Record record = recordList.get(position);
            String filePath = record.getFilePath();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + filePath), "image/*");
            context.startActivity(intent);

        }
    }
}

