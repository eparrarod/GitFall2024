package src;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

// This is where I found the .jar file for OpenPDF: https://repo1.maven.org/maven2/com/github/librepdf/openpdf/3.0.5/
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;

// This is where I found the .jar file for JFreeChart:https://mvnrepository.com/artifact/org.jfree/jfreechart/1.5.6


public class Spire {

    /**
     * This is the main method used to run the program to take in a deck file and
     * print either a standard report or a void report if it is invalid.
     * @param args The user input which should be the file name for the deck.
     */
    public static void main(String[] args) {
        // Takes in User Input
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

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
            System.out.println("Invalid File");
            return;
        }else{
            readFile(file,id);
        }
    }

    /**
     *
     * @param file The deck to check if it is a valid file or not.
     * @param id The unique randomly generated 9-digit id of the deck.
     * @return True if the file does not exist and False if the file does exist.
     */
    private static boolean isValid(File file, int id){
        return file.exists();
    }

    /**
     * This reads the deck file to get all the information needed for the report or the void report
     * if the deck meets certain conditions. This will calculate the total cost of the deck, get the
     * frequency of each cost for the histogram, call methods to generate either one of the normal
     * report of the void report.
     * @param file The deck to write the repot for.
     * @param id The 9-digit id for the deck.
     */
    private static void readFile(File file,int id) {
        // Try to read the file to see if it is readable or will cause an error.
        try (Scanner fileScanner = new Scanner(file)){

            // Keep track of the invalid cards
            ArrayList <String> invalid = new ArrayList<>();

            // Total cost of the deck
            int total = 0;

            // Frequency of each valid cost for the histogram
            int costFrequency [] = new int[7];

            // The cost of a card after it is deciphered
            int cost;

            // Count of how many cards there are in the deck
            int rowCount=0;

            // The ArrayList of all valid card names which are real Slay the src.Spire | card names
            ArrayList<String> validCardNames = validNames();

            // Iterate over the entire deck
            while(fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();

                // Only if the line contains a colon which indicates the row is a data row that is valid
                if(line.contains(":")){

                    // Increment card count and if it is over 1000 return with the void file.
                    rowCount++;
                    if(rowCount>1000){
                        voidFile(id);
                        return;
                    }

                    // Split the row into the card name and the cost which are both strings.
                    String [] split = line.split(":");

                    // See if both the card name and cost are valid values to either add to the total cost
                    // and frequency count or invalid to add to the invalid counter.
                    if(validCost(split[1].strip()) && validCard(split[0].strip(),validCardNames)){
                        cost = (int)split[1].strip().charAt(0) - 48;
                        total+= cost;
                        costFrequency[cost]++;

                    }else{
                        // If it is invalid it will append energy to the end and then if there
                        // Are more than 10 invalid cards then it will return and print the void file.
                        invalid.add(line + " energy");
                        if(invalid.size()>10){
                            voidFile(id);
                            return;
                        }
                    }
                }
            }

            //Write PDF REPORT
            writePDF(id,total,costFrequency,invalid);

        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    /**
     * This sees if the cost of a card is a valid cost, being between and including 0 and 6.
     * @param cost The cost of the card as a string.
     * @return True if the cost is valid and False if the cost is invalid.
     */
    private static boolean validCost(String cost){
        // Found .matches from JavaDoc on String class
        return cost.matches("[0-6]");
    }

    /**
     * This checks if a card's name is in the ArrayList of valid names, which are real
     * names of Slay the src.Spire | cards, or not to check if a card is valid from its name.
     * @param cardName The name of the card to check.
     * @param validCardNames The ArrayList of valid card names.
     * @return True if the card name is valid and False if the card name is invalid.
     */
    private static boolean validCard(String cardName, ArrayList<String> validCardNames){
        return !cardName.isEmpty() && validCardNames.contains(cardName.toLowerCase());
    }

    /**
     * This reads a file on the same directory as the program called cards.txt, which contains
     * all the valid card names from Slay the src.Spire where each row is one card name, and
     * puts them in an ArrayList.
     * @return ArrayList of with all the card names inside of it.
     * @throws FileNotFoundException
     */
    private static ArrayList<String> validNames() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/cards.txt"));

        ArrayList<String> validNames = new ArrayList<>();

        while (scanner.hasNextLine()) {
            validNames.add(scanner.nextLine().toLowerCase().strip());
        }
        return validNames;

    }

    /**
     * This method takes in the ID of the deck and produces the VOID PDF.
     * @param id The 9-digit id of the deck.
     */
    private static void voidFile(int id) {
        //Make the void pdf
        // I used the Javadoc for OpenPDF as well as the Tutorial to help me understand
        // how to use this library.
        Document document = new Document();
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("SpireDeck_" + id + "(VOID).pdf"));
            document.open();
            document.add(new Paragraph("VOID"));
            document.close();
        }catch (IOException | DocumentException e){
            System.out.println("Error with Void Report creation.");
        }
    }

    /**
     * This creates the PDF report of the deck featuring its id, histogram, and a list of
     * invalid cards in the deck.
     * @param id The 9-digit integer id of the deck.
     * @param total The total cost of all the cards in the deck.
     * @param frequency An integer array of the frequency of each valid cost in the deck.
     * @param invalid An ArrayList of all invalid cards in the deck.
     */
    private static void writePDF(int id, int total, int[] frequency, ArrayList<String> invalid){

    }

}
