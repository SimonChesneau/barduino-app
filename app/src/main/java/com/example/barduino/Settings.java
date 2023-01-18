package com.example.barduino;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.barduino.bottle.BottleView;
import com.google.gson.Gson;

import java.util.Set;


public class Settings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button backbtn = (Button)findViewById(R.id.back); //Create a button to finish the activity
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //Finishing the activity of the button is pressed


        ImageButton bottlesettingsbtn = (ImageButton)findViewById(R.id.bottle); //Creating a bottle button to start a new activity
        bottlesettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, BottleView.class));
/**                Intent i = new Intent (Settings.this, BottleView.class);
                Intent intent = getIntent();
                String bottlelist = intent.getStringExtra("bottleList");

                i.putExtra("bottleList", bottlelist);
                startActivity(i);**/
            }
        }); //Start a new activity based on this one and the bottleView activity
    }
}
