package com.example.cooksnest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class filtersActivity extends AppCompatActivity {

    public ListAdapter filtersAdapter;
    private static final String[] FILTERS = new String[] {
            "#french", "#Italian", "#Curry", "#Japanese", "#Spanish", "#Korean", "#American", "#finland"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ArrayList<String> filteredTags = new ArrayList<String>();
        browse.filters.addAll(filteredTags);
        filtersAdapter = new ListAdapter(this, R.layout.listadapter, filteredTags);
        AutoCompleteTextView filtersView = (AutoCompleteTextView) this.findViewById(R.id.filtersAutoComplete2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, FILTERS);
        filtersView.setAdapter(adapter);
        ImageButton exit = this.findViewById(R.id.exit_completed2);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        Button applyFilter = this.findViewById(R.id.filterApplication);
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        String filterString = browse.filters.toString();
        filterString = filterString.substring(1, filterString.length() - 1);
        String fullFiltersText = "Filters: " + filterString;
        filtersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("HI","FROM ONITEMCLICK");
                String selected = (String) parent.getItemAtPosition(position);
                if(browse.filters.contains(selected)){
                    Toast.makeText(getApplicationContext(), "You already added this hashtag!",
                            Toast.LENGTH_SHORT).show();
                    filtersView.setText("");
                    return;
                }
                int pos = Arrays.asList(FILTERS).indexOf(selected);
                Log.d(String.valueOf(pos), selected);
                filtersView.setText("");
                browse.filters.add(selected);
                filtersAdapter.add(selected);
                filtersAdapter.notifyDataSetChanged();
            }
        });
    }
}
