package com.example.lab_2;

import java.util.regex.Pattern;

public class User {
    private String email;
    private String password;

    private static final String EMAIL_REGEX = "^(?=.{1,50}$)[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9][a-zA-Z0-9-]*(\\.[a-zA-Z]{2,})+$";;
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#.,+_()*&^%$?])[a-zA-Z\\d!@#.,+_()*&^%$?]{8,12}$";

    // בדיקה שהשם משתמש והסיסמא עומדים בדרישות
    public User(String email, String password) throws Exception {
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new Exception("Please enter a valid Email as username");
        }
        if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            throw new Exception("Please enter a valid password");
        }
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
