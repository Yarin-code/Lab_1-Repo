import java.util.regex.Pattern;

public class User {

    private static final String EMAIL_REGEX = "^(?=.{1,}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#.,+_()*&^%$?])[a-zA-Z\\d!@#.,+_()*&^%$?]+$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    private String email;
    private String password;

    // constructor of User
    public User(String email, String password) throws Exception {

        // first we check the email and password based on regex rules
        if (!emailPattern.matcher(email).matches()) {

            throw new Exception(email + " " + password +" Please enter a valid Email as username");

        }

        if (!passwordPattern.matcher(password).matches()) {

            throw new Exception(email + " " + password +" Please enter a valid password");

        }

        // after checking regex, we check the length of the email and password
        if(email.length() > 50) {

            throw new Exception(email + " " + password +" Username is too long, try a shorter one");

        }


        if (password.length() < 8) {

            throw new Exception(email + " " + password +" Your password is too short, add more characters");


        }

        if (password.length() > 12) {

            throw new Exception(email + " " + password +" Your password is too long, try a shorter one");

        }



        this.email = email;
        this.password = password;


    }

    public String getPassword() {
        return password;
    }



    public String getEmail() {
        return email;
    }


    public String toString(){

        return email + " " + password;

    }

}
