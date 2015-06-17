package com.fuhu.iot_speech_recognition_test;


import java.util.ArrayList;
import java.util.Locale;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 1234;
    Button Start;
    TextView Speech;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;
    ImageView Lightbulb;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //speech recognition
        setContentView(R.layout.activity_main);
        Start = (Button)findViewById(R.id.start_reg);
        Speech = (TextView)findViewById(R.id.speech);
        Lightbulb=(ImageView)findViewById(R.id.lightbulb);

        Start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }}

        });
    }

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            matches_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //Show potential result
            match_text_dialog = new Dialog(MainActivity.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = (ListView)match_text_dialog.findViewById(R.id.list);

            ArrayAdapter<String> adapter =    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String cmd = matches_text.get(position);
                    if(cmd.equals("开灯") || cmd.contains("light on")) {
                        System.out.println("on");
                        Lightbulb.setImageResource(R.drawable.lighton);
                    }
                    if(cmd.equals("关灯") || cmd.contains("light off")) {
                        System.out.println("off");
                        Lightbulb.setImageResource(R.drawable.lightoff);
                    }

                    match_text_dialog.hide();
                }
            });
            match_text_dialog.show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

