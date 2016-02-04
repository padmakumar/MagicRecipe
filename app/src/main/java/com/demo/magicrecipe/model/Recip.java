package com.demo.magicrecipe.model;


import com.google.gson.annotations.SerializedName;

public class Recip {


    @SerializedName("title")
    public String title;
    @SerializedName("href")
    public String href;

    @SerializedName("ingredients")
    public String ingredients;
    @SerializedName("thumbnail")
    public String thumbnail;

    public Recip(String title, String href, String ingredients, String thumbnail ) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }


}


