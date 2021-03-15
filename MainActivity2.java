package com.example.personpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MainActivity2 extends AppCompatActivity {
    // Declare UI elements
    TextView mHistoryDisplay;
    Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Connect variables with UI
        mHistoryDisplay = (TextView) findViewById(R.id.tv_display_history);
        mBackButton = (Button) findViewById(R.id.back_button);

        BufferedReader br = null;
        try {
            String filePath = getApplicationInfo().dataDir;
            br = new BufferedReader(new FileReader(filePath + "/save.txt"));
            String line = null;
            while((line = br.readLine()) != null){
                mHistoryDisplay.append(line + "\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Go back to main
    public void goBackToMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}