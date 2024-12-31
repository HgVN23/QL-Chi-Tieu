package com.example.myapplication2.models;

public class ThuChi {
    private int id;
    private String title;
    private String date;
    private int payment;
    private String type;
    private String category;

    public ThuChi() {
    }

    public ThuChi(int id, String title, String date, int payment, String type, String category) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.payment = payment;
        this.type = type;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
