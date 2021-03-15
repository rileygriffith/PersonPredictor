package com.example.personpredictor;

import lecture.l01_android_simple_buttons.utilities.NetworkUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    // Declare UI elements
    TextView mPersonResults;
    EditText mSearchNameBox;
    Button mSearchButton;
    Button mHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        // Connect variables to UI elements
        mPersonResults = (TextView) findViewById(R.id.tv_display_person);
        mSearchNameBox = (EditText) findViewById(R.id.et_name_box);
        mSearchButton = (Button) findViewById(R.id.predict_button);
        mHistoryButton = (Button) findViewById(R.id.history_button);

        // Response to Search Button
        mSearchButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        String searchText = mSearchNameBox.getText().toString();
                        // get age info
                        String ageUrl = "https://api.agify.io/?name=" + searchText;
                        String ageResponseString = null;
                        try {
                            ageResponseString = getResponseFromUrl(ageUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String age = parseJson(ageResponseString);
                        // get gender info
                        String genderUrl = "https://api.genderize.io?name=" + searchText;
                        String genderResponseString = null;
                        try{
                            genderResponseString = getResponseFromUrl(genderUrl);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        String gender = parseJson(genderResponseString);
                        // get nationality info
                        String nationalUrl = "https://api.nationalize.io?name=" + searchText;
                        String nationalResponseString = null;
                        try{
                            nationalResponseString = getResponseFromUrl(nationalUrl);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        String nationality = parseJson(nationalResponseString);
                        Locale l = new Locale("", nationality);
                        String output = searchText + ": most likely " + age + " years old, " + gender + ", and from " + l.getDisplayCountry();
                        mPersonResults.setText(output);
                        // Save data in text file for other activity
                        FileWriter writer = null;
                        try {
                            String filePath = getApplicationInfo().dataDir;
                            writer = new FileWriter(filePath + "/save.txt", true);
                            writer.append(output + "\n");
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static String parseJson(String responseString){
        try{
            Log.d("information", responseString);
            JSONObject jsonObject = new JSONObject(responseString);
            // check if age, gender, or nationality
            if(jsonObject.has("age")) return jsonObject.getString("age");
            if(jsonObject.has("gender")) return jsonObject.getString("gender");
            if(jsonObject.has("country")) return jsonObject.getJSONArray("country").getJSONObject(0).getString("country_id");
        }catch(JSONException e){
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    // Go to history page
    public void goToHistoryActivity(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    // get URL response
    public static String getResponseFromUrl(String u) throws IOException{
        URL url = new URL(u);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A"); // delimiter for end of message
            boolean hasInput = scanner.hasNext();
            if(hasInput) return scanner.next(); // success
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return "wack";
    }
}