package com.group16.seeyaapp.model;

/**
 * Created by Andrea on 21/04/16.
 */
public class NestedCategory {
    private int id;
    private String name;
    private String parentName;
    private int parentId;

    public NestedCategory() {}

    public NestedCategory(int id, String name) {this(id, name, 0, null);}

    public NestedCategory(int id, String name, int parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
