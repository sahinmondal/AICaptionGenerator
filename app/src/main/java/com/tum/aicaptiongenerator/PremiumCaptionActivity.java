package com.tum.aicaptiongenerator;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class PremiumCaptionActivity extends AppCompatActivity {

    TextView premiumCaption;
    Button unlockButton;
    private RewardedAd rewardedAd;
    private final String adUnitId = "ca-app-pub-3940256099942544/5224354917";  // Test Rewarded Ad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_premium_caption);

        premiumCaption = findViewById(R.id.premiumCaption);
        unlockButton = findViewById(R.id.unlockButton);

        MobileAds.initialize(this, initializationStatus -> {
        });
        loadRewardedAd();

        unlockButton.setOnClickListener(v -> {
            if (rewardedAd != null) {
                rewardedAd.show(PremiumCaptionActivity.this, rewardItem -> {
                    // ইউজার রিওয়ার্ড পেয়েছে
                    premiumCaption.setText("🌟 আমি শুধু তোমারই জন্য, প্রিয়..."); // Unlocked Caption
                    unlockButton.setEnabled(false);
                    Toast.makeText(this, "Unlocked!", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, "Ad not loaded yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                Log.d("RewardedAd", "✅ Ad Loaded Successfully");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedAd = null;
                String error = loadAdError.getMessage();
                Log.e("RewardedAd", "❌ Failed to load ad: " + error);
                Toast.makeText(PremiumCaptionActivity.this, "Failed to load ad: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
