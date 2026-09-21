import java.util.*;
import java.io.*;

public class Spire {

    /**
     * This is the main method used to run the program to take in a deck file and
     * print either a standard report or a void report if it is invalid.
     * @param args The user input which should be the file name for the deck.
     */
    public static void main(String[] args) {

        // Takes in User Input
        Scanner scanner = new Scanner(System.in);
        String fileName =scanner.nextLine();

        //Creates a file using the input
        File file = new File(fileName);
        scanner.close();

        //Generate the random id to ensure it is 9-digits long
        Random gen = new Random();
        // having it be at minimum the smallest 9-digit number and having it bound to the
        // highest 9-digit number ensures that it will be 9-digits long.
        int id = gen.nextInt(0,900000000) + 100000000;

        // Checks to see if the deck is valid or not to see if it should read the file and continue or not.
        if(!isValid(file,id)){
            return;
        }else{
            //read file
        }
    }

    /**
     *
     * @param file The deck to check if it is a valid file or not.
     * @param id The unique randomly generated 9-digit id of the deck.
     * @return True if the file does not exist and False if the file does exist.
     */
    private static boolean isValid(File file, int id){
        return !file.exists();
    }

}
