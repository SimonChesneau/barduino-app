package com.example.barduino.bottle;

import java.util.ArrayList;
import java.util.List;

public class BottleList{
    private List<Bottle> bottleList;

    public BottleList(List<Bottle> bottleList) {
        this.bottleList = bottleList;
    }

    public List<Bottle> getBottleList() {
        return bottleList;
    }

    public void setBottleList(ArrayList<Bottle> bottleList) {
        this.bottleList = bottleList;
    }

    public void addBottle(Bottle bottle){
        this.bottleList.add(bottle);
    }

    public int size(){
        return this.bottleList.size();
    }

    public Bottle get(int position){
        return this.bottleList.get(position);
    }

    public void set(int position, Bottle bottle) {
        this.bottleList.set(position,bottle);
    }
}
