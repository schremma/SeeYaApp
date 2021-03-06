package com.group16.seeyaapp.model;

/**
 * Created by Andrea on 15/04/16.
 * Model class representation a single location where an activity can take place.
 * A location has a name and an id.
 */
public class Location {
    private String name;
    private int id;

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
