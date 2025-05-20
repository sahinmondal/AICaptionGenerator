package com.tum.aicaptiongenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CaptionDetailActivity extends AppCompatActivity {

    TextView captionTextView;
    Button copyButton, shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_caption_detail);

        captionTextView = findViewById(R.id.captionText);
        copyButton = findViewById(R.id.copyButton);
        shareButton = findViewById(R.id.shareButton);

        String caption = getIntent().getStringExtra("caption");
        captionTextView.setText(caption);

        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("caption", caption);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(CaptionDetailActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
        });

        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, caption);
            startActivity(Intent.createChooser(shareIntent, "Share caption via"));
        });
    }
}