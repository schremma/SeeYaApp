package com.group16.seeyaapp.model;

/**
 * Created by Andrea on 18/04/16.
 */
public class UserLogin {
    private String username;
    private String password;

    public UserLogin() {}

    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUserName()  {return username;}
    public String getPassword()  {return password;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String psw) {password = psw;}

    public boolean ValidateFormat() {
        boolean userOk = username != null && !username.isEmpty();
        boolean passwordOk = password != null && !password.isEmpty();

        return userOk && passwordOk;
    }
}
