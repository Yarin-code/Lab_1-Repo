import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class UserApp {
    public static void main(String[] args) {


        ArrayList<User> users = new ArrayList<User>();

        // opening the input file
        try {

            File myFile = new File("Users.txt");
            Scanner reader = new Scanner(myFile);

            while (reader.hasNextLine()) {
                String email = reader.next();
                String password = reader.next();

                User newUser;

                try {
                    newUser = new User(email, password);
                }
                catch (Exception e) {

                    System.out.println(e.getMessage());
                    continue;

                }
                //if user is valid, add to ArrayList of User
                users.add(newUser);

            }
            reader.close();

        }

        catch (Exception e) {

            System.out.println("File not found");

        }
        // lexicographically sorting the ArrayList
        Collections.sort(users,(u1, u2) -> u1.getEmail().compareTo(u2.getEmail()));

        // creating new out1.exe file, and storing the ArrayList in it
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("out1.txt"))) {
            for(int i = 0; i<users.size(); i++){
                writer.write(String.valueOf(users.get(i)));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error creating the file" + e.getMessage());
        }
        

    }


}



