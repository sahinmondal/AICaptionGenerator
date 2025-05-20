package com.tum.aicaptiongenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {

    String[] categories = {"Love", "Attitude", "Sad", "Funny", "Birthday", "Friendship", "Motivational", "EidMubarak", "Story", };
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Button audiobtn = findViewById(R.id.audiobtn);

        ListView listView = findViewById(R.id.categoryList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category_list_item, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            intent.putExtra("category", categories[i].toLowerCase());
            startActivity(intent);
        });

audiobtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, AudioChanger.class));
    }
});

    }
}
