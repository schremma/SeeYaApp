package com.group16.seeyaapp.model;

import java.util.regex.Pattern;

/**
 * Created by Andrea on 10/04/16.
 * Model class for storing information for registering a new user and performing local
 * validation on the stored data.
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


    /**
     * Checks if the email field represents a valid email format.
     * @return True if the email is in valid format, false otherwise
     */
    public boolean validateEmail() {
        if (email != null) {

            //This just checks for the @ to be present
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(email).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if password is not empty.
     * @return True if the password is in valid format, false otherwise
     */
    public boolean validatePassword() {
        return (password != null && !password.isEmpty());
    }

    /**
     * Checks if user name is not empty
     * @return True if the user name is in valid format, false otherwise
     */
    public boolean validateUserName() {
        return (username != null && !username.isEmpty());
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


}
