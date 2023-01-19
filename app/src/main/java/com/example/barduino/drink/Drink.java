package com.example.barduino.drink;

import com.example.barduino.R;
import com.example.barduino.bottle.Bottle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Drink {

    private String name;
    private Map<Bottle, Double> ingredients;
    private Integer img;
    private long id;



    public Drink(String name, Map<Bottle, Double> ingredients, Integer img) {
        this.name = name;
        this.ingredients = ingredients;
        this.img = img;
    }

    public Drink(long id, String name, Map<Bottle, Double> ingredients, Integer img) {
        setId(id);
        setName(name);
        setIngredient(ingredients);
        setImg(img);
    }

    /*
    Anciens constructeurs utilisant des array plutot que des maps

    public Drink(String name, List<Bottle> ingredients, Double[] ingredientsQty, Integer img) {
        this.name = name;
        this.ingredients = new LinkedHashMap<Bottle, Double>();
        for(int i = 0; i < ingredients.size(); i++){
            this.ingredients.put(ingredients.get(i),ingredientsQty[i]);
        }

        this.img = img;
    }

    public Drink(String name, String[] ingredientsName, Double[] ingredientsQty, Integer img) {
        this.name = name;
        this.ingredients = new LinkedHashMap<Bottle, Double>();
        for(int i = 0; i < ingredientsName.length; i++){
            Bottle bottle = new Bottle(ingredientsName[i], 1, R.drawable.bottle_vodka);
            this.ingredients.put(bottle,ingredientsQty[i]);
        }

        this.img = img;
    }

    public Drink(long id, String name, List<Bottle> ingredients, Double[] ingredientsQty, Integer img) {
        this(name, ingredients, ingredientsQty, img); // We call the other constructor
        this.id = id; //And we add the id set
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredientText() {
        String ret = "";
        for(Map.Entry<Bottle, Double> entry : this.ingredients.entrySet()) {
            Bottle key = entry.getKey();
            Double value = entry.getValue();
            ret += key.getName()+" "+value+"cl, ";
        }
        return ret;
    }

    public Map<Bottle, Double> getIngredientMap() {
        return ingredients;
    }

    /*
    Old getter using array instead of Maps
    public void setIngredient(Bottle[] ingredients, Double[] ingredientsQty) {
        for(int i = 0; i < ingredients.length; i++){
            this.ingredients.put(ingredients[i],ingredientsQty[i]);
        }
    }
    */

    public void setIngredient(Map<Bottle, Double> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Bottle ingredient, Double ingredientQty) {
            this.ingredients.put(ingredient,ingredientQty);
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
