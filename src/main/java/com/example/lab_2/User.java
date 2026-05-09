package com.example.lab_2;

import java.util.regex.Pattern;

public class User {
    private String email;
    private String password;
    private int failedAttempts;
    private long lockoutEndTime;

    private static final String EMAIL_REGEX = "^(?=.{1,50}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#.,+_()*&^%$?])[a-zA-Z\\d!@#.,+_()*&^%$?]{8,12}$";

    public User(String email, String password) throws Exception {
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new Exception("Please enter a valid Email as username");
        }
        if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            throw new Exception("Please enter a valid password");
        }
        this.email = email;
        this.password = password;
        this.failedAttempts = 0;
        this.lockoutEndTime = 0;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }

    // מתודות מסונכרנות (Thread-Safe) לניהול מצב המשתמש
    public synchronized int getFailedAttempts() { return failedAttempts; }

    public synchronized void incrementFailedAttempts() { this.failedAttempts++; }

    public synchronized void resetFailedAttempts() { this.failedAttempts = 0; }

    public synchronized long getLockoutEndTime() { return lockoutEndTime; }

    public synchronized void setLockoutEndTime(long lockoutEndTime) { this.lockoutEndTime = lockoutEndTime; }

    // בדיקה האם המשתמש חסום כרגע
    public synchronized boolean isLocked() {
        return System.currentTimeMillis() < lockoutEndTime;
    }
}