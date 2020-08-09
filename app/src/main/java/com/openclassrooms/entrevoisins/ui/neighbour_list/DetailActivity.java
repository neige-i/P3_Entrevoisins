package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.favourite)
    FloatingActionButton mFavourite;

    private static final String NEIGHBOUR_EXTRA = "NEIGHBOUR_EXTRA";

    private NeighbourApiService mApiService;
    private Neighbour mNeighbour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mApiService = DI.getNeighbourApiService();

        mNeighbour = (Neighbour) getIntent().getSerializableExtra(NEIGHBOUR_EXTRA);
        initUi();
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

    private void initUi() {
        setTitle(mNeighbour.getName());

        Glide.with(this)
                .load(mNeighbour.getAvatarUrl())
                .into(mAvatar);
        mName.setText(mNeighbour.getName());
        mAddress.setText(mNeighbour.getAddress());
        mPhoneNumber.setText(mNeighbour.getPhoneNumber());
        mWebsite.setText(mNeighbour.getWebsiteUrl());
        mAboutMe.setText(mNeighbour.getAboutMe());
        mFavourite.setImageResource(mNeighbour.isFavourite() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp);
    }

    @OnClick(R.id.favourite)
    void setFavourite() {
        int imageResourceId = mNeighbour.isFavourite() ? R.drawable.ic_star_border_white_24dp : R.drawable.ic_star_white_24dp;
        mFavourite.setImageResource(imageResourceId);
        mFavourite.setTag(imageResourceId);
        mApiService.toggleFavourite(mNeighbour);
    }

    /** Used to navigate to this activity */
    public static void navigate(FragmentActivity activity, Neighbour neighbour) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(NEIGHBOUR_EXTRA, neighbour);
        ActivityCompat.startActivity(activity, intent, null);
    }
}