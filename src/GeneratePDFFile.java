/**
 * This class provides methods to generate PDF reports for the "Slay the Spire"
 * deck cost tally. It utilizes the iText library to create PDF documents that
 * include details such as deck ID, total card cost, a list of invalid cards,
 * and a histogram representing card values.
 *
 * <p>There are two primary methods: {@link #generatePDF(String, String, int, HashMap, List)}
 * for generating a detailed report with a histogram, and {@link #generateVoidPDF(String, String)}
 * for creating a simple report indicating that the deck is void.</p>
 *
 * <p>PDF generation handles exceptions gracefully, ensuring a user-friendly experience
 * even when issues arise during file operations.</p>
 */


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import java.util.List;

public class GeneratePDFFile {

    /**
     * Generates a PDF report for the Slay the Spire deck cost tally.
     *
     * <p>This method creates a PDF document containing key information about the
     * card deck, including the deck ID, total cost, a list of invalid cards,
     * and a histogram representation of the card values. The PDF is generated
     * using the specified file path.</p>
     *
     * <p>The histogram visually represents the distribution of card costs,
     * with each bar corresponding to a card in the deck. The height of each
     * bar is proportional to the card's value, and random colors are assigned
     * to each bar for visual differentiation.</p>
     *
     * @param file_path the path where the generated PDF will be saved
     * @param deck_ID the unique identifier for the deck
     * @param total_cost the total energy cost of the cards in the deck
     * @param cardDeck a {@link HashMap} containing card names as keys and
     *                 their corresponding costs as values
     * @param invalid_cards a {@link List} of invalid card names that were
     *                      encountered during processing
     * @throws DocumentException if an error occurs while creating the PDF
     * @throws IOException if an error occurs while writing to the file
     */
    public static void generatePDF(String file_path, String deck_ID, int total_cost, HashMap<String, Integer> cardDeck, List<String> invalid_cards){

        try{
            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file_path));
            document.open();

            System.out.println(file_path);

            //include deck ID, total cost of cards, and histogram in PDF report
            document.add(new Paragraph("======================================"));
            document.add(new Paragraph("SLAY THE SPIRE DECK COST TALLY REPORT"));
            document.add(new Paragraph("======================================"));
            document.add(new Paragraph("Deck ID: " + deck_ID));
            document.add(new Paragraph("Total Cost: " + total_cost +" energy"));
            //list any invalid cards
            document.add(new Paragraph("---------------"));
            document.add(new Paragraph("Invalid Cards:"));
            document.add(new Paragraph("---------------"));

            for(int i = 0; i < invalid_cards.size(); i++){
                document.add(new Paragraph(invalid_cards.get(i)));
            }
            document.add(new Paragraph("--------------------"));
            document.add(new Paragraph("Histogram of Cards:"));
            document.add(new Paragraph("--------------------"));

            //creating the Histogram from the HashMap
            PdfContentByte canvas = writer.getDirectContent();
            float bar_width = 50f;
            float space_between_bars = 20f;
            float startX = 50f; //staring position for bars
            float startY = 200f;

            Random rand = new Random();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bf, 10); // Set font and size

            for(String key : cardDeck.keySet()) { //retrieve data from hashmap

                int value = cardDeck.get(key);
                float bar_height = value * 5;

                //generates a random color for each bar
                BaseColor color = new BaseColor(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                canvas.setColorFill(color);
                canvas.rectangle(startX, startY, bar_width, bar_height);
                canvas.fill();

                Paragraph barLabel = new Paragraph(key + " (" + value + " energy )"); //labeling bars
                barLabel.getFont().setColor(color); // Set the color of the label
                barLabel.setSpacingAfter(10);

                document.add(barLabel);

                // Move the starting x position for the next bar
                startX += bar_width + space_between_bars;
            }

            document.close();
            System.out.println("PDF histogram generated at: " + file_path);
        }
        catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generates a simple PDF report indicating that the deck is void.
     *
     * <p>This method creates a PDF document with a title, deck ID, and a
     * "VOID" message. The generated PDF serves as a placeholder or
     * indication that the specified deck is not valid or has no associated data.</p>
     *
     * @param file_path the path where the generated PDF will be saved
     * @param deck_ID the unique identifier for the deck
     * @throws DocumentException if an error occurs while creating the PDF
     * @throws IOException if an error occurs while writing to the file
     */

    public static void generateVoidPDF(String file_path, String deck_ID){

        try{
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_path)); //write to file
            document.open();
            document.add(new Paragraph("======================================"));
            document.add(new Paragraph("SLAY THE SPIRE DECK COST TALLY REPORT"));
            document.add(new Paragraph("======================================"));
            document.add(new Paragraph("Deck ID: " + deck_ID));
            document.add(new Paragraph("VOID"));
            document.close();

            System.out.println("PDF generated!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
