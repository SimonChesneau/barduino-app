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
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private List<Drink> drinks = null;
    private List<Bottle> bottles = null;
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
        /*drinks.add(new Drink("Bloody mary",new String[]{"vodka", "tomato juice", "lemon juice"},new Double[]{4.5,9.0,1.5},R.drawable.cocktail));
        drinks.add(new Drink("Cocktail",new String[]{"orange", "pomme", "raisin"}, new Double[]{5.0,3.0,4.0},R.drawable.cocktail1));
        drinks.add(new Drink("Jus d'orange",new String[]{"orange"}, new Double[]{100.},R.drawable.cocktail2));
        drinks.add(new Drink("Gin Tonic",new String[]{"gin", "tonic"}, new Double[]{7.0,14.0}, R.drawable.gintonic));
        drinks.add(new Drink("Mai Thai",new String[]{"rhum blanc", "rhum ambré","cointreau","sucre de canne","sirop d'orgeat"}, new Double[]{3.,3.,2.,1.,1.},R.drawable.maithai));
        drinks.add(new Drink("Mojito", new String[]{"rhum blanc","sucre de canne","eau gazeuse"},new Double[]{4.,2.,1.},R.drawable.mojito));
        */

        if(myDatabase.getDrinksCount() == 0) {

            List<Drink> liste = new ArrayList<>();

            HashMap<Bottle, Double> bloodyMary = new HashMap<>();
            bloodyMary.put(bottles.get(0), 4.5);
            bloodyMary.put(bottles.get(6), 9.0);
            bloodyMary.put(bottles.get(7), 1.5);
            liste.add(new Drink("Bloody mary", bloodyMary, R.drawable.cocktail));

            HashMap<Bottle, Double> cocktail = new HashMap<>();
            cocktail.put(bottles.get(8), 5.0);
            cocktail.put(bottles.get(9), 3.0);
            cocktail.put(bottles.get(10), 4.0);
            liste.add(new Drink("Cocktail", cocktail, R.drawable.cocktail1));

            HashMap<Bottle, Double> orangeJuice = new HashMap<>();
            orangeJuice.put(bottles.get(8), 100.);
            liste.add(new Drink("Orange Juice", orangeJuice, R.drawable.cocktail2));

            HashMap<Bottle, Double> ginTonic = new HashMap<>();
            ginTonic.put(bottles.get(5), 7.0);
            ginTonic.put(bottles.get(11), 14.0);
            liste.add(new Drink("Gin Tonic", ginTonic, R.drawable.gintonic));

            HashMap<Bottle, Double> maiThai = new HashMap<>();
            maiThai.put(bottles.get(12), 3.);
            maiThai.put(bottles.get(13), 3.);
            maiThai.put(bottles.get(14), 2.);
            maiThai.put(bottles.get(15), 1.);
            maiThai.put(bottles.get(16), 1.);
            liste.add(new Drink("Mai Thai", maiThai, R.drawable.maithai));

            HashMap<Bottle, Double> mojito = new HashMap<>();
            mojito.put(bottles.get(12), 4.);
            mojito.put(bottles.get(15), 2.);
            mojito.put(bottles.get(17), 1.);
            liste.add(new Drink("Mojito", mojito, R.drawable.mojito));

            List<Drink> liste2 = myDatabase.getAllDrinks();
            for (Drink drink : liste2) {
                Log.i(TAG, "drink name: " + drink.getName() + " -- id: " + drink.getId());
            }

            for (Drink drink : liste) {
                myDatabase.insertDrinkInDatabase(drink);
            }
        }

        drinks = myDatabase.getAllDrinks();
        /*for(Drink drink : liste2){
            Log.i(TAG, "drink name: "+drink.getName() + " -- id: " + drink.getId());
        }*/


    }

    private void setUpBottles(){

        if(myDatabase.getBottlesCount() == 0) {
            //Hardcoded values (now in the database)
            bottles = new ArrayList<>();
            bottles.add(new Bottle("Vodka", 1, R.drawable.bottle_vodka));
            bottles.add(new Bottle("Jager", 2, R.drawable.bottle_jager));
            bottles.add(new Bottle("Rum", 3, R.drawable.bottle_rum));
            bottles.add(new Bottle("Tequila", 4, R.drawable.bottle_champagne));
            bottles.add(new Bottle("Whiskey", 5, R.drawable.bottle_whiskey));
            bottles.add(new Bottle("Gin", 6, R.drawable.bottle_gin));
            bottles.add(new Bottle("Tomato Juice", 7, R.drawable.bottle_jager));
            bottles.add(new Bottle("Lemon Juice", 8, R.drawable.bottle_jager));
            bottles.add(new Bottle("Orange Juice", 9, R.drawable.bottle_jager));
            bottles.add(new Bottle("Apple Juice", 10, R.drawable.bottle_jager));
            bottles.add(new Bottle("Grape Juice", 11, R.drawable.bottle_jager));
            bottles.add(new Bottle("Tonic", 12, R.drawable.bottle_jager));
            bottles.add(new Bottle("White Rum", 13, R.drawable.bottle_jager));
            bottles.add(new Bottle("Dark Rum", 14, R.drawable.bottle_jager));
            bottles.add(new Bottle("Cointreau", 15, R.drawable.bottle_jager));
            bottles.add(new Bottle("Brown Sugar", 16, R.drawable.bottle_jager));
            bottles.add(new Bottle("Orgeat Sirop", 17, R.drawable.bottle_jager));
            bottles.add(new Bottle("Sparkling Water", 18, R.drawable.bottle_jager));
            for (Bottle bottle : bottles) {
                myDatabase.insertBottleInDatabase(bottle);
            }
        }



        bottles = myDatabase.getAllBottles();
        bottleList = new BottleList(bottles);
    }
}
