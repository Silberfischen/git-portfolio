package logic.document.pdf;

import java.awt.Color;
import java.io.File;
import logic.document.DocumentWriterException;
import logic.document.Style;
import logic.document.element.misc.Image;
import logic.document.element.misc.NewLine;
import logic.document.element.table.Cell;
import logic.document.element.table.Table;
import logic.document.element.text.Paragraph;
import logic.document.element.text.Phrase;

/**
 *
 * @author Thorsten Jojart
 */
public class PDFWriterTest {

    private static File pdfFile;
    private static PDFWriter pdfWriter;

    protected static void setUp() throws Exception {
        pdfFile = new File("test/pdf/pdfTest.pdf");
        pdfFile.createNewFile();

        pdfWriter = new PDFWriter(pdfFile);

        //pdfFile.deleteOnExit();

    }

    public static void main(String[] args) throws Exception {
        setUp();
        testCreation();
    }

    public static void testCreation() throws DocumentWriterException {
        Style s = new Style(Style.COURIER_NEW, 23.0, true, true, Color.RED, Color.CYAN, Style.ALIGNMENT_LEFT);

        pdfWriter.addHeader(new Phrase("TestHeader", s));
        //pdfWriter.addFooter(new Phrase(String.format("erzeugt am %s", new SimpleDateFormat("dd.MM.yyyy, 'um' HH:mm").format(new Date()))));

        //Text things work like expected
        pdfWriter.addElement(new Paragraph("Testing").addElement(new Paragraph("Extra Testing")));
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new NewLine());
        pdfWriter.addElement(new Phrase("Phrase Test", s).addElement(new Phrase("w00t")));
        pdfWriter.addElement(new Image(new File("Rauchberger_Jesus.png")));

        Cell c = new Cell();
        c.addElement(new Image(new File("Rauchberger_Jesus.png")));
        //workz
        Table t = new Table(4);
        t.addElement(new Cell("testStyle", s));
        t.addElement(c);
        t.addElement(new Cell("Test Nr. 3"));
        t.addElement(new Cell("Test Nr. 4"));
        t.addElement(new Cell("Test Nr. 5"));
        t.addElement(new Cell("Test Nr. 6"));

        pdfWriter.addElement(t);

        pdfWriter.writeToDisk();
    }
}
