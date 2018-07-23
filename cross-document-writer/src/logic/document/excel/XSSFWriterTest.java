package logic.document.excel;

import java.awt.Color;
import java.io.File;
import logic.document.DocumentWriter;
import logic.document.DocumentWriterException;
import logic.document.MetaInformation;
import logic.document.Style;
import logic.document.element.misc.Image;
import logic.document.element.misc.NewLine;
import logic.document.element.table.Cell;
import logic.document.element.table.Table;
import logic.document.element.text.Paragraph;
import logic.document.element.text.Phrase;

/**
 *
 * @author Ario32
 */
public class XSSFWriterTest {

    private static File xssfFile;
    private static XSSFWriter xssfWriter;

    protected static void setUp() throws Exception {
        xssfFile = new File("test/xssf/xssfTest.xlsx");
        xssfFile.createNewFile();

        xssfWriter = new XSSFWriter(xssfFile);
        xssfWriter.setOrientation(DocumentWriter.FORMAT_LANDSCAPE);

        //xssfFile.deleteOnExit();

    }

    public static void main(String[] args) throws Exception {
        setUp();
        testCreation();
    }

    public static void testCreation() throws DocumentWriterException {
        Style s = new Style(Style.COURIER_NEW, 23.0, true, true, Color.RED, Color.CYAN, Style.ALIGNMENT_LEFT);
        MetaInformation metadata;
        metadata = new MetaInformation("Ario", "Test XSSF", "Fächerzuteilung", "Fächerzuteilung für die Matura", "Nur ein Test für XSSF");

        // NOT SUPPORTED IN EXCEL FORMAT
        //xssfWriter.addHeader(new Phrase("TestHeader", s));
        //xssfWriter.addFooter(new Phrase(String.format("erzeugt am %s", new SimpleDateFormat("dd.MM.yyyy, 'um' HH:mm").format(new Date()))));

        //Text things work like expected
        xssfWriter.addElement(new Paragraph("Testing").addElement(new Paragraph("Extra Testing")));
        xssfWriter.addElement(new NewLine());
        xssfWriter.addElement(new Phrase("Phrase Test", s).addElement(new Phrase("w00t")).addElement(new Phrase("DIE MOTHERFUCKER")));

        xssfWriter.addElement(new Image(new File("Rauchberger_Jesus.png")));

        Cell c = new Cell("test");
        c.addElement(new Image(new File("Rauchberger_Jesus.png")));
        //now to something more difficult :/
        Table t = new Table(4);
        t.addElement(new Cell("testStyle", s));
        t.addElement(c);
        t.addElement(new Cell("Test Nr. 3"));
        t.addElement(new Cell("Test Nr. 4"));
        t.addElement(new Cell("Test Nr. 5"));
        t.addElement(new Cell("Test Nr. 6"));
        
        xssfWriter.provideMetaData(metadata);
        xssfWriter.addElement(t);
        

        xssfWriter.writeToDisk();
    }
}
