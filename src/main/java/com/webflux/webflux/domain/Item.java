package com.webflux.webflux.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Item {

    private @Id String id;
    private String name;
    private String description;
    private double price;

    private Item() {
    }

    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Item(String id, String name, String description, double price) {
        this(name, description, price);
        this.id = id;
    }
}