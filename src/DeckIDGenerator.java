/**
 * The {@code DeckIDGenerator} class is responsible for generating unique
 * 9-digit deck IDs and managing their persistence in a text file.
 *
 * <p>This class provides methods to generate a random, unique number and
 * save it to a specified file. It also loads previously generated IDs at
 * initialization to ensure that new IDs are unique.</p>
 *
 * <p>Deck IDs are stored in a HashSet to facilitate quick checks for uniqueness.</p>
 *
 * <p>Note: The generated IDs are saved to the file specified by the
 * {@code DECK_ID_FILE} constant, which should be correctly set to ensure
 * proper file operations.</p>
 */
import java.io.*;
import java.util.HashSet;
import java.util.Random;

public class DeckIDGenerator {

    private static final String DECK_ID_FILE = "/Users/sophiabrix/Desktop/Java Programs/SlayTheSpireDeckCostTally/src/DeckIDs.txt";
    private static HashSet<String> generatedIDs = new HashSet<>();

    static {
        loadGeneratedNumbers();
    }

    /**
     * Loads previously generated deck IDs from a specified file.
     *
     * <p>This method reads each line of the file designated by {@code DECK_ID_FILE}
     * and adds the trimmed IDs to the {@link #generatedIDs} collection. Each line
     * in the file is expected to contain a single deck ID.</p>
     *
     * <p>If the file does not exist (for example, on the first run of the application),
     * the method handles the {@link IOException} gracefully without throwing an error.</p>
     *
     * @throws IOException if an error occurs while reading the file.
     *         (This is handled internally, and the method will not propagate the exception.)
     */
    private static void loadGeneratedNumbers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DECK_ID_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                generatedIDs.add(line.trim());
            }
        } catch (IOException e) {
            //file might not exist on first run, handle gracefully
        }
    }

    /**
     * Generates a unique 9-digit string.
     *
     * <p>This method creates a random 9-digit number and ensures its uniqueness
     * by checking against a collection of previously generated IDs. If the generated
     * number already exists in the {@link #generatedIDs} collection, the method
     * will continue generating new numbers until a unique one is found.</p>
     *
     * <p>Once a unique number is generated, it is added to the collection of
     * generated IDs and saved to a persistent storage file using the
     * {@link #saveGeneratedNumber(String)} method.</p>
     *
     * @return a unique 9-digit string as a {@code String}
     */
    public static String generateUniqueNumber() {
        Random random = new Random();
        String generated;

        do {
            generated = String.format("%09d", random.nextInt(1_000_000_000)); //generate 9-digit number
        } while (generatedIDs.contains(generated)); //check for uniqueness

        generatedIDs.add(generated);
        saveGeneratedNumber(generated); //save to file
        return generated;
    }

    /**
     * Saves the generated number to a persistent storage file.
     *
     * <p>This method appends the provided number to a file specified by the
     * {@link #DECK_ID_FILE} constant. Each number is written on a new line
     * to maintain a clear record of generated IDs.</p>
     *
     * <p>If an {@link IOException} occurs during the file operation, the
     * exception will be caught and its stack trace printed to the console.</p>
     *
     * @param number the generated number to be saved, as a {@code String}
     */
    private static void saveGeneratedNumber(String number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DECK_ID_FILE, true))) {
            writer.write(number);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}