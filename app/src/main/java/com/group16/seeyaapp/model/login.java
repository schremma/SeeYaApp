package com.group16.seeyaapp.model;

/**
 * Created by Andrea on 08/04/16.
 */
public class Login {

    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUserName()  {return username;}
    public String getPassword()  {return password;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String psw) {password = psw;}
}
