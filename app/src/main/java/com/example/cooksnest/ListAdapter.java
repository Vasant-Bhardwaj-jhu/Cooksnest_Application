package com.example.cooksnest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    public ArrayList<String> Hashtags;
    int res;
    Context cntx;

    public ListAdapter(@NonNull Context context, int resource, ArrayList<String> hashtags) {
        super(context, resource,hashtags);
        Hashtags = hashtags;
        cntx = context;
        res = resource;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        String it =getItem(position);
        Log.d("listadapter", it);
        LinearLayout itemView;
        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }
        TextView Hashview = (TextView) itemView.findViewById(R.id.HashViewSingle);
        Hashview.setText(it);
        Button removeHashtag = itemView.findViewById(R.id.removeHashtag);
        removeHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hashtags.remove(it);
                notifyDataSetChanged();
            }
        });

        return itemView;
    }

    public ArrayList<String> getHashtags() {
        return Hashtags;
    }
}
