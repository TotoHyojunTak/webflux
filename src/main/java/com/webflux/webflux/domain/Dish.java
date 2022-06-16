package com.webflux.webflux.domain;

import lombok.Data;

@Data
public class Dish {
    private String descrption;
    private boolean delivered = false;

    public static Dish deliver(Dish dish){
        Dish deliveredDish = new Dish(dish.descrption);
        deliveredDish.delivered = true;
        return deliveredDish;
    }

    public Dish(String descrption){
        this.descrption = descrption;
    }
}
