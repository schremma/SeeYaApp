package com.group16.seeyaapp.model;

import java.util.regex.Pattern;

/**
 * Created by Andrea on 10/04/16.
 */
public class Account {

    private String username;
    private String password;
    private String email;
    private boolean emailPublic;

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    // Get and set methods for all fields:
    public String getUserName()  {return username;}
    public String getPassword()  {return password;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String psw) {password = psw;}
    public String getEmail()  {return email;}
    public void setEmail(String email) {this.email = email;}
    public boolean emailIsPublic() {return emailPublic;}
    public void setEmailPublic(boolean isPublic) {emailPublic = isPublic;}

    // TODO: add methods for checking if fields have valid format - maybe return a checked exception
    // instead with information on the expected format in the message

    public boolean validateEmail() {
        if (email != null) {

            //This just checks for the @ to be present
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(regex).matches()) {
                return true;
            }
        }
        return false;
    }

    //Now it just checks if psw is not empty
    public boolean validatePassword() {
        return (password != null && !password.isEmpty());
    }
}
