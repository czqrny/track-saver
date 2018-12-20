package com.example.lokalnedoserwisu.tracksaver;

public class User {
    private int id;
    private String name;
    private String email;
    private int password;

    public User(){}

    public User(int id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    @Override
    public String toString() {
        String user;
        user = "id: " + this.id + "\n";
        user += "name: " + this.name + "\n";
        user += "email: " + this.email + "\n";
        user += "password: " + this.password + "\n";
        return user;
    }
}
