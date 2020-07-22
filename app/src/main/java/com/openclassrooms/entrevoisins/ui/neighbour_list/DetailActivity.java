package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

public class DetailActivity extends AppCompatActivity {

    private static final String NEIGHBOUR_EXTRA = "NEIGHBOUR_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Neighbour neighbour = (Neighbour) getIntent().getSerializableExtra(NEIGHBOUR_EXTRA);
        setTitle(neighbour.getName());
        System.out.println(neighbour.getId() + " / " + neighbour.getName() + " / " +
                neighbour.getAddress() + " / " + neighbour.getPhoneNumber());
    }

    /** Used to navigate to this activity */
    public static void navigate(FragmentActivity activity, Neighbour neighbour) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(NEIGHBOUR_EXTRA, neighbour);
        ActivityCompat.startActivity(activity, intent, null);
    }
}