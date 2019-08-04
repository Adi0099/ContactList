package com.example.contactlist.framework.model;

public class DataBaseModel {
    public static final String TABLE_NAME = "contact";

    public static final String COLUMN_CID = "cid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";

    private int id;
    private String cid;
    private String name;
    private String number;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_CID + " TEXT PRIMARY KEY,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NUMBER + " TEXT"
                    + ")";

    public DataBaseModel() {
    }

    public DataBaseModel(String cid, String name, String number) {
        this.id = id;
        this.cid = cid;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}