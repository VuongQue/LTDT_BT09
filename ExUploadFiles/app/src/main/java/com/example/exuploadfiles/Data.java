package com.example.exuploadfiles;

public class Data {
    private int id;
    private String username;
    private String fname;
    private String email;
    private String gender;
    private String images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Data(int id, String username, String fname, String email, String gender, String images) {
        this.id = id;
        this.username = username;
        this.fname = fname;
        this.email = email;
        this.gender = gender;
        this.images = images;
    }
}
