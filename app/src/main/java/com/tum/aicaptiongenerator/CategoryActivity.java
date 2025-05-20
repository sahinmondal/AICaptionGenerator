package com.tum.aicaptiongenerator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ListView captionListView;
    private AdView mAdView;
    List<String> captionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        captionListView = findViewById(R.id.captionList);
        String category = getIntent().getStringExtra("category");

        loadCaptionsFromJSON(category);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, captionList);
        captionListView.setAdapter(adapter);

        captionListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(CategoryActivity.this, CaptionDetailActivity.class);
            intent.putExtra("caption", captionList.get(i));
            startActivity(intent);
        });
    }

    private void loadCaptionsFromJSON(String category) {
        try {
            InputStream is = getAssets().open("captions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(category);
            for (int i = 0; i < jsonArray.length(); i++) {
                captionList.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
