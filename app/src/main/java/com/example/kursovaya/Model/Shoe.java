package com.example.kursovaya.Model;

import java.io.Serializable;

public class Shoe implements Serializable {

    boolean isLike;
    String Name, Description, URL, Number, Size, Money, Image, ShoesID;
    public Shoe()
    {

    }

    public Shoe(boolean isLike, String name, String description, String URL, String number, String size, String money, String image, String shoesID) {
        this.isLike = isLike;
        Name = name;
        Description = description;
        this.URL = URL;
        Number = number;
        Size = size;
        Money = money;
        Image = image;
        ShoesID = shoesID;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getShoesID() {
        return ShoesID;
    }

    public void setShoesID(String shoesID) {
        ShoesID = shoesID;
    }
}
