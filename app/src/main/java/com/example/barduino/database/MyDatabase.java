package com.example.barduino.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.barduino.bottle.Bottle;
import com.example.barduino.drink.Drink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Data_Manager";

    // Table name: Bottle.
    private static final String TABLE_BOTTLE = "Bottle";
    private static final String COLUMN_BOTTLE_ID ="PK_id_Bottle";
    private static final String COLUMN_BOTTLE_NAME ="Name";
    private static final String COLUMN_BOTTLE_PICTURE ="Picture";
    private static final String COLUMN_BOTTLE_POSITION ="Position";

    // Table name: Drinks.
    private static final String TABLE_DRINKS = "Drinks";
    private static final String COLUMN_DRINKS_ID ="PK_id_Drink";
    private static final String COLUMN_DRINKS_NAME ="Name";
    private static final String COLUMN_DRINKS_PICTURE ="Picture";

    // Table name: Ingredients.
    private static final String TABLE_INGREDIENTS = "Ingredients";
    private static final String COLUMN_INGREDIENTS_DRINK_ID ="PK_FK_Drink";
    private static final String COLUMN_INGREDIENTS_BOTTLE_ID ="PK_FK_Bottle";
    private static final String COLUMN_INGREDIENTS_QUANTITY ="Quantity";

    Context ctx;
    private static MyDatabase myDatabase;

    public static MyDatabase getInstance(Context context){
        //Setting the database as a singleton to prevent multi access within this app
        if(myDatabase == null){
            myDatabase = new MyDatabase(context);
        }

        return myDatabase;
    }

    private MyDatabase(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx=context;
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseBottle creation ... ");

        //TODO see if we can multithread the database creation

        // Creation bottle table Script.
        String createBottleTableScript = "CREATE TABLE " + TABLE_BOTTLE + "("
                + COLUMN_BOTTLE_ID + " [int] PRIMARY KEY,"
                + COLUMN_BOTTLE_NAME + " [nvarchar](255),"
                + COLUMN_BOTTLE_PICTURE + " INTEGER, "
                + COLUMN_BOTTLE_POSITION + " INTEGER NOT NULL "
                + ")";

        // Execute Script
        db.execSQL(createBottleTableScript);


        Log.i(TAG, "MyDatabaseIngredient creation ... ");

        // Creation ingredients table Script.
        String createIngredientsTableScript = "CREATE TABLE " + TABLE_DRINKS + "("
                + COLUMN_INGREDIENTS_DRINK_ID + " [int],"
                + COLUMN_INGREDIENTS_BOTTLE_ID + " [int],"
                + COLUMN_INGREDIENTS_QUANTITY + " [decimal], "
                + "PRIMARY KEY(["+COLUMN_INGREDIENTS_DRINK_ID+"], ["+COLUMN_INGREDIENTS_BOTTLE_ID+"])"
                + ")";

        // Execute Script
        db.execSQL(createIngredientsTableScript);


        Log.i(TAG, "MyDatabaseDrinks creation ... ");

        // Creation drinks table Script.
        String createDrinksTableScript = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + COLUMN_DRINKS_ID + " [int] PRIMARY KEY,"
                + COLUMN_DRINKS_NAME + " [nvarchar](255), "
                + COLUMN_DRINKS_PICTURE + " INTEGER "
                + ")";

        // Execute Script
        db.execSQL(createDrinksTableScript);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseBottle upgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOTTLE);

        Log.i(TAG, "MyDatabaseDrinks upgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINKS);

        Log.i(TAG, "MyDatabaseIngredients upgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);

        // Create tables again
        onCreate(db);
    }

    public void insertBottleInDatabase(Bottle bottle) {
        Log.i(TAG, "MyDatabaseGrille.insertBottleInDatabase ... " + bottle.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOTTLE_NAME, bottle.getName());
        values.put(COLUMN_BOTTLE_PICTURE, bottle.getImg());
        values.put(COLUMN_BOTTLE_POSITION, bottle.getPosition());

        // Inserting Row
        long generatedId = db.insert(TABLE_BOTTLE, null, values);
        bottle.setId(generatedId);

        // Closing database connection
        db.close();
    }

    public void insertDrinksInDatabase(Drink drink) {
        Log.i(TAG, "MyDatabase.insertDrinksInDatabase ... " + drink.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRINKS_NAME, drink.getName());
        values.put(COLUMN_DRINKS_PICTURE, drink.getImg());

        // Inserting Row
        long generatedId = db.insert(TABLE_DRINKS, null, values);
        drink.setId(generatedId);


        // Closing database connection
        db.close();
    }

    public void insertIngredientsInDatabase(Drink drink) {
        Log.i(TAG, "MyDatabase.insertIngredientsInDatabase ... " + drink.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        for(Map.Entry<Bottle, Double> ingredientWithQuantity : drink.getIngredientMap().entrySet()){
            //Retrieving bottle and quantities
            Bottle bottleNeeded = ingredientWithQuantity.getKey();
            Double QuantityNeeded = ingredientWithQuantity.getValue();

            //Setting up values
            ContentValues values = new ContentValues();
            values.put(COLUMN_INGREDIENTS_BOTTLE_ID, bottleNeeded.getId());
            values.put(COLUMN_INGREDIENTS_DRINK_ID, drink.getId());
            values.put(COLUMN_INGREDIENTS_QUANTITY, QuantityNeeded);

            // Inserting Row
            db.insert(TABLE_DRINKS, null, values);
        }

        // Closing database connection
        db.close();

    }

    public Bottle getBottle(long id) {
        Log.i(TAG, "MyDatabase.getBottle ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOTTLE, new String[] {
                        COLUMN_BOTTLE_ID,
                        COLUMN_BOTTLE_NAME,
                        COLUMN_BOTTLE_POSITION,
                        COLUMN_BOTTLE_PICTURE
                }, COLUMN_BOTTLE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Bottle bottle = new Bottle( cursor.getLong(0),
                                    cursor.getString(1),
                                    cursor.getInt(2),
                                    cursor.getInt(3) );

        // return bottle
        return bottle;
    }

    public Drink getDrink(long id) {
        Log.i(TAG, "MyDatabase.getDrink ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DRINKS, new String[] {
                        COLUMN_DRINKS_ID,
                        COLUMN_DRINKS_NAME,
                        COLUMN_DRINKS_PICTURE
                }, COLUMN_DRINKS_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Map<Bottle, Double> ingredients = retrieveIngredientsFromDrinkId(id);

        Drink drink = new Drink( cursor.getLong(0),
                cursor.getString(1),
                ingredients,
                cursor.getInt(2));

        // return drink
        return drink;
    }

    private Map<Bottle, Double> retrieveIngredientsFromDrinkId(long drinkId) {

        Map<Bottle, Double> BottleWithQuantities = new HashMap<>();

        Log.i(TAG, "MyDatabase.getIngredientsFromDrinkId ... " + drinkId);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INGREDIENTS, new String[] {
                        COLUMN_INGREDIENTS_BOTTLE_ID,
                        COLUMN_INGREDIENTS_QUANTITY,
                }, COLUMN_INGREDIENTS_DRINK_ID + "=?",
                new String[] { String.valueOf(drinkId) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bottle bottle = getBottle(cursor.getLong(0)); //Retrieving Bottle
                BottleWithQuantities.put(bottle, cursor.getDouble(1)); //Adding the bottle and its quantity to the hashmap
            } while (cursor.moveToNext());
        }

        return BottleWithQuantities;
    }

    public List<Bottle> getAllBottles() {
        Log.i(TAG, "MyDatabase.getAllBottles ... " );

        List<Bottle> bottleList = new ArrayList<Bottle>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOTTLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bottle bottle = new Bottle(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

                // Adding bottle to list
                bottleList.add(bottle);
            } while (cursor.moveToNext());
        }

        // return grid list
        return bottleList;
    }

    public List<Drink> getAllDrinks() {
        Log.i(TAG, "MyDatabase.getAllBottles ... " );

        List<Drink> drinkList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DRINKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Map<Bottle, Double> ingredients = retrieveIngredientsFromDrinkId(cursor.getLong(0));
                Drink drink = new Drink( cursor.getLong(0),
                        cursor.getString(1),
                        ingredients,
                        cursor.getInt(2));

                // Adding bottle to list
                drinkList.add(drink);
            } while (cursor.moveToNext());
        }

        // return grid list
        return drinkList;
    }

    public int getBottlesCount() {
        Log.i(TAG, "MyDatabase.getBottlesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_BOTTLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int getDrinksCount() {
        Log.i(TAG, "MyDatabase.getDrinksCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_DRINKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateBottle(Bottle bottle) {
        Log.i(TAG, "MyDatabase.updateBottle ... "  + bottle.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BOTTLE_NAME, bottle.getName());
        values.put(COLUMN_BOTTLE_PICTURE, bottle.getImg());
        values.put(COLUMN_BOTTLE_POSITION, bottle.getPosition());

        // updating row
        return db.update(TABLE_BOTTLE, values, COLUMN_BOTTLE_ID + " = ?",
                new String[]{String.valueOf(bottle.getId())});
    }

    public int updateDrink(Drink drink) {
        Log.i(TAG, "MyDatabase.updateDrink ... "  + drink.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRINKS_NAME, drink.getName());
        values.put(COLUMN_DRINKS_PICTURE, drink.getImg());

        updateRelatedIngredients(drink);

        // updating row
        return db.update(TABLE_DRINKS, values, COLUMN_DRINKS_ID + " = ?",
                new String[]{String.valueOf(drink.getId())});
    }

    private void updateRelatedIngredients(Drink drink) {
        Log.i(TAG, "MyDatabase.updateIngredients ... "  + drink.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        for(Map.Entry<Bottle,Double> bottleWithQuantity : drink.getIngredientMap().entrySet()){

            ContentValues values = new ContentValues();
            values.put(COLUMN_INGREDIENTS_QUANTITY, bottleWithQuantity.getValue());

            // updating row
            db.update(TABLE_INGREDIENTS, values, COLUMN_INGREDIENTS_DRINK_ID + " = ? AND " + COLUMN_INGREDIENTS_BOTTLE_ID + " = ?",
                    new String[]{String.valueOf(drink.getId()), String.valueOf(bottleWithQuantity.getKey().getId())});
        }

    }

    public void deleteBottle(Bottle bottle) {
        Log.i(TAG, "MyDatabase.deleteBottle ... " + bottle.getName() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOTTLE, COLUMN_BOTTLE_ID + " = ?",
                new String[] { String.valueOf(bottle.getId()) });
        db.close();
    }

    public void deleteDrinks(Drink drink) {
        Log.i(TAG, "MyDatabase.deleteDrink ... " + drink.getName() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRINKS, COLUMN_DRINKS_ID + " = ?",
                new String[] { String.valueOf(drink.getId()) });
        db.close();
    }


}

