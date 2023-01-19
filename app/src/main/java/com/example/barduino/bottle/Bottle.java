package com.example.barduino.bottle;


public class Bottle {
    private long id;
    private String bottlename;
    private int bottleposition;
    private Integer img;

    public Bottle(String bottlename, int bottleposition, Integer img) {
        setName(bottlename); //I use the setters to simplify correction if I have to correct all the set of element
        setPosition(bottleposition); // Here, I only have 1 method to correct
        setImg(img);
    }

    public Bottle(long id, String bottlename, int bottleposition, Integer img) {
        this(bottlename, bottleposition, img);
        setId(id);
    }

    public String getName() {
        return bottlename;
    }

    public void setName(String bottlename) {
        this.bottlename = bottlename;
    }

    public int getPosition() {
        return bottleposition;
    }

    public void setPosition(int bottleposition) {
        this.bottleposition = bottleposition;
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

    public String toString(){
        return "Bottlename: "+bottlename+"Bottleposition: "+bottleposition;
    }
}
