package me.ayomidearo.lagosjavadevs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import me.ayomidearo.lagosjavadevs.R;

public class DevProfileActivity extends AppCompatActivity {

    String username = null;
    String profileImage = null;
    String profileUrl = null;

    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareIntent();
                }
            });
        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;

        TextView linkTextView = (TextView) findViewById(R.id.profileUrl);
        ImageView profileImageView = (ImageView) findViewById(R.id.profileImage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("USERNAME");
            profileImage = getIntent().getExtras().getString("PROFILEIMAGE");
            profileUrl = getIntent().getExtras().getString("PROFILEURL");

            collapsingToolbarLayout.setTitle(username);

            linkTextView.setText(profileUrl);


            Picasso.with(DevProfileActivity.this)
                    .load(profileImage)
                    .placeholder(R.drawable.ic_empty_profile)
                    .error(R.drawable.ic_empty_profile)
                    .into(profileImageView);


        }

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startWebActivity(profileUrl);

            }
        });
    }

    private void startWebActivity(String profileUrl) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(profileUrl));
            startActivity(intent);
        }
        catch(android.content.ActivityNotFoundException e) {
            // can't start activity
        }
    }

    private void shareIntent() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer @"+username+" , "+ profileUrl+" .");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
}
