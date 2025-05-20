package com.tum.aicaptiongenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class AudioChanger extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private EditText messageEditText;
    private Spinner languageSpinner, voiceTypeSpinner;
    private Button generateButton, shareButton, downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_audio_changer);


        // Initialize views
        messageEditText = findViewById(R.id.messageEditText);
        languageSpinner = findViewById(R.id.languageSpinner);
        voiceTypeSpinner = findViewById(R.id.voiceTypeSpinner);
        generateButton = findViewById(R.id.generateButton);
        shareButton = findViewById(R.id.shareButton);
        downloadButton = findViewById(R.id.downloadButton);

        // Setup language spinner (Bangla, English)
        String[] languages = {"Bangla", "English"};
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        // Setup voice type spinner (Male, Female)
        String[] voiceTypes = {"Male", "Female"};
        ArrayAdapter<String> voiceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, voiceTypes);
        voiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        voiceTypeSpinner.setAdapter(voiceAdapter);

        // Initialize Text-to-Speech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set default language to English
                    textToSpeech.setLanguage(Locale.US);
                } else {
                    Toast.makeText(AudioChanger.this, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle generate button click
        generateButton.setOnClickListener(view -> {
            String message = messageEditText.getText().toString();
            String selectedLanguage = languageSpinner.getSelectedItem().toString();
            String selectedVoice = voiceTypeSpinner.getSelectedItem().toString();

            // Set Language
            if (selectedLanguage.equals("Bangla")) {
                textToSpeech.setLanguage(new Locale("bn", "BD"));
            } else {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }

            // Set Voice Type
            if (selectedVoice.equals("Female")) {
                textToSpeech.setPitch(0.2f); // Female voice (lower pitch)
            } else {
                textToSpeech.setPitch(1.1f); // Male voice (higher pitch)
            }

            // Speak the message
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        // Handle share button click
        shareButton.setOnClickListener(view -> {
            String message = messageEditText.getText().toString();
            File audioFile = generateAudioFile(message);
            shareAudio(audioFile);
        });

        // Handle download button click
        downloadButton.setOnClickListener(view -> {
            String message = messageEditText.getText().toString();
            File audioFile = generateAudioFile(message);
            downloadAudio(audioFile);
        });
    }

    // Method to generate audio file
    private File generateAudioFile(String message) {
        File audioFile = new File(getExternalFilesDir(null), "voice_message.wav");

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "voiceMessage");

        textToSpeech.synthesizeToFile(message, params, audioFile, "voiceMessage");

        return audioFile;
    }

    // Method to share audio file
    private void shareAudio(File audioFile) {
        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                audioFile
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share Voice Message"));
    }


    // Method to download audio file
    private void downloadAudio(File audioFile) {
        // Save the file to the device's storage (just for demo purposes)
        try {
            File downloadDir = new File(getExternalFilesDir(null), "Downloads");
            if (!downloadDir.exists()) {
                downloadDir.mkdir();
            }
            File downloadFile = new File(downloadDir, "voice_message.wav");
            audioFile.renameTo(downloadFile);
            Toast.makeText(this, "Audio file downloaded: " + downloadFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown TTS when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}