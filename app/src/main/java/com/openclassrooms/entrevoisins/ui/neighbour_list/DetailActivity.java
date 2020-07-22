package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.addressText)
    TextView mAddress;
    @BindView(R.id.phoneNumberText)
    TextView mPhoneNumber;
    @BindView(R.id.websiteText)
    TextView mWebsite;
    @BindView(R.id.aboutMeContent)
    TextView mAboutMe;

    private static final String NEIGHBOUR_EXTRA = "NEIGHBOUR_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Neighbour neighbour = (Neighbour) getIntent().getSerializableExtra(NEIGHBOUR_EXTRA);
        setTitle(neighbour.getName());
        init(neighbour);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(Neighbour neighbour) {
        Glide.with(this)
                .load(neighbour.getAvatarUrl())
                .into(mAvatar);
        mName.setText(neighbour.getName());
        mAddress.setText(neighbour.getAddress());
        mPhoneNumber.setText(neighbour.getPhoneNumber());
        mAboutMe.setText(neighbour.getAboutMe());
    }

    /** Used to navigate to this activity */
    public static void navigate(FragmentActivity activity, Neighbour neighbour) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(NEIGHBOUR_EXTRA, neighbour);
        ActivityCompat.startActivity(activity, intent, null);
    }
}