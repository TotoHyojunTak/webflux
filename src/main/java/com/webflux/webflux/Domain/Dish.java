package com.webflux.webflux.Domain;

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

    public String getDescrption(){
        return descrption;
    }

    public void setDescrption(String descrption){
        this.descrption = descrption;
    }

    public boolean isDelivered(){
        return delivered;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "descrption='" + descrption + '\'' +
                ", delivered=" + delivered +
                '}';
    }
}
