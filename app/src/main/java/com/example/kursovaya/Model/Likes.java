package com.example.kursovaya.Model;

public class Likes {

    public String userId;
    public String shoesId, userIud_ShoesID;
    public String category;

    public  Likes(){

    }

    public Likes(String userId, String shoesId, String userIud_ShoesID) {
        this.userId = userId;
        this.shoesId = shoesId;
        this.userIud_ShoesID = userIud_ShoesID;
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShoesId() {
        return shoesId;
    }

    public void setShoesId(String shoesId) {
        this.shoesId = shoesId;
    }

    public String getUserIud_ShoesID() {
        return userIud_ShoesID;
    }

    public void setUserIud_ShoesID(String userIud_ShoesID) {
        this.userIud_ShoesID = userIud_ShoesID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


