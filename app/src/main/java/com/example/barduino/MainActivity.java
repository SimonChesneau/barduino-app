package com.example.barduino;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.barduino.bottle.Bottle;
import com.example.barduino.bottle.BottleList;
import com.example.barduino.database.MyDatabase;
import com.example.barduino.drink.Drink;
import com.example.barduino.drink.DrinkAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private final ArrayList<Drink> drinks = new ArrayList<Drink>();
    private final ArrayList<Bottle> bottles = new ArrayList<Bottle>();
    private BottleList bottleList = null;
    private MyDatabase myDatabase= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = MyDatabase.getInstance(this); //Retrieving a database instance

        // TODO: This part is Hardcoded and needs to be done using a database
        setUpBottles(); //Create all the bottles and add them to the list
        setUpDrinks(); //Create all the drinks and add them to the list

        setUpDrinksLayout(); //Add all the drinks to the layout and their onClick actions
        createSettingsButton(); //Add the settings button on the main screen

    }

    private void createSettingsButton() {
        ImageButton settingsbtn = (ImageButton)findViewById(R.id.settings);
        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 Intent i = new Intent (MainActivity.this, Settings.class);
                 String s = new Gson().toJson(bottleList);
                 i.putExtra("bottleList", s);
                 startActivity(i);
                 **/
                // TODO : Finish the proper implementation of shared prefs
                // change shared pref key
                SharedPreferences mPrefs = getSharedPreferences("your_sp_key", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String json = new Gson().toJson(bottleList);
                prefsEditor.putString("someName", json).apply();
                startActivity(new Intent(MainActivity.this, Settings.class));

            }
        });
    }

    private void setUpDrinksLayout() {
        DrinkAdapter adapter = new DrinkAdapter(this, drinks); //Create a new DrinkAdapter based on the drinks list
        ListView list = (ListView)findViewById(R.id.list); //Find the layout with the id "list"
        list.setAdapter(adapter); //Set the adapter of this layout to the new one

        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) { //Add a click Listener on each member of the list (each drinks)
                Log.d(TAG, "onItemClick: drink: " + drinks.get(position).getName()); //Show in the console the selected drink
                Toast.makeText(MainActivity.this, drinks.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpDrinks(){
        drinks.add(new Drink("Bloody mary",new String[]{"vodka", "tomato juice", "lemon juice"},new Double[]{4.5,9.0,1.5},R.drawable.cocktail));
        drinks.add(new Drink("Cocktail",new String[]{"orange", "pomme", "raisin"}, new Double[]{5.0,3.0,4.0},R.drawable.cocktail1));
        drinks.add(new Drink("Jus d'orange",new String[]{"orange"}, new Double[]{100.},R.drawable.cocktail2));
        drinks.add(new Drink("Gin Tonic",new String[]{"gin", "tonic"}, new Double[]{7.0,14.0}, R.drawable.gintonic));
        drinks.add(new Drink("Mai Thai",new String[]{"rhum blanc", "rhum ambré","cointreau","sucre de canne","sirop d'orgeat"}, new Double[]{3.,3.,2.,1.,1.},R.drawable.maithai));
        drinks.add(new Drink("Mojito", new String[]{"rhum blanc","sucre de canne","eau gazeuse"},new Double[]{4.,2.,1.},R.drawable.mojito));
        drinks.add(new Drink("Bloody mary",new String[]{"vodka", "tomato juice", "lemon juice"},new Double[]{4.5,9.0,1.5},R.drawable.cocktail));
        drinks.add(new Drink("Cocktail",new String[]{"orange", "pomme", "raisin"}, new Double[]{5.0,3.0,4.0},R.drawable.cocktail1));
        drinks.add(new Drink("Jus d'orange",new String[]{"orange"}, new Double[]{100.},R.drawable.cocktail2));
        drinks.add(new Drink("Gin Tonic",new String[]{"gin", "tonic"}, new Double[]{7.0,14.0},R.drawable.gintonic));
        drinks.add(new Drink("Mai Thai",new String[]{"rhum blanc", "rhum ambré","cointreau","sucre de canne","sirop d'orgeat"}, new Double[]{3.,3.,2.,1.,1.},R.drawable.maithai));
        drinks.add(new Drink("Mojito", new String[]{"rhum blanc","sucre de canne","eau gazeuse"},new Double[]{4.,2.,1.},R.drawable.mojito));

    }

    private void setUpBottles(){
        bottles.add(new Bottle("Vodka", 1,R.drawable.bottle_vodka));
        bottles.add(new Bottle("Jager", 2,R.drawable.bottle_jager));
        bottles.add(new Bottle("Rum", 3,R.drawable.bottle_rum));
        bottles.add(new Bottle("Tequila", 4,R.drawable.bottle_champagne));
        bottles.add(new Bottle("Whiskey", 5,R.drawable.bottle_whiskey));
        bottles.add(new Bottle("Gin", 6,R.drawable.bottle_gin));
        bottleList= new BottleList(bottles);

        for(Bottle bottle : bottles){
            myDatabase.insertBottleInDatabase(bottle);
        }
    }
}
